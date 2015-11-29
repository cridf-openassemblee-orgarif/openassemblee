package fr.cridf.babylone14166.security;

import java.util.*;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import fr.cridf.babylone14166.domain.Authority;
import fr.cridf.babylone14166.domain.User;
import fr.cridf.babylone14166.repository.UserRepository;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(UserDetailsService.class);

    @Inject
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);
        String lowercaseLogin = login.toLowerCase();
        Optional<User> userFromDatabase = userRepository.findOneByLogin(lowercaseLogin);
        if (!userFromDatabase.isPresent()) {
            throw new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the " +
                "database");
            }
        User user = userFromDatabase.get();
        if (!user.getActivated()) {
            throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        for (Authority a : user.getAuthorities()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(a.getName()));
        }
        return new org.springframework.security.core.userdetails.User(lowercaseLogin,
            user.getPassword(),
            grantedAuthorities);
    }
}
