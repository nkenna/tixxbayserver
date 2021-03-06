/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.config;

import com.steinacoz.tixx.tixxbayserver.config.jwtservice.JwtAuthenticationEntryPoint;
import com.steinacoz.tixx.tixxbayserver.config.jwtservice.JwtAuthenticationFilter;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
 *
 * @author nkenn
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer{
   //@Resource(name = "userRepo")
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;
    
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(encoder());
    }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

    @Bean
    public JwtAuthenticationFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationFilter();
    }
    
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/tixxbay/api/user/v1/auth/login-user").permitAll()
               // .antMatchers(HttpMethod.GET, "/tixxbay/api/user/v1/all-users").permitAll()
                .antMatchers(HttpMethod.POST, "/tixxbay/api/user/v1/create-user").permitAll()
                .antMatchers(HttpMethod.POST, "/tixxbay/api/user/v1/create-role").permitAll()
                .antMatchers(HttpMethod.GET, "/tixxbay/api/event/v1/all-events").permitAll()
                .antMatchers(HttpMethod.GET, "/tixxbay/api/event/v1/all-events-by-time-range").permitAll()
                .antMatchers(HttpMethod.PUT, "/tixxbay/api/event/v1/all-events-by-name").permitAll()
                .antMatchers(HttpMethod.PUT, "/tixxbay/api/event/v1/all-events-by-state").permitAll()
                .antMatchers(HttpMethod.GET, "/tixxbay/api/event/v1/all-events-by-random").permitAll()
                .antMatchers(HttpMethod.PUT, "/tixxbay/api/event/v1/all-events-by-gps").permitAll()
                .antMatchers(HttpMethod.GET, "/tixxbay/api/event/v1/all-states").permitAll()
                .antMatchers(HttpMethod.GET, "/tixxbay/api/event/v1/all-cities").permitAll()
                .antMatchers(HttpMethod.POST, "/tixxbay/api/event/v1/event-by-code").permitAll()
                .antMatchers(HttpMethod.POST, "/tixxbay/api/mailing/v1/add-email-list").permitAll()
                .antMatchers(HttpMethod.POST, "/tixxbay/api/mailing/v1/remove-email-list").permitAll()
                .antMatchers(HttpMethod.GET, "/tixxbay/api/mailing/v1/all-email-list").permitAll()
                .antMatchers(HttpMethod.GET, "/tixxbay/api/user/v1/auth/reset-user-password-2/**").permitAll()
                .antMatchers(HttpMethod.GET, "/tixxbay/api/user/v1/verify-user/**").permitAll()
                .antMatchers(HttpMethod.GET, "/tixxbay/api/category/v1/all-events-category").permitAll()
                .antMatchers(HttpMethod.GET, "/tixxbay/api/discount/v1/get-discounts").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        
        http
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
        
    }
    
    
    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }
    
}













































