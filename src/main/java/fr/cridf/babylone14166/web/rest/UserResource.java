package fr.cridf.babylone14166.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.function.*;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.codahale.metrics.annotation.Timed;

import fr.cridf.babylone14166.domain.Authority;
import fr.cridf.babylone14166.domain.User;
import fr.cridf.babylone14166.repository.AuthorityRepository;
import fr.cridf.babylone14166.repository.UserRepository;
import fr.cridf.babylone14166.repository.search.UserSearchRepository;
import fr.cridf.babylone14166.security.AuthoritiesConstants;
import fr.cridf.babylone14166.service.UserService;
import fr.cridf.babylone14166.web.rest.dto.ManagedUserDTO;
import fr.cridf.babylone14166.web.rest.util.HeaderUtil;
import fr.cridf.babylone14166.web.rest.util.PaginationUtil;

/**
 * REST controller for managing users.
 *
 * <p>This class accesses the User entity, and needs to fetch its collection of authorities.</p>
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and Authority,
 * and send everything to the client side: there would be no DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * </p>
 * <p>
 * We use a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </p>
 * <p>Another option would be to have a specific JPA entity graph to handle this case.</p>
 */
@RestController
@RequestMapping("/api")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private AuthorityRepository authorityRepository;

    @Inject
    private UserService userService;

    @Inject
    private UserSearchRepository userSearchRepository;

    /**
     * POST  /users -> Create a new user.
     */
    @RequestMapping(value = "/users",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<User> createUser(@RequestBody User user) throws URISyntaxException {
        log.debug("REST request to save User : {}", user);
        if (user.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new user cannot already have an ID").body(null);
        }
        User result = userRepository.save(user);
        return ResponseEntity.created(new URI("/api/users/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("user", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /users -> Updates an existing User.
     */
    @RequestMapping(value = "/users",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<ManagedUserDTO> updateUser(final @RequestBody ManagedUserDTO managedUserDTO) throws
        URISyntaxException {
        log.debug("REST request to update User : {}", managedUserDTO);
        return userRepository
            .findOneById(managedUserDTO.getId())
            .map(new Function<User, ResponseEntity>() {
                @Override
                public ResponseEntity apply(User user) {
                    user.setLogin(managedUserDTO.getLogin());
                    user.setFirstName(managedUserDTO.getFirstName());
                    user.setLastName(managedUserDTO.getLastName());
                    user.setEmail(managedUserDTO.getEmail());
                    user.setActivated(managedUserDTO.isActivated());
                    user.setLangKey(managedUserDTO.getLangKey());
                    final Set<Authority> authorities = user.getAuthorities();
                    authorities.clear();
                    managedUserDTO.getAuthorities().stream().forEach(new Consumer<String>() {
                        @Override
                        public void accept(String authority) {
                            authorities.add(authorityRepository.findOne(authority));
                        }
                    });
                    return ResponseEntity.ok()
                        .headers(HeaderUtil.createEntityUpdateAlert("user", managedUserDTO.getLogin()))
                        .body(new ManagedUserDTO(userRepository
                            .findOne(managedUserDTO.getId())));
                }
            })
            .orElseGet(new Supplier<ResponseEntity>() {
                @Override
                public ResponseEntity get() {
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            });
    }

    /**
     * GET  /users -> get all users.
     */
    @RequestMapping(value = "/users",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ManagedUserDTO>> getAllUsers(Pageable pageable)
        throws URISyntaxException {
        Page<User> page = userRepository.findAll(pageable);
        List<ManagedUserDTO> managedUserDTOs = new ArrayList<>();
        for (User u : page.getContent()) {
            managedUserDTOs.add(new ManagedUserDTO(u));
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users");
        return new ResponseEntity<>(managedUserDTOs, headers, HttpStatus.OK);
    }

    /**
     * GET  /users/:login -> get the "login" user.
     */
    @RequestMapping(value = "/users/{login}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ManagedUserDTO> getUser(@PathVariable String login) {
        log.debug("REST request to get User : {}", login);
        return userService.getUserWithAuthoritiesByLogin(login)
            .map(new Function<User, ManagedUserDTO>() {
                @Override
                public ManagedUserDTO apply(User user) {
                    return new ManagedUserDTO(user);
                }
            })
            .map(new Function<ManagedUserDTO, ResponseEntity>() {
                @Override
                public ResponseEntity apply(ManagedUserDTO managedUserDTO) {
                    return new ResponseEntity<>(managedUserDTO, HttpStatus.OK);
                }
            })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * SEARCH  /_search/users/:query -> search for the User corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/users/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<User> search(@PathVariable String query) {
        Iterable<User> result = userSearchRepository.search(queryStringQuery(query));
        List<User> users = new ArrayList<>();
        for (User u : result) {
            users.add(u);
        }
        return users;
    }
}
