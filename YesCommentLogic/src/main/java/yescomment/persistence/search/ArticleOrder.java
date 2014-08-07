package yescomment.persistence.search;

import java.util.Date;

public enum ArticleOrder {
	ARTICLE_CREATE_DATE("createDate",Date.class),ARTICLE_LAST_COMMENT_DATE("lastCommentDate",Date.class),ARTICLE_COMMENT_COUNT("commentCount",Number.class);
	private final String attributeName;
	private final Class<?> attributeClass;
	public String getAttributeName() {
		return attributeName;
	}

	public Class<?> getAttributeClass() {
		return attributeClass;
	}

	private ArticleOrder(String attributeName, Class<?> attributeClass) {
		this.attributeName = attributeName;
		this.attributeClass = attributeClass;
	}

	
}
