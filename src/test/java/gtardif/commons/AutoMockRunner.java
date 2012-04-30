package gtardif.commons;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class AutoMockRunner extends BlockJUnit4ClassRunner {
	public AutoMockRunner(Class<?> clazz) throws InitializationError {
		super(clazz);
	}

	@Override
	protected Object createTest() throws Exception {
		return new MockInjector().injectMocks(super.createTest());
	}
}