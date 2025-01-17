/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.internal.util.collections;

import java.util.Map;

/**
 * For efficient lookup based on Class types as key,
 * a ClassValue should be used; however it requires
 * lazy association of values; this helper wraps
 * a plain HashMap but optimises lookups via the ClassValue.
 * N.B. there is a cost in memory and in terms of weak references,
 * so let's use this only where proven that a simple Map lookup
 * is otherwise too costly.
 * @param <V> the type of the values stored in the Maps.
 * @author Sanne Grinovero
 * @since 6.2
 */
public final class MapBackedClassValue<V> {

	private volatile Map<Class<?>, V> map;

	private final ClassValue<V> classValue = new ClassValue<>() {
		@Override
		protected V computeValue(final Class<?> type) {
			final Map<Class<?>, V> m = map;
			if ( m == null ) {
				throw new IllegalStateException( "This MapBackedClassValue has been disposed" );
			}
			else {
				return map.get( type );
			}
		}
	};

	public MapBackedClassValue(final Map<Class<?>, V> map) {
		//Defensive copy, and implicit null check.
		//Choose the Map.copyOf implementation as it has a compact layout;
		//it doesn't have great get() performance but it's acceptable since we're performing that at most
		//once per key before caching it via the ClassValue.
		this.map = Map.copyOf( map );
	}

	public V get(Class<?> key) {
		return classValue.get( key );
	}

	/**
	 * Use this to wipe the backing map, but N.B.
	 * we won't be clearing the ClassValue: this is useful
	 * only to avoid classloader leaks since the Map
	 * may hold references to user classes.
	 * Since ClassValue is also possibly caching state,
	 * it might be possible to retrieve some values after this
	 * but shouldn't be relied on.
	 * ClassValue doesn't leak references to classes.
	 */
	public void dispose() {
		this.map = null;
	}

}
