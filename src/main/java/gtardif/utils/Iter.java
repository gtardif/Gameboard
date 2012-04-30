/*
 * 
 *  * ========================================================= *
 *  *************************************************************
 *  *      _______. _______  _______ .___________.____    ____  *
 *  *     /       ||   ____||   ____||           |\   \  /   /  *
 *  *    |   (----`|  |__   |  |__   `---|  |----` \   \/   /   *
 *  *     \   \    |   __|  |   __|      |  |       \_    _/    *
 *  * .----)   |   |  |____ |  |____     |  |         |  |      *
 *  * |_______/    |_______||_______|    |__|         |__|      *
 *  *                                                           *
 *  *************************************************************
 *  * ========================================================= *
 *           _____  _   _      _   
 *     /\   |  __ \| \ | |    | |  
 *    /  \  | |  | |  \| | ___| |_ 
 *   / /\ \ | |  | | . ` |/ _ \ __|
 *  / ____ \| |__| | |\  |  __/ |_ 
 * /_/    \_\_____/|_| \_|\___|\__|
 * 
 * 
 * 
 * NOTICE:
 * 
 * ##################################################################################
 * #                                                                                #
 * # This file is part of the Seety project.                                        #
 * # All information contained herein is, remains the property of PagesJaunes Group #
 * # The intellectual and technical concepts contained herein are proprietary to    #
 * # PagesJaunes Group and may be covered by Patents, patents in process, and are   #
 * # protected by trade secret or copyright law.                                    #
 * #                                                                                #
 * # Dissemination of this information or reproduction of this material is strictly #
 * # forbidden unless prior written permission is obtained from PagesJaunes Group.  #
 * #                                                                                #
 * # All Right Reserved                                                             #
 * # Copyright (c) 2011 , PagesJaunes Group                                         #
 * #                                                                                #
 * ##################################################################################
 */
package gtardif.utils;

import static com.google.common.base.Predicates.*;
import static com.google.common.collect.Iterables.*;
import static com.google.common.collect.Lists.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

public class Iter<T> implements Iterable<T> {
	private final Iterable<T> values;

	Iter(Iterable<T> values) {
		this.values = values;
	}

	public static <T> Iter<T> with(Iterable<T> values) {
		if (values instanceof Iter) {
			return (Iter<T>) values;
		}
		return new Iter<T>(values);
	}

	public static <T> Iter<T> with(T... values) {
		return with(Arrays.asList(values));
	}

	public Iter<T> only(Predicate<? super T> filter) {
		if (filter == Predicates.alwaysTrue()) {
			return this;
		}
		return with(filter(values, filter));
	}

	public <S extends T> Iter<S> only(Class<S> subClass) {
		return only(Predicates.instanceOf(subClass)).map(MoreFunctions.cast(subClass));
	}

	public <P> Iter<T> only(Function<? super T, P> equalTo, P valueToCompareWith) {
		return only(whereEquals(equalTo, valueToCompareWith));
	}

	@SuppressWarnings("unchecked")
	public <P> Iter<T> only(Function<? super T, P> equalTo, P... valuesToCompareWith) {
		List<Predicate<? super T>> allWhereEquals = newArrayList();

		for (P value : valuesToCompareWith) {
			allWhereEquals.add(whereEquals(equalTo, value));
		}

		return only(or(allWhereEquals.toArray(new Predicate[allWhereEquals.size()])));
	}

	public <P> Iter<T> only(Function<? super T, P> transform, Predicate<? super P> filter) {
		return only(compose(filter, transform));
	}

	public T first(Predicate<? super T> predicate) {
		return Iterables.find(values, predicate);
	}

	public T nth(Predicate<? super T> predicate, int n) {
		return with(Iterables.filter(values, predicate)).list().get(n - 1);
	}

	public T firstOrDefault(T defaultValue) {
		try {
			return first();
		} catch (NoSuchElementException e) {
			return defaultValue;
		}
	}

	public T firstOrDefault(Predicate<? super T> predicate, T defaultValue) {
		try {
			return first(predicate);
		} catch (NoSuchElementException e) {
			return defaultValue;
		}
	}

	public <P> T firstOrDefault(Function<? super T, P> equalTo, P value, T defaultValue) {
		try {
			return first(equalTo, value);
		} catch (NoSuchElementException e) {
			return defaultValue;
		}
	}

	public <P> T first(Function<? super T, P> equalTo, P value) {
		return first(whereEquals(equalTo, value));
	}

	public T first() {
		try {
			return Iterables.get(values, 0);
		} catch (IndexOutOfBoundsException e) {
			throw new NoSuchElementException();
		}
	}

	public boolean contains(Predicate<? super T> predicate) {
		return Iterables.any(values, predicate);
	}

	public <P> boolean contains(Function<? super T, P> equalTo, P value) {
		return contains(whereEquals(equalTo, value));
	}

	public <S extends T> Iter<T> exclude(Class<S> subClass) {
		return only(not(Predicates.instanceOf(subClass)));
	}

	public Iter<T> exclude(Predicate<? super T> filter) {
		return only(not(filter));
	}

	public <P> Iter<T> exclude(Function<? super T, P> transform, Predicate<? super P> filter) {
		return only(not(compose(filter, transform)));
	}

	public Iter<T> exclude(T... excludeValues) {
		return only(not(in(Lists.newArrayList(excludeValues))));
	}

	public Iter<T> exclude(Collection<T> excludeValues) {
		return only(not(in(excludeValues)));
	}

	public Iter<T> excludeDuplicates() {
		return only(new Predicate<T>() {
			private final Set<T> alreadyInList = Sets.newHashSet();

			@Override
			public boolean apply(T input) {
				return alreadyInList.add(input);
			}
		});
	}

	public int count(Predicate<? super T> filter) {
		return only(filter).size();
	}

	public <P> int count(Function<? super T, P> equalTo, P value) {
		return only(whereEquals(equalTo, value)).size();
	}

	public Iter<T> subList(int firstIndex, int lastIndex) {
		try {
			return with(list().subList(firstIndex, lastIndex));
		} catch (IllegalArgumentException e) {
			return with(Lists.<T> newArrayList());
		} catch (IndexOutOfBoundsException e) {
			return with(Lists.<T> newArrayList());
		}
	}

	public Iter<T> subListExcluding(T firstObject, T lastObject) {
		List<T> list = list();
		return subList(list.indexOf(firstObject) + 1, list.indexOf(lastObject));
	}

	public Iter<T> subList(T firstIncludedObject, T lastIncludedObject) {
		List<T> list = list();
		return subList(list.indexOf(firstIncludedObject), list.indexOf(lastIncludedObject) + 1);
	}

	public Iter<T> sortOn(Ordering<? super T> ordering) {
		return with(ordering.sortedCopy(values));
	}

	public Iter<T> sortOn(Comparator<? super T> comparator) {
		return sortOn(Ordering.from(comparator));
	}

	public Iter<T> sortOn(Function<? super T, ? extends Comparable<?>> transform) {
		return sortOn(Ordering.natural().onResultOf(transform));
	}

	public Iter<T> notNulls() {
		return only(Predicates.<T> notNull());
	}

	public <R> Iter<R> map(Function<? super T, R> transform) {
		return with(transform(values, transform));
	}

	public Iter<Double> to(final ToDouble<? super T> toDouble) {
		return with(transform(values, new Function<T, Double>() {
			@Override
			public Double apply(T value) {
				return toDouble.convert(value);
			}
		}));
	}

	public Iter<Integer> to(final ToInt<? super T> toInt) {
		return with(transform(values, new Function<T, Integer>() {
			@Override
			public Integer apply(T value) {
				return toInt.convert(value);
			}
		}));
	}

	public Iter<Long> to(final ToLong<? super T> toLong) {
		return with(transform(values, new Function<T, Long>() {
			@Override
			public Long apply(T value) {
				return toLong.convert(value);
			}
		}));
	}

	public Iter<String> to(final ToString<? super T> toString) {
		return with(transform(values, new Function<T, String>() {
			@Override
			public String apply(T value) {
				return toString.apply(value);
			}
		}));
	}

	public <R, C extends Iterable<R>> Iter<R> flatMap(Function<? super T, C> transform) {
		return with(Iterables.concat(map(transform)));
	}

	public <R> NavigableSet<R> toNavigableSet(Function<? super T, R> transform, Comparator<? super R> ordering) {
		NavigableSet<R> set = new TreeSet<R>(ordering);
		Iterables.addAll(set, map(transform));
		return set;
	}

