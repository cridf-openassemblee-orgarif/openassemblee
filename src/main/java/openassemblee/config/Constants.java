package openassemblee.config;

import java.time.ZoneId;

/**
 * Application constants.
 */
public final class Constants {

    // Spring profile for development, production and "fast", see http://jhipster.github.io/profiles.html
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";
    public static final String SPRING_PROFILE_FAST = "fast";
    // Spring profile used when deploying with Spring Cloud (used when deploying to CloudFoundry)
    public static final String SPRING_PROFILE_CLOUD = "cloud";
    // Spring profile used when deploying to Heroku
    public static final String SPRING_PROFILE_HEROKU = "heroku";

    public static final String SPRING_PROFILE_CLEVERCLOUD = "clevercloud";
    public static final String SPRING_PROFILE_MLO = "mlo";

    public static final String SYSTEM_ACCOUNT = "system";

    public static final String parisZoneIdAsString = "Europe/Paris";
    public static final ZoneId parisZoneId = ZoneId.of(parisZoneIdAsString);

    private Constants() {}
}
