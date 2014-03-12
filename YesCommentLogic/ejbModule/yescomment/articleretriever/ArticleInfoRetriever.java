package yescomment.articleretriever;

import java.io.IOException;
import java.util.concurrent.Future;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.validation.constraints.NotNull;

import yescomment.util.ArticleInfo;
import yescomment.util.URLUtil;

@Stateless
public class ArticleInfoRetriever {

	@Asynchronous
	public Future<ArticleInfo> retrieveArticleInfo(@NotNull String urlString) throws IOException {
		ArticleInfo ai = URLUtil.getArticleInfoFromURL(urlString);
		 return new AsyncResult<ArticleInfo>(ai);
	}
}