	public <R extends Comparable<R>> NavigableSet<R> toNavigableSet(Function<? super T, R> transform) {
		return toNavigableSet(transform, null);
	}

	public NavigableSet<T> toNavigableSet() {
		NavigableSet<T> set = new TreeSet<T>();
		Iterables.addAll(set, this);
		return set;
	}

	public NavigableSet<T> toNavigableSet(Comparator<? super T> c) {
		NavigableSet<T> set = new TreeSet<T>(c);
		Iterables.addAll(set, this);
		return set;
	}

	public static <T extends Comparable<T>> NavigableSet<T> newNavigableSet(T... elements) {
		return new TreeSet<T>(Sets.newHashSet(elements));
	}

	public static <T> NavigableSet<T> newNavigableSet(Comparator<? super T> comparator, T... elements) {
		NavigableSet<T> treeSet = new TreeSet<T>(comparator);
		treeSet.addAll(Sets.newHashSet(elements));
		return treeSet;
	}

	public T max(Ordering<? super T> ordering) {
		return ordering.max(values);
	}

	public T min(Ordering<? super T> ordering) {
		return ordering.min(values);
	}

	public <V extends Comparable<V>> T maxOnResultOf(Function<? super T, V> function) {
		return Ordering.natural().onResultOf(function).max(values);
	}

	public <V extends Comparable<V>> T minOnResultOf(Function<? super T, V> function) {
		return Ordering.natural().onResultOf(function).min(values);
	}

	public List<T> list() {
		return newArrayList(values);
	}

	public ImmutableList<T> immutable() {
		return ImmutableList.copyOf(values);
	}

	public String join(String separator) {
		return Joiner.on(separator).join(values);
	}

	public T[] toArray(Class<T> type) {
		return Iterables.toArray(values, type);
	}

	public Set<T> toSet() {
		return Sets.newHashSet(values);
	}

	public <R> Set<R> toSet(Function<? super T, R> transform) {
		return Sets.newHashSet(map(transform));
	}

	public ImmutableSet<T> toImmutableSet() {
		return ImmutableSet.copyOf(values);
	}

	public T last() {
		return Iterables.getLast(values);
	}

	public Iter<T> last(int n) {
		int size = size();
		return subList(Math.max(0, size - n), size);
	}

	public int size() {
		return Iterables.size(values);
	}

	public double sum(ToDouble<? super T> toDouble) {
		double sum = 0L;
		for (T value : values) {
			sum += toDouble.convert(value);
		}
		return sum;
	}

	public int sum(ToInt<? super T> toInt) {
		int sum = 0;
		for (T value : values) {
			sum += toInt.convert(value);
		}
		return sum;

	}

	public long sum(ToLong<? super T> toLong) {
		long sum = 0;
		for (T value : values) {
			sum += toLong.convert(value);
		}
		return sum;
	}

	public double average(ToDouble<? super T> toDouble) {
		long size = 0;
		double sum = 0L;
		for (T value : values) {
			sum += toDouble.convert(value);
			size++;
		}
		return (0 == size) ? 0.0 : sum / size;
	}

	public double average(ToInt<? super T> toInt) {
		long size = 0;
		double sum = 0L;
		for (T value : values) {
			sum += toInt.convert(value);
			size++;
		}
		return (0 == size) ? 0.0 : sum / size;
	}

	public double average(ToLong<? super T> toLong) {
		long size = 0;
		double sum = 0L;
		for (T value : values) {
			sum += toLong.convert(value);
			size++;
		}
		return (0 == size) ? 0.0 : sum / size;
	}

	public <K> Map<K, T> indexBy(Function<? super T, ? extends K> toKey) {
		Map<K, T> map = Maps.newHashMap();

		for (T value : values) {
			map.put(toKey.apply(value), value);
		}

		return map;
	}

	@Override
	public Iterator<T> iterator() {
		return values.iterator();
	}

	public void forEach(Action<? super T> action) {
		for (T element : values) {
			action.execute(element);
		}
	}

	private <P> Predicate<? super T> whereEquals(Function<? super T, P> equalTo, P value) {
		return compose(equalTo(value), equalTo);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o instanceof Iterable<?>) {
			return Iterables.elementsEqual(this, (Iterable<?>) o);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return values.hashCode();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public boolean isEmpty() {
		return size() == 0;
	}
}
