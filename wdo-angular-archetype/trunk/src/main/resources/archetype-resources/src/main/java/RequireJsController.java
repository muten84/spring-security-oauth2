#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * 
 */
package ${package};

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.webjars.RequireJS;

/**
 * @author Bifulco Luigi
 *
 */
@Controller
public class RequireJsController {

	@ResponseBody
	@RequestMapping(value = "/webjarsjs", produces = "application/javascript")
	public String webjarjs() {
		return RequireJS.getSetupJavaScript("/webjars/");
	}
}
