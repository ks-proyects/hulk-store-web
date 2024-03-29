package com.hulkstoreweb.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SecurityFilterController {

	@RequestMapping(value = "/**/{[path:[^\\.]*}")
	public String index(final HttpServletRequest request, HttpServletResponse response) {
	    final String url = request.getRequestURI();

	    if (!url.startsWith("/api")) {
		    return "forward:/index.html";
		}
	    
	    response.setStatus(404);
	    return null;

	}
}
