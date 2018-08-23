package it.eng.areas.ems.sdodaeservices.test;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import it.eng.areas.ems.sdodaeservices.delegate.config.TemplateParserContext;
import it.eng.areas.ems.sdodaeservices.delegate.model.Dae;
import it.eng.areas.ems.sdodaeservices.delegate.model.Responsabile;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { MailDelegateTestCtx.class })
@ActiveProfiles(profiles = "oracle")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MailDelegateTest {

	@Test
	public void testPreparemail() {
		try {
			Dae d = new Dae();
			d.setResponsabile(new Responsabile());
			d.getResponsabile().setNome("Pippo");
			d.getResponsabile().setCognome("Pluto");

			String template = "Test template : Responsabile ${responsabile.nome} ${responsabile.cognome}, ${responsabile.telefono}";

			ExpressionParser parser = new SpelExpressionParser();
			Expression exp = parser.parseExpression(template, new TemplateParserContext());

			String mail = (String) exp.getValue(d);

			System.out.println(mail);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

}
