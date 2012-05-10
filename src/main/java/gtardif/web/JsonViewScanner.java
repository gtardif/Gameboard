package gtardif.web;

import java.lang.annotation.Annotation;

import org.zdevra.guice.mvc.Utils;
import org.zdevra.guice.mvc.ViewPoint;
import org.zdevra.guice.mvc.ViewScanner;

public class JsonViewScanner implements ViewScanner {
	@Override
	public ViewPoint scan(Annotation[] annotations) {
		JsonView anot = Utils.getAnnotation(JsonView.class, annotations);
		if (anot != null) {
			return new JsonViewPoint();
		}
		return ViewPoint.NULL_VIEW;
	}
}
