package gtardif;

import org.zdevra.guice.mvc.MvcModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class LimeServletListener extends GuiceServletContextListener {
	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new MyModule());
	}

	static class MyModule extends MvcModule {
		@Override
		protected void configureControllers() {
			control("/hello/*").withController(MyController.class);
		}
	}
}
