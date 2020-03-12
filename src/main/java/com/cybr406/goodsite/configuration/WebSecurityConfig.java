package com.cybr406.goodsite.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    Environment env;

    @Value("${spring.h2.console.enabled}")
    boolean h2ConsoleEnabled;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/**/*.ico").permitAll()
                .antMatchers("/").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
            .logout()
                .permitAll();

        if (Arrays.asList(env.getActiveProfiles()).contains("withoutCsrf")) {
            http.csrf().disable();
        }
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        User.UserBuilder builder = User.withDefaultPasswordEncoder();
        return new InMemoryUserDetailsManager(
                builder.username("user").password("password").roles("USER").build(),
                builder.username("friend").password("password").roles("USER").build(),
                builder.username("hacker").password("password").roles("USER").build()
        );
    }
    
}