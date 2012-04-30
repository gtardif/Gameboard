package gtardif.commons;

import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.commons.lang.mutable.MutableInt;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;

class MockInjector {
	private static final String STUB_PREFIX = "stub";
	private static final String MOCK_PREFIX = "mock";

	Object injectMocks(final Object test) {
		final MutableInt mockCount = new MutableInt(0);

		try {
			Field[] fields = test.getClass().getDeclaredFields();
			for (Field field : fields) {
				if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) {
					if (field.getName().startsWith(MOCK_PREFIX) || field.getName().startsWith(STUB_PREFIX)) {
						mockCount.increment();
					}
					continue; // Ignore field
				}

				if (field.getName().startsWith(MOCK_PREFIX)) {
					Object mock = mock(field);
					setFieldValue(test, field, mock);
					mockCount.increment();
				} else if (field.getName().startsWith(STUB_PREFIX)) {
					Object mock = Mockito.mock(field.getType(), withSettings().name(field.getName()).defaultAnswer(RETURNS_DEEP_STUBS));
					setFieldValue(test, field, mock);
					mockCount.increment();
				} else if (field.getType().isAssignableFrom(ArgumentCaptor.class)) {
					ArgumentCaptor<?> captor = ArgumentCaptor.forClass(field.getType());
					setFieldValue(test, field, captor);
					mockCount.increment();
				}
			}
		} catch (MockitoException e) {
			throw new RuntimeException("Unable to initialize mocks in class: " + test.getClass().getSimpleName(), e);
		}

		if (0 == mockCount.intValue()) {
			throw new RuntimeException(test.getClass().getSimpleName() + " test should not use " + MockInjector.class.getSimpleName()
					+ " runner as it doesn't declare any mock");
		}

		return test;
	}

	protected static Object mock(Field field) {
		return Mockito.mock(field.getType(), withSettings().name(field.getName()).defaultAnswer(RETURNS_DEFAULTS));
	}

	protected void setFieldValue(Object test, Field field, Object value) {
		try {
			field.setAccessible(true);
			field.set(test, value);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Unable to access a field", e);
		}
	}
}