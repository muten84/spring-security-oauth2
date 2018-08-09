package it.eng.areas.ems.sdodaeservices.test;

import java.util.List;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import it.eng.areas.ems.sdodaeservices.dao.impl.rule.UtenteDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.delegate.UserDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.model.Utente;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.UtenteFilter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { UserDelegateTestCtx.class })
@ActiveProfiles(profiles = "oracle")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserDelegateTest {

	@Autowired
	UserDelegateService utenteService;

	@Ignore
	public void testSearchutente() {
		try {
			UtenteFilter filter = new UtenteFilter();
			filter.setFetchRule(UtenteDeepDepthRule.NAME);
			List<Utente> utenti = utenteService.searchUtenteByFilter(filter);

			Assert.assertTrue(utenti.size() > 0);

		} catch (Exception e) {

			e.printStackTrace();

			Assert.fail();
		}
	}

}
