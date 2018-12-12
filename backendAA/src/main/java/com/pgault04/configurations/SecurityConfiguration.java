/**
 *
 */
package com.pgault04.configurations;

import com.pgault04.services.UserDetailsServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.sql.DataSource;

/**
 * @author Paul Gault - 40126005
 *
 *         Security configuration class
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    @Autowired
    DataSource ds;

    @Autowired
    UserDetailsServiceImplementation userDetails;


    /**
     * Bean encodes passwords
     *
     * @return
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

    /**
     * Initialises user details service and password encoder
     *
     * @param auth
     * @throws Exception
     */
    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userDetails).passwordEncoder(passwordEncoder());
    }

    /**
     * Allows static resources to be available to login page before logging in i.e.
     * necessary images and css
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/static/css/**").addResourceLocations("/css/");
        registry.addResourceHandler("/resources/static/images/**").addResourceLocations("/images/");
        registry.addResourceHandler("/resources/static/js/**").addResourceLocations("/js/");
    }


    /**
     * The security configuration for the system.
     */
    @Override
    protected void configure(HttpSecurity https) throws Exception {

        https.cors().and()
                .authorizeRequests().antMatchers("/", "/main/login", "/logout", "/images/**", "/js/**", "/css/**").permitAll()
                .anyRequest().fullyAuthenticated()
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/main/login")
                .and().httpBasic().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authorizeRequests().antMatchers("/myModules/**", "/user/**", "/moduleHome/**", "/modules/**").access("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
                .and()
                .authorizeRequests().antMatchers("/admin").access("hasAnyRole('ROLE_ADMIN')")
                .and()
                .csrf().disable();


    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:4200");

            }
        };
    }

}
