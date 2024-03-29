package openassemblee.config;

import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import openassemblee.security.*;
import openassemblee.web.filter.CsrfCookieGeneratorFilter;
import openassemblee.web.filter.DevCorsFilter;
import openassemblee.web.rest.HemicycleArchiveResource;
import openassemblee.web.rest.HemicyclePlanResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.csrf.CsrfFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${spring.profiles.active}")
    private String profiles;

    @Inject
    private Environment env;

    @Inject
    private AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler;

    @Inject
    private AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler;

    @Inject
    private AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;

    @Inject
    private Http401UnauthorizedEntryPoint authenticationEntryPoint;

    @Inject
    private UserDetailsService userDetailsService;

    @Inject
    private RememberMeServices rememberMeServices;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Inject
    public void configureGlobal(AuthenticationManagerBuilder auth)
        throws Exception {
        auth
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
            .ignoring()
            .antMatchers("/scripts/**/*.{js,html}")
            .antMatchers("/bower_components/**")
            .antMatchers("/i18n/**")
            .antMatchers("/assets/**")
            .antMatchers("/test/**")
            .antMatchers("/console/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        List<String> springProfile = Arrays.asList(profiles.split(","));
        if (springProfile.contains("dev")) {
            // Pour le cross domain en dev sur l'hémicycle en React (requêtes get et post)
            http
                .addFilterAfter(new DevCorsFilter(), CsrfFilter.class)
                .authorizeRequests()
                .antMatchers(
                    "/api/" +
                    HemicyclePlanResource.hemicyclePlansAssociationsUrl +
                    "/**"
                )
                .permitAll()
                .antMatchers(
                    "/api/" +
                    HemicycleArchiveResource.hemicycleArchivesUrl +
                    "/**"
                )
                .permitAll()
                .antMatchers(
                    "/api/" +
                    HemicycleArchiveResource.hemicycleArchivesDataUrl +
                    "/**"
                )
                .permitAll()
                .antMatchers("/api/elus")
                .permitAll();
            // Pour désactiver le csrf en dev sur l'hémicycle en React (post requests only)
            http
                .csrf()
                .ignoringAntMatchers(
                    "/api/" +
                    HemicyclePlanResource.hemicyclePlansAssociationsUrl +
                    "/**"
                )
                .ignoringAntMatchers(
                    "/api/" +
                    HemicycleArchiveResource.hemicycleArchivesUrl +
                    "/**"
                );
        }
        http
            .csrf()
            .ignoringAntMatchers("/remote/**")
            .ignoringAntMatchers("/api/publicdata/**")
            .ignoringAntMatchers("/healthcheck")
            .ignoringAntMatchers("/websocket/**")
            .and()
            .addFilterAfter(new CsrfCookieGeneratorFilter(), CsrfFilter.class)
            .exceptionHandling()
            .authenticationEntryPoint(authenticationEntryPoint)
            .and()
//            .rememberMe()
//            .rememberMeServices(rememberMeServices)
//            .rememberMeParameter("remember-me")
//            .key(env.getProperty("jhipster.security.rememberme.key"))
//            .and()
            .formLogin()
            .loginProcessingUrl("/api/authentication")
            .successHandler(ajaxAuthenticationSuccessHandler)
            .failureHandler(ajaxAuthenticationFailureHandler)
            .usernameParameter("j_username")
            .passwordParameter("j_password")
            .permitAll()
            .and()
            .logout()
            .logoutUrl("/api/logout")
            .logoutSuccessHandler(ajaxLogoutSuccessHandler)
            .deleteCookies("JSESSIONID")
            .permitAll()
            .and()
            .headers()
            .frameOptions()
            .disable()
            .and()
            .authorizeRequests()
            .antMatchers("/api/register")
            .permitAll()
            .antMatchers("/api/activate")
            .permitAll()
            .antMatchers("/api/authenticate")
            .permitAll()
            .antMatchers("/api/account/reset_password/init")
            .permitAll()
            .antMatchers("/api/account/reset_password/finish")
            .permitAll()
            .antMatchers("/api/logs/**")
            .hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/api/audits/**")
            .hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/api/publicdata/**")
            .permitAll()
            // pour "publication"
            .antMatchers("/api/elus/**")
            .permitAll()
            .antMatchers("/api/search/**")
            .permitAll()
            .antMatchers("/api/commission-permanente/**")
            .permitAll()
            .antMatchers("/api/executif/**")
            .permitAll()
            .antMatchers("/api/groupePolitiques/**")
            .permitAll()
            .antMatchers("/api/groupePolitiques-dtos")
            .permitAll()
            .antMatchers("/api/commissionThematiques/**")
            .permitAll()
            .antMatchers("/api/commissionThematiques-dtos")
            .permitAll()
            .antMatchers("/api/**")
            .authenticated()
            .antMatchers("/metrics/**")
            .hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/health/**")
            .hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/trace/**")
            .hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/dump/**")
            .hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/shutdown/**")
            .hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/beans/**")
            .hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/configprops/**")
            .hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/info/**")
            .hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/autoconfig/**")
            .hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/env/**")
            .hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/trace/**")
            .hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/mappings/**")
            .hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/liquibase/**")
            .hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/v2/api-docs/**")
            .permitAll()
            .antMatchers("/configuration/security")
            .permitAll()
            .antMatchers("/configuration/ui")
            .permitAll()
            .antMatchers("/protected/**")
            .authenticated();
    }

    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }
}
