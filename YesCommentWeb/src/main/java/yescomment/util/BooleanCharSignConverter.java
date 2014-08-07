package yescomment.util;



import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("BooleanCharSignConverter")
public class BooleanCharSignConverter implements Converter {

	private static final char CHECKMARK_CHAR= 0x2713;
	private static final char CROSSMARK_CHAR= 0x2715;
	private static final char QUESTIONMARK_CHAR='?';
	private static final String CHECKMARK_STRING= String.valueOf(CHECKMARK_CHAR);
	private static final String CROSSMARK_STRING= String.valueOf(CROSSMARK_CHAR);
	private static final String QUESTIONMARK_STRING= String.valueOf(QUESTIONMARK_CHAR);
	@Override
	public Object getAsObject(FacesContext ctx, UIComponent uic, String value) {
		if (value.equals(QUESTIONMARK_STRING))  {
			return null;
		}
		else if (value.equals(CHECKMARK_STRING))  {
			return Boolean.TRUE;
		}
		if (value.equals(CROSSMARK_STRING))  {
			return Boolean.FALSE;
		}
		else {
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext ctx, UIComponent uic, Object value) {
		Boolean valueAsBoolean=(Boolean) value;
		if (valueAsBoolean==null) {
			return QUESTIONMARK_STRING;
		}
		else if (valueAsBoolean.equals(Boolean.TRUE)) {
			return CHECKMARK_STRING;
		}
		else if (valueAsBoolean.equals(Boolean.FALSE)) {
			return CROSSMARK_STRING;
		}
		else {
			return null;
		}
		
	}

}
