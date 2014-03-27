package yescomment.test.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import yescomment.test.ArticleImageTest;
import yescomment.test.ArticleTitleTest;
import yescomment.test.CommentPermissionTest;
import yescomment.test.HTMLParseTest;
import yescomment.test.PaginationTest;
import yescomment.test.URLErrorTest;
import yescomment.test.URLTest;

@RunWith(Suite.class)
@SuiteClasses({ HTMLParseTest.class, ArticleImageTest.class, ArticleTitleTest.class,/* CommentPermissionTest.class,*/ HTMLParseTest.class, PaginationTest.class, URLErrorTest.class, URLTest.class })
public class AllTestsSuite {

}
