package cz.sangis.personal.cv_builder_app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    public void configureGlobal( AuthenticationManagerBuilder auth )
            throws Exception {
        auth.inMemoryAuthentication().withUser( "user" )
                .password( passwordEncoder().encode( "password" ) ).roles( "USER" );
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public static UserDetailsService userDetailsService( PasswordEncoder bCryptPasswordEncoder ) {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser( User.withUsername( "user" )
                .password( bCryptPasswordEncoder.encode( "userPass" ) )
                .roles( "USER" )
                .build() );
        manager.createUser( User.withUsername( "admin" )
                .password( bCryptPasswordEncoder.encode( "adminPass" ) )
                .roles( "USER", "ADMIN" )
                .build() );
        return manager;
    }

    @Bean
    public static SecurityFilterChain filterChain( HttpSecurity http ) throws Exception {
        http.csrf()
                .disable()
                .authorizeRequests()
                .requestMatchers( HttpMethod.DELETE )
                .hasRole( "ADMIN" )
                .requestMatchers( "/admin/**" )
                .hasAnyRole( "ADMIN" )
                .requestMatchers( "/user/**" )
                .hasAnyRole( "USER", "ADMIN" )
                .requestMatchers( "/login/**" )
                .anonymous()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic()
                .and()
                .sessionManagement()
                .sessionCreationPolicy( SessionCreationPolicy.STATELESS );

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        boolean securityDebug = true;
        return ( web ) -> web.debug( securityDebug )
                .ignoring()
                .requestMatchers( "/css/**", "/js/**", "/img/**", "/lib/**", "/favicon.ico" );
    }

}
