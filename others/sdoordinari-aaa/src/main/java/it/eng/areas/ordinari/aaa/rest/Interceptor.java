/**
 * 
 */
package it.eng.areas.ordinari.aaa.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Bifulco Luigi
 *
 */
// @Component
public class Interceptor implements HandlerInterceptor {

	// @Autowired
	// private CacheTokenService tokenService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("---Before Method Execution---");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return false;
		}
		if (auth.getPrincipal() == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return false;
		}
		String token = request.getHeader("X-Auth-Token");
		// tokenService.renew(token);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		System.out.println("---method executed---");
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// if (ex != null) {
		// String token = request.getHeader("X-Auth-Token");
		// tokenService.renew(token);
		// }
		System.out.println("---Request Completed---");
	}
}