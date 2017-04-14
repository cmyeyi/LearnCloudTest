package com.aile.www.basesdk.net.filter;

import com.aile.www.basesdk.UnProguardable;

public abstract class BaseResponseFilter implements UnProguardable {

	public abstract String filterJsonString(String str);

}
