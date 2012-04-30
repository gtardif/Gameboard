package gtardif.commons;

import static com.google.common.collect.Lists.newArrayList;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;

import com.google.common.collect.Lists;

public final class DeepEqualsBuilder extends EqualsBuilder {
	private DeepEqualsBuilder() {
		// private constructor
	}

	public static boolean reflectionDeepEquals(Object lhs, Object rhs) {
		if (lhs == rhs) {
			return true;
		}
		if ((lhs == null) || (rhs == null)) {
			return false;
		}
		Class<?> testClass = getTestClass(lhs, rhs);
		if (testClass == null) {
			return false;
		}
		DeepEqualsBuilder equalsBuilder = new DeepEqualsBuilder();
		try {
			reflectionAppend(lhs, rhs, testClass, equalsBuilder, null, Lists.newArrayList());
		} catch (IllegalAccessException e) {
			// this can't happen. Would get a Security exception instead
			// throw a runtime exception in case the impossible happens.
			throw new RuntimeException("Unexpected IllegalAccessException", e);
		} catch (IllegalArgumentException e) {
			return false;
		}
		return equalsBuilder.isEquals();
	}

	@Override
	public EqualsBuilder append(Object lhs, Object rhs) {
		if (!isEquals()) {
			return this;
		}
		if (lhs == rhs) {
			return this;
		}
		if ((lhs == null) || (rhs == null)) {
			this.setEquals(false);
			return this;
		}
		Class<?> lhsClass = lhs.getClass();
		if (!lhsClass.isArray()) {
			try {
				reflectionAppend(lhs, rhs, lhsClass, this, null, newArrayList(lhs, rhs));
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		} else if (lhs.getClass() != rhs.getClass()) {
			// Here when we compare different dimensions, for example: a
			// boolean[][] to a boolean[]
			this.setEquals(false);
		} else {
			appendArray(lhs, rhs);
		}
		return this;
	}

	private void appendArray(Object lhs, Object rhs) {
		if (lhs instanceof long[]) {
			append((long[]) lhs, (long[]) rhs);
		} else if (lhs instanceof int[]) {
			append((int[]) lhs, (int[]) rhs);
		} else if (lhs instanceof short[]) {
			append((short[]) lhs, (short[]) rhs);
		} else if (lhs instanceof char[]) {
			append((char[]) lhs, (char[]) rhs);
		} else if (lhs instanceof byte[]) {
			append((byte[]) lhs, (byte[]) rhs);
		} else if (lhs instanceof double[]) {
			append((double[]) lhs, (double[]) rhs);
		} else if (lhs instanceof float[]) {
			append((float[]) lhs, (float[]) rhs);
		} else if (lhs instanceof boolean[]) {
			append((boolean[]) lhs, (boolean[]) rhs);
		} else {
			// Not an array of primitives
			append((Object[]) lhs, (Object[]) rhs);
		}
	}

	private static void reflectionAppend(Object lhs, Object rhs, Class<?> aClass, EqualsBuilder builder, String[] excludeFields,
			List<Object> previouslyComparedObjects) throws IllegalArgumentException, IllegalAccessException {
		boolean useTransients = lhs instanceof Collection<?>;
		Class<?> clazz = aClass;

		while (clazz.getSuperclass() != null) {
			Field[] fields = clazz.getDeclaredFields();
			List<String> excludedFieldList = excludeFields != null ? Lists.newArrayList(excludeFields) : Lists.<String> newArrayList();
			AccessibleObject.setAccessible(fields, true);
			for (int i = 0; (i < fields.length) && builder.isEquals(); i++) {
				Field f = fields[i];
				if (shouldNotCompareThisField(f, excludedFieldList)) {
					continue;
				}
				if (isTransient(f) && !useTransients) {
					continue;
				}
				if (isStatic(f)) {
					continue;
				}

				Object lhsChild = f.get(lhs);
				if (hasAlreadyBeenCompared(lhsChild, previouslyComparedObjects)) {
					continue;
				}

				previouslyComparedObjects.add(lhsChild);

				Object rhsChild = f.get(rhs);
				Class<?> testClass = getTestClass(lhsChild, rhsChild);
				boolean hasEqualsMethod = classHasEqualsMethod(testClass);

				if ((testClass != null) && (testClass.getDeclaredFields().length > 0) && !hasEqualsMethod) {
					reflectionAppend(lhsChild, rhsChild, testClass, builder, excludeFields, previouslyComparedObjects);
				} else {
					builder.append(lhsChild, rhsChild);
				}
			}

			// now for the parent
			clazz = clazz.getSuperclass();
			reflectionAppend(lhs, rhs, clazz, builder, excludeFields, previouslyComparedObjects);
		}
	}

	private static boolean hasAlreadyBeenCompared(Object lhsChild, List<Object> previouslyComparedObjects) {
		if (lhsChild != null) {
			return previouslyComparedObjects.contains(lhsChild);
		}
		return false;
	}

	private static boolean isStatic(Field f) {
		return Modifier.isStatic(f.getModifiers());
	}

	private static boolean isTransient(Field f) {
		return Modifier.isTransient(f.getModifiers());
	}

	private static boolean shouldNotCompareThisField(Field f, List<String> excludedFieldList) {
		return excludedFieldList.contains(f.getName());
	}

	private static Class<?> getTestClass(Object lhs, Object rhs) {
		Class<?> testClass = null;
		if ((lhs != null) && (rhs != null)) {

			Class<?> lhsClass = lhs.getClass();
			Class<?> rhsClass = rhs.getClass();

			if (lhsClass.isInstance(rhs)) {
				testClass = lhsClass;
				if (!rhsClass.isInstance(lhs)) {
					// rhsClass is a subclass of lhsClass
					testClass = rhsClass;
				}
			} else if (rhsClass.isInstance(lhs)) {
				testClass = rhsClass;
				if (!lhsClass.isInstance(rhs)) {
					// lhsClass is a subclass of rhsClass
					testClass = lhsClass;
				}
			}
		}
		return testClass;
	}

	private static boolean classHasEqualsMethod(Class<?> clazz) {
		if (clazz != null) {
			Method[] methods = clazz.getDeclaredMethods();
			for (Method method : methods) {
				if ("equals".equals(method.getName()) && (method.getParameterTypes().length == 1)
						&& method.getParameterTypes()[0].equals(Object.class)) {
					return true;
				}
			}
		}
		return false;
	}
}