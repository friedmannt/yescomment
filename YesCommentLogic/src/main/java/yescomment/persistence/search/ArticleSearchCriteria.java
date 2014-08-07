package yescomment.persistence.search;

import java.io.Serializable;
import java.util.Date;

import yescomment.util.ArticleLanguage;


/**
 * Search parameter class for holding article search criteria. If any param is not filled, that parameter is not narrowing the search
 * @author Friedmann Tamás
 *
 */
public class ArticleSearchCriteria implements Serializable {

	/**
	 * Bounds are inclusive, null bound means no boundary
	 * @author Friedmann Tamás
	 *
	 */
	public static enum CommentCountRange {
		ZERO(0,0),BETWEEN1AND10(1,10),BETWEEN11AND100(11,100),BETWEEN101AND1000(101,1000),ABOVE1000(1001,null);
		private final Integer lowerBound;
		private final Integer upperBound;
		private CommentCountRange(Integer lowerBound, Integer upperBound) {
			this.lowerBound = lowerBound;
			this.upperBound = upperBound;
		}
		public Integer getLowerBound() {
			return lowerBound;
		}
		public Integer getUpperBound() {
			return upperBound;
		}
		
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * case insensitive, not exact, like search
	 */
	private String title;
	
	/**
	 * case insensitive, exact match
	 */
	private String site;
	
	/**
	 * exact
	 */
	private ArticleLanguage language;
	
	
	/**
	 * inclusive
	 */
	private Date createdAfter;
	
	private Date createdBefore;
	
	/**
	 * exact, case sensitive
	 */
	private String commenter;
	
	private CommentCountRange commentCountRange;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}


	public ArticleLanguage getLanguage() {
		return language;
	}

	public void setLanguage(ArticleLanguage language) {
		this.language = language;
	}

	public Date getCreatedAfter() {
		return createdAfter;
	}

	public void setCreatedAfter(Date createdAfter) {
		this.createdAfter = createdAfter;
	}

	public Date getCreatedBefore() {
		return createdBefore;
	}

	public void setCreatedBefore(Date createdBefore) {
		this.createdBefore = createdBefore;
	}

	public String getCommenter() {
		return commenter;
	}

	public void setCommenter(String commenter) {
		this.commenter = commenter;
	}



	public CommentCountRange getCommentCountRange() {
		return commentCountRange;
	}

	public void setCommentCountRange(CommentCountRange commentCountRange) {
		this.commentCountRange = commentCountRange;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((commentCountRange == null) ? 0 : commentCountRange.hashCode());
		result = prime * result + ((commenter == null) ? 0 : commenter.hashCode());
		result = prime * result + ((createdAfter == null) ? 0 : createdAfter.hashCode());
		result = prime * result + ((createdBefore == null) ? 0 : createdBefore.hashCode());
		result = prime * result + ((language == null) ? 0 : language.hashCode());
		result = prime * result + ((site == null) ? 0 : site.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ArticleSearchCriteria)) {
			return false;
		}
		ArticleSearchCriteria other = (ArticleSearchCriteria) obj;
		if (commentCountRange != other.commentCountRange) {
			return false;
		}
		if (commenter == null) {
			if (other.commenter != null) {
				return false;
			}
		} else if (!commenter.equals(other.commenter)) {
			return false;
		}
		if (createdAfter == null) {
			if (other.createdAfter != null) {
				return false;
			}
		} else if (!createdAfter.equals(other.createdAfter)) {
			return false;
		}
		if (createdBefore == null) {
			if (other.createdBefore != null) {
				return false;
			}
		} else if (!createdBefore.equals(other.createdBefore)) {
			return false;
		}
		if (language == null) {
			if (other.language != null) {
				return false;
			}
		} else if (!language.equals(other.language)) {
			return false;
		}
		if (site == null) {
			if (other.site != null) {
				return false;
			}
		} else if (!site.equals(other.site)) {
			return false;
		}
		if (title == null) {
			if (other.title != null) {
				return false;
			}
		} else if (!title.equals(other.title)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ArticleSearchCriteria [title=" + title + ", site=" + site + ", language=" + language + ", createdAfter=" + createdAfter + ", createdBefore=" + createdBefore + ", commenter=" + commenter + ", commentCountRange=" + commentCountRange + "]";
	}

	
}
