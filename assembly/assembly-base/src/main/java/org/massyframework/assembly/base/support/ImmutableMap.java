/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月11日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.base.support;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 具有不可变性的Map
 * 
 * @author huangkaihui
 * 
 *
 */
public final class ImmutableMap<K, V> extends AbstractMap<K, V> {

	final Entry<K, V>[] entries;

	/**
	 * 创建不可变性Map实例
	 * 
	 * @param entries
	 *            map记录集合
	 * @return {@link ImmutableMap}
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> ImmutableMap<K, V> newInstance(Entry<K, V>... entries) {
		return new ImmutableMap<K, V>(entries);
	}

	/**
	 * 创建不可变性Map实例
	 * 
	 * @param entries
	 *            {@link Map}
	 * @return {@link ImmutableMap}
	 */
	public static <K, V> ImmutableMap<K, V> newInstance(Map<K, V> entries) {
		if (entries == null){
			return new ImmutableMap<K, V>();
		}
		
		if (entries instanceof ImmutableMap) {
			return (ImmutableMap<K, V>) entries;
		} else {
			return new ImmutableMap<K, V>(entries);
		}
	}

	/**
	 * 构造方法
	 */
	@SuppressWarnings("unchecked")
	protected ImmutableMap(){
		this.entries = new Entry[0];
	}
	
	/**
	 * 构造方法
	 * 
	 * @param entries
	 */
	protected ImmutableMap(Entry<K, V>[] entries) {
		this.entries = entries.clone();
	}

	/**
	 * 
	 * @param map
	 */
	@SuppressWarnings("unchecked")
	protected ImmutableMap(Map<K, V> map) {
		this.entries = map.entrySet().toArray(new Entry[map.size()]);
	}

	/**
	 * 
	 */
	@Override
	public V get(Object key) {
		if (key == null) {
			for (int i = 0; i < entries.length; i++) {
				if (entries[i].getKey() == null) {
					return entries[i].getValue();
				}
			}
		} else {
			for (int i = 0; i < entries.length; i++) {
				if (key.equals(entries[i].getKey())) {
					return entries[i].getValue();
				}
			}
		}
		return null;
	}

	/**
	 * 
	 */
	@Override
	public Set<Entry<K, V>> entrySet() {
		return new EntrySet();
	}

	private class EntrySet extends AbstractSet<Entry<K, V>> {
		
		/**
		 * 
		 */
		@Override
		public Iterator<Entry<K, V>> iterator() {
			return new EntryItr(0);
		}

		/**
		 * 
		 */
		@Override
		public int size() {
			return entries.length;
		}
	}

	/**
	 * Iterator实体
	 */
	private class EntryItr implements Iterator<Entry<K, V>> {
		int cursor;

		private EntryItr(int cursor) {
			this.cursor = cursor;
		}

		public boolean hasNext() {
			return cursor != size();
		}

		public Entry<K, V> next() {
			return entries[cursor++];
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
