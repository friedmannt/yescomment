package yescomment.test.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import yescomment.test.HTMLParseTest;
import yescomment.test.URLTest;

@RunWith(Suite.class)
@SuiteClasses({ HTMLParseTest.class,
		 URLTest.class })
public class AllTestsSuite {

}
