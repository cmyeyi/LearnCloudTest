package com.aile.www.basesdk;

public interface ILoginVerify {

	/**
	 * 登录回调
	 * @param isLogined true 登录，false 未登录
	 * @param user 登录时为用户信息
	 */
	void onLoginChanged(boolean isLogined, LoginInfo user);

	/**
	 * 在登录成功前，取消了登录操作
	 */
	void onLoginCancel();
}
