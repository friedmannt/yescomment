package yescomment.articleretriever;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.Future;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.validation.constraints.NotNull;

import yescomment.util.ArticleInfo;

@Stateless
public class AsynchArticleInfoRetriever implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Asynchronous
	public Future<ArticleInfo> retrieveArticleInfo(@NotNull String urlString) throws IOException  {
		ArticleInfo ai = ArticleInfoRetriever.getArticleInfoFromURL(urlString);
		return new AsyncResult<ArticleInfo>(ai);
	}

}
