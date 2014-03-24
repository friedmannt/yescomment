package yescomment.managedbean.captcha;

import java.io.Serializable;

public class CaptchaOption implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static enum Color {
		 AQUA, BLACK, BLUE, FUCHSIA, GRAY, GREEN, LIME, MAROON, NAVY, OLIVE, ORANGE, PURPLE, RED, SILVER, TEAL,  YELLOW;
	}

	private Color color;
	private Integer number;

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + ((number == null) ? 0 : number.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CaptchaOption other = (CaptchaOption) obj;
		if (color != other.color)
			return false;
		if (number == null) {
			if (other.number != null)
				return false;
		} else if (!number.equals(other.number))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CaptchaOption [color=" + color + ", number=" + number + "]";
	}

	public CaptchaOption(Color color, Integer number) {
		super();
		this.color = color;
		this.number = number;
	}

	
	
	
}
