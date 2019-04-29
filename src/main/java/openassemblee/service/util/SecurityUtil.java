package openassemblee.service.util;

import org.springframework.security.core.Authentication;

public class SecurityUtil {

    public static boolean isAdmin(Authentication auth) {
        return auth != null && auth.getAuthorities().stream()
            .filter(a -> a.getAuthority().equals("ROLE_ADMIN"))
            .count() > 0;
    }

}
