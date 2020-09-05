/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.config.jwtservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.steinacoz.tixx.tixxbayserver.config.jwtservice.JwtTokenUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import com.steinacoz.tixx.tixxbayserver.utils.Utils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import java.util.Arrays;
import static java.util.Collections.emptyList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 *
 * @author nkenn
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {
     @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(Utils.HEADER_STRING);
        String email = null;
        String authToken = null;
        if (header != null && header.startsWith(Utils.TOKEN_PREFIX)) {
            authToken = header.replace(Utils.TOKEN_PREFIX,"");
            try {
                email = jwtTokenUtil.getEmailFromToken(authToken);
            } catch (IllegalArgumentException e) {
                System.out.println("an error occured during getting email from token" + e.getMessage());
                
            } catch (ExpiredJwtException e) {
                System.out.println("the token is expired and not valid anymore" + e.getMessage());
               
            } catch(SignatureException e){
                System.out.println("Authentication Failed. email or Password not valid." + e.getMessage());
                
            }
        } else {
            System.out.println("couldn't find bearer string, will ignore the header");
           
        }
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println(jwtTokenUtil.getRoleFromToken(authToken));
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                System.out.println("authenticated user " + email + ", setting security context");
                
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(req, res);
    }
}










