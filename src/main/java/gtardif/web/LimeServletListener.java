package gtardif.web;

import gtardif.p4.GameRepository;
import gtardif.sample.MyController;

import org.zdevra.guice.mvc.MvcModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class LimeServletListener extends GuiceServletContextListener {
	private final GameRepository gameRepository;

	public LimeServletListener(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new MyModule());
	}

	class MyModule extends MvcModule {
		@Override
		protected void configureControllers() {
			bind(GameRepository.class).toInstance(gameRepository);
			control("/hello/*").withController(MyController.class);
			control("/game/*").withController(GameController.class);
		}
	}
}
