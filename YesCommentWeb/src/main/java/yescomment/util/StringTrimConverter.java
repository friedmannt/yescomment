package yescomment.util;



import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("StringTrimConverter")
public class StringTrimConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext ctx, UIComponent uic, String value) {
		return value == null ? null : value.trim();
	}

	@Override
	public String getAsString(FacesContext ctx, UIComponent uic, Object value) {
		return value==null?null:value.toString();
	}

}
