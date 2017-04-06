/**
 * 
 */
package es.jma.prestamos.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Permitir conexiones desde cualquier dominio
 * @author jmiranda
 *
 */
public class CORSFilter extends OncePerRequestFilter{
	
	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
	    		response.setHeader("Access-Control-Allow-Origin", "*");
	    		response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE");
	    		response.setHeader("Access-Control-Max-Age", "3600");
	    		response.setHeader("Access-Control-Max-Age", "3600");
	    		response.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

		
		filterChain.doFilter(request, response);
		
	}

}
