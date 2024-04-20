// The following code is modified from https://www.codejava.net/frameworks/spring-boot/user-registration-and-login-tutorial
// Token based remember me functionality based on https://github.com/lizard-buzzard/persistent-token-rememberme-authentication/blob/master/README.md
package com.jmel.inv3st0r.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
public class WebSecurityConfig {
    @Bean
    UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(@Autowired PasswordEncoder passwordEncoder,
                                                            @Autowired UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);

        return authProvider;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http, @Autowired
    DaoAuthenticationProvider authenticationProvider) throws Exception {
        http.authenticationProvider(authenticationProvider);

        http.authorizeHttpRequests((AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorize) ->
                authorize
                        .requestMatchers("/register", "/login")
                        .permitAll()
                        .anyRequest()
                        .authenticated());

        http.formLogin(form -> form
                .usernameParameter("email")
                .loginPage("/login")
                .permitAll());

        http.requestCache(RequestCacheConfigurer::disable);

        http.rememberMe(r -> {
                    r.tokenRepository(persistentTokenRepository());
                    r.rememberMeParameter("rememberMe");
                    r.tokenValiditySeconds(60 * 60 * 24); // 24 hours
                }
        );

        return http.build();
    }

    @Autowired
    DataSource dataSource;

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        MyJdbcTokenRepositoryImpl tokenRepository = new MyJdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }
}
