package openassemblee.service;

import openassemblee.domain.Authority;
import openassemblee.domain.PersistentToken;
import openassemblee.domain.User;
import openassemblee.repository.AuthorityRepository;
import openassemblee.repository.PersistentTokenRepository;
import openassemblee.repository.UserRepository;
import openassemblee.repository.search.UserSearchRepository;
import openassemblee.security.SecurityUtils;
import openassemblee.service.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserSearchRepository userSearchRepository;

    @Inject
    private PersistentTokenRepository persistentTokenRepository;

    @Inject
    private AuthorityRepository authorityRepository;

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        Optional<User> optional = userRepository.findOneByActivationKey(key);
        if (optional.isPresent()) {
            User user = optional.get();
            // activate given user for the registration key.
            user.setActivated(true);
            user.setActivationKey(null);
            userRepository.save(user);
            userSearchRepository.save(user);
            log.debug("Activated user: {}", user);
            return optional;
        }
        return Optional.empty();
    }

    public Optional<User> completePasswordReset(final String newPassword, String key) {
       log.debug("Reset user password for reset key {}", key);

       return userRepository.findOneByResetKey(key)
           .filter(new Predicate<User>() {
               @Override
               public boolean test(User user) {
                   ZonedDateTime oneDayAgo = ZonedDateTime.now().minusHours(24);
                   return user.getResetDate().isAfter(oneDayAgo);
               }
           })
           .map(new Function<User, User>() {
               @Override
               public User apply(User user) {
                   user.setPassword(passwordEncoder.encode(newPassword));
                   user.setResetKey(null);
                   user.setResetDate(null);
                   userRepository.save(user);
                   return user;
               }
           });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository.findOneByEmail(mail)
            .filter(new Predicate<User>() {
                @Override
                public boolean test(User user) {
                    return user.getActivated();
                }
            })
            .map(new Function<User, User>() {
                @Override
                public User apply(User user) {
                    user.setResetKey(RandomUtil.generateResetKey());
                    user.setResetDate(ZonedDateTime.now());
                    userRepository.save(user);
                    return user;
                }
            });
    }

    public User createUserInformation(String login, String password, String firstName, String lastName, String email,
        String langKey) {

        User newUser = new User();
        Authority authority = authorityRepository.findOne("ROLE_USER");
        Set<Authority> authorities = new HashSet<>();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(login);
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setLangKey(langKey);
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        authorities.add(authority);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        userSearchRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public void updateUserInformation(final String firstName, final String lastName, final String email,
        final String langKey) {
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUser().getUsername());
        if (user.isPresent()) {
            User u = user.get();
            u.setFirstName(firstName);
            u.setLastName(lastName);
            u.setEmail(email);
            u.setLangKey(langKey);
            userRepository.save(u);
            userSearchRepository.save(u);
            log.debug("Changed Information for User: {}", u);
        }
    }

    public void changePassword(final String password) {
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUser().getUsername());
        if (user.isPresent()) {
            User u = user.get();
            String encryptedPassword = passwordEncoder.encode(password);
            u.setPassword(encryptedPassword);
            userRepository.save(u);
            log.debug("Changed password for User: {}", u);
        }
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneByLogin(login).map(new Function<User, User>() {
            @Override
            public User apply(User u) {
                u.getAuthorities().size();
                return u;
            }
        });
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities(Long id) {
        User user = userRepository.findOne(id);
        user.getAuthorities().size(); // eagerly load the association
        return user;
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities() {
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUser().getUsername()).get();
        user.getAuthorities().size(); // eagerly load the association
        return user;
    }

    /**
     * Persistent Token are used for providing automatic authentication, they should be automatically deleted after
     * 30 days.
     * <p/>
     * <p>
     * This is scheduled to get fired everyday, at midnight.
     * </p>
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void removeOldPersistentTokens() {
        LocalDate now = LocalDate.now();
        List<PersistentToken> tokens = persistentTokenRepository.findByTokenDateBefore(now.minusMonths(1));
        for (PersistentToken token : tokens) {
            log.debug("Deleting token {}", token.getSeries());
            User user = token.getUser();
            user.getPersistentTokens().remove(token);
            persistentTokenRepository.delete(token);

        }
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p/>
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     * </p>
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        ZonedDateTime now = ZonedDateTime.now();
        List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minusDays(3));
        for (User user : users) {
            log.debug("Deleting not activated user {}", user.getLogin());
            userRepository.delete(user);
            userSearchRepository.delete(user);
        }
    }
}
