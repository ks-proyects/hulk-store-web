package com.hulkstoreweb.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hulkstoreweb.db.hulkstoreweb_db.service.SecurityService;

public class CommonAuthenticationTokenFilter extends OncePerRequestFilter {
	
	@Autowired
	private SecurityService securityService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
    	
    	String token = request.getHeader("Authorization");

        if ( token != null && !token.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {
        	token = token.replace("Bearer ", "");
        	try{
        		
        		String jsonUserDetails = securityService.verifyTokenJson(token);
                UserDetails userDetails = prepareUserDetails(jsonUserDetails);

                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                
        	}catch(Exception e){
        		logger.error(e.getMessage());
        	}
        	
        }

        chain.doFilter(request, response);
    }
    
    private UserDetails prepareUserDetails(String jsonUserDetails) throws JsonProcessingException, IOException{
    	
    	ObjectMapper objectMapper = new ObjectMapper();
    	JsonNode root = objectMapper.readTree(jsonUserDetails);
    	
    	String userId = root.get("_id").asText();
    	String username = root.get("username").asText();

    	
    	List<SimpleGrantedAuthority> authorities = new ArrayList<>();
    	authorities.add(new SimpleGrantedAuthority("ROLE_PRIVATE_USER"));
    	if(root.get("roles") != null) {
        	for(JsonNode role : root.get("roles")) {
        		authorities.add(new SimpleGrantedAuthority("ROLE_" + role.asText()));    		
        	}
    	}
    	
    	return new AuthUser(userId, username, authorities);
    }
}

