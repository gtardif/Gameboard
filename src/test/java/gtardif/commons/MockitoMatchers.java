package gtardif.commons;

import static org.mockito.Matchers.*;

import java.util.List;

import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.matchers.Not;
import org.mockito.internal.matchers.VarargMatcher;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public class MockitoMatchers {
	private MockitoMatchers() {
		// static class
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> eqIterable(final T... values) {
		return argThat(new ArgumentMatcher<List<T>>() {
			@Override
			public boolean matches(Object argument) {
				return Iterables.elementsEqual((Iterable<T>) argument, ImmutableList.copyOf(values));
			}
		});
	}

	public static <T> T deepRefEq(final T wanted) {
		return argThat(new ArgumentMatcher<T>() {
			@Override
			public boolean matches(Object argument) {
				return DeepEqualsBuilder.reflectionDeepEquals(argument, wanted);
			}
		});
	}

	@SuppressWarnings("unchecked")
	public static <T> T neq(T wanted) {
		return (T) argThat(new Not(new Equals(wanted)));
	}

	public static Object[] varArgOfLength(final int expectedLength) {
		return argThat(new VarArgsLengthMatcher(expectedLength));
	}

	@SuppressWarnings("serial")
	private static final class VarArgsLengthMatcher extends ArgumentMatcher<Object[]> implements VarargMatcher {
		private final int expectedLength;

		VarArgsLengthMatcher(int expectedLength) {
			this.expectedLength = expectedLength;
		}

		@Override
		public boolean matches(Object argument) {
			return ((Object[]) argument).length == expectedLength;
		}
	}
}
