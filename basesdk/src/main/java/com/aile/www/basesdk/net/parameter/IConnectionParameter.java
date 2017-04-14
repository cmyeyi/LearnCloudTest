/*
 * Baidu.com Inc.
 * Copyright (c) 2000-2014 All Rights Reserved.
 */
package com.aile.www.basesdk.net.parameter;

/**
 * 
 * @ClassName: IConnectionParameter
 * @Description: 网络连接所需要的参数
 *
 */
public interface IConnectionParameter {

	/**
	 * 
	 * 
	 * @description 将网络参数设置为默认值
	 */
	public void setDefaultValue();

	/**
	 * @return 网络参数是否有效
	 * @description 验证设置的网络参数是否有效
	 */
	public boolean isValidParameter();
}
