package yescomment.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import yescomment.test.CaptchaTest;
import yescomment.test.PaginationTest;
import yescomment.test.SHA256Test;
import yescomment.test.StringUtilTest;

@RunWith(Suite.class)
@SuiteClasses({PaginationTest.class, SHA256Test.class,StringUtilTest.class,CaptchaTest.class})
public class AllUnitTestsSuite {

}
