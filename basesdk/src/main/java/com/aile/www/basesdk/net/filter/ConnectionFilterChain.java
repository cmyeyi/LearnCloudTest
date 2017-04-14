/*
 * Baidu.com Inc.
 * Copyright (c) 2000-2014 All Rights Reserved.
 */
package com.aile.www.basesdk.net.filter;

import java.util.LinkedList;

/**
 * 
 * @ClassName: ConnectionFilterChain
 * @Description: 管理一个过滤链，用于网络数据的转换
 *
 */
public class ConnectionFilterChain {

	private LinkedList<IConnectionFilter> filterChain = new LinkedList<IConnectionFilter>();
	private int index;

	/**
	 * 
	 * @param filter
	 * @description 在过滤链最后增加一个filter
	 */
	public void addFilter(IConnectionFilter filter) {
		filterChain.add(filter);
	}

	/**
	 * 
	 * @param index
	 * @param filter
	 * @description 在指定位置插入filter，如果指定位置非法(<0 或 >链长度)，则将该filter加入链尾
	 */
	public void insertFilter(int index, IConnectionFilter filter) {
		if (index < 0 || index > filterChain.size()) {
			filterChain.add(filter);
		} else {
			filterChain.add(index, filter);
		}
	}

	/**
	 * 
	 * @param index
	 * @param filter
	 * @description 替换掉指定位置的filter，如果指定位置非法(<0 或 >链长度)，则将该filter加入链尾
	 */
	public void replaceFilter(int index, IConnectionFilter filter) {
		if (index < 0 || index > filterChain.size()) {
			filterChain.add(filter);
		} else {
			filterChain.set(index, filter);
		}
	}

	/**
	 * 
	 * @param index
	 * @description 删除指定位置的filter
	 */
	public void removeFilter(int index) {
		filterChain.remove(index);
	}

	/**
	 * 
	 * @param filter
	 * @description 删除指定的filter
	 */
	public void removeFilter(IConnectionFilter filter) {
		filterChain.remove(filter);
	}

	/**
	 * 
	 * @description 将过滤链的游标置为0
	 */
	public void init() {
		index = 0;
	}

	/**
	 * 
	 * @return
	 * @description 判断该过滤链还有没有后续过滤器
	 */
	public boolean hasNext() {
		return index < filterChain.size();
	}

	/**
	 * 
	 * @return
	 * @description 获取过滤链后续过滤器
	 */
	public IConnectionFilter next() {
		if (index < filterChain.size()) {
			final IConnectionFilter filter = filterChain.get(index);
			index++;
			return filter;
		} else {
			return null;
		}
	}
}
