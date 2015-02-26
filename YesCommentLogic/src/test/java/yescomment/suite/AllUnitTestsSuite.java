package yescomment.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import yescomment.test.CaptchaTest;
import yescomment.test.CommentLinkDetectorTest;
import yescomment.test.JAXBTest;
import yescomment.test.PaginationTest;
import yescomment.test.SHA256Test;
import yescomment.test.StringUtilTest;
import yescomment.test.TranslationTest;

@RunWith(Suite.class)
@SuiteClasses({PaginationTest.class, SHA256Test.class,StringUtilTest.class,CaptchaTest.class,TranslationTest.class,JAXBTest.class,CommentLinkDetectorTest.class})
public class AllUnitTestsSuite {

}
