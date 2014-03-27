package yescomment.managedbean.keyword;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class TagCloudItem implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@NotNull
	private String tag;
	private String url;
	@Min(1)
	@Max(5)
	private int strength;
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getStrength() {
		return strength;
	}
	public void setStrength(int strength) {
		this.strength = strength;
	}
	public TagCloudItem(String tag, String url, int strength) {
		super();
		this.tag = tag;
		this.url = url;
		this.strength = strength;
	}
	public TagCloudItem(String tag, int strength) {
		this(tag, null, strength);
		
	}
	@Override
	public String toString() {
		return "TagCloudItem [tag=" + tag + ", url=" + url + ", strength=" + strength + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + strength;
		result = prime * result + ((tag == null) ? 0 : tag.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
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
		if (!(obj instanceof TagCloudItem)) {
			return false;
		}
		TagCloudItem other = (TagCloudItem) obj;
		if (strength != other.strength) {
			return false;
		}
		if (tag == null) {
			if (other.tag != null) {
				return false;
			}
		} else if (!tag.equals(other.tag)) {
			return false;
		}
		if (url == null) {
			if (other.url != null) {
				return false;
			}
		} else if (!url.equals(other.url)) {
			return false;
		}
		return true;
	}
	
	
	
	
}
