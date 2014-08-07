package yescomment.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import yescomment.integrationtest.ArticleCommentPersistenceIT;
import yescomment.integrationtest.ArticleImageIT;
import yescomment.integrationtest.ArticleKeywordsIT;
import yescomment.integrationtest.ArticleLanguageIT;
import yescomment.integrationtest.ArticleTitleIT;
import yescomment.integrationtest.DetailedSearchIT;
import yescomment.integrationtest.KeywordRetrieveIT;
import yescomment.integrationtest.NotHtmlIT;
import yescomment.integrationtest.RSSFeedIT;
import yescomment.integrationtest.RecommendedArticlesIT;
import yescomment.integrationtest.SchemaIT;
import yescomment.integrationtest.URLErrorIT;
import yescomment.integrationtest.URLIT;
import yescomment.integrationtest.UserPersistenceIT;
import yescomment.test.PaginationTest;
import yescomment.test.SHA256Test;

@RunWith(Suite.class)
@SuiteClasses({ArticleImageIT.class,ArticleKeywordsIT.class, ArticleLanguageIT.class,  ArticleTitleIT.class,NotHtmlIT.class ,SchemaIT.class, URLErrorIT.class, URLIT.class,ArticleCommentPersistenceIT.class,DetailedSearchIT.class,RecommendedArticlesIT.class,RSSFeedIT.class,KeywordRetrieveIT.class,UserPersistenceIT.class})
public class AllIntegrationTestsSuite {

}
