package com.pgault04.configurations;

import com.pgault04.services.UserDetailsServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.sql.DataSource;

/**
 * Security configuration class
 *
 * @author Paul Gault - 40126005
 * @since November 2018
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    /*
     *The datasource is configured in the application.properties file
     */
    @Autowired
    DataSource ds;

    /*
     * The URL of where the front-end application is running from in each environment
     */
    @Value("${app.url}")
    String appUrl;

    /*
     * Authorization service
     */
    @Autowired
    UserDetailsServiceImplementation userDetails;

    /**
     * Bean encodes passwords
     *
     * @return new password encoder
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     * Initialises user details service and password encoder
     */
    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetails).passwordEncoder(passwordEncoder());
    }

    /*
     * The security configuration for the system.
     */
    @Override
    protected void configure(HttpSecurity https) throws Exception {
        https.cors().and()
                .authorizeRequests().antMatchers("/", "/main/login", "/user/getUsernames", "/user/createProfile", "/user/requestResetPassword", "/user/resetPassword", "/images/**", "/js/**", "/css/**", "/static/**").permitAll()
                .anyRequest().fullyAuthenticated()
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/main/login")
                .and().httpBasic().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests().antMatchers("/myModules/**", "/user/**", "/moduleHome/**", "/modules/**").access("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
                .and()
                .authorizeRequests().antMatchers("/user/getTutorRequests", "/user/approveTutorRequest", "/user/rejectTutorRequest", "/modules/getModuleRequests", "/modules/approveModuleRequest", "/modules/rejectModuleRequest", "/user/makeAdmin", "/user/makeTutor", "/user/removeUser", "/user/addUsers").access("hasAnyRole('ROLE_ADMIN')")
                .and()
                .csrf().disable();
    }

    /**
     * Method to ensure that backend server allows communication from the front end application
     *
     * @return a web mvc config
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins(appUrl);
            }
        };
    }
}