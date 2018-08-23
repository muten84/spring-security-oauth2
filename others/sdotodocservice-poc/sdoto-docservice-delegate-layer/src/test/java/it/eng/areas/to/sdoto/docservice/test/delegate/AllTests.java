/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.test.delegate;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import it.eng.areas.to.sdoto.docservice.test.delegate.print.PrintBookingsServiceDelegateTest;

/**
 * @author Bifulco Luigi
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ DocumentServiceDelegateTest.class, PrintBookingsServiceDelegateTest.class })
public class AllTests {

}
