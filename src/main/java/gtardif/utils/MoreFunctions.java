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

import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

public class MoreFunctions {
	private MoreFunctions() {
		// static class
	}

	public static final Function<String, Integer> TO_INTEGER = new Function<String, Integer>() {
		@Override
		public Integer apply(String input) {
			return Integer.parseInt(input);
		}
	};

	public static final Function<Object, Class<?>> TO_CLASS = new Function<Object, Class<?>>() {
		@Override
		public Class<?> apply(Object input) {
			return input.getClass();
		}
	};

	public static <T> Function<T, List<T>> toList() {
		return new Function<T, List<T>>() {
			@Override
			public List<T> apply(T value) {
				return ImmutableList.of(value);
			}
		};
	}

	@SuppressWarnings("unused")
	public static <S, T> Function<S, T> cast(Class<T> clazz) {
		return new Function<S, T>() {
			@Override
			@SuppressWarnings("unchecked")
			public T apply(S value) {
				return (T) value;
			}
		};
	}

	public static <K, V> Function<K, V> memoize(final Function<K, V> keyToValue) {
		return new Function<K, V>() {
			private final Map<K, V> cache = Maps.newHashMap();

			@Override
			public V apply(K key) {
				V value = cache.get(key);
				if (null == value) {
					value = keyToValue.apply(key);
					cache.put(key, value);
				}
				return value;
			}
		};
	}
}
