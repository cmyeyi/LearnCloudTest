/*
 * Baidu.com Inc.
 * Copyright (c) 2000-2014 All Rights Reserved.
 */
package com.aile.www.basesdk.net.parameter;

import android.text.TextUtils;

import com.aile.www.basesdk.net.file.FileContentTypeHelper;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPOutputStream;

/**
 * 
 * @ClassName: HttpConnectionParameter
 * @Description: 默认http连接的参数
 *
 */
public class HttpConnectionParameter implements IConnectionParameter {

	public static final String METHOD_GET = "get";
	public static final String METHOD_POST = "post";

	public static final String METHOD_PUT = "put";
	public static final String METHOD_DELETE = "delete";

	public String uri;
	public String method;
	public Map<String, String> headParameter = null;
	public HttpEntity entity;

	public Map<String,String> parameters = null;
	
	@Override
	public void setDefaultValue() {
		method = METHOD_GET;
	}

	@Override
	public boolean isValidParameter() {
		if (!METHOD_GET.equalsIgnoreCase(method)
				&& !METHOD_POST.equalsIgnoreCase(method)
				&& !METHOD_PUT.equalsIgnoreCase(method)
				&& !METHOD_DELETE.equalsIgnoreCase(method)) {
			return false;
		}

		if (TextUtils.isEmpty(uri)) {
			return false;
		}
		return true;
	}

	public void setString(String text) {
		try {
			entity = new StringEntity(text, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void setData(Map map) {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		if(map != null ) {
			Set set = map.entrySet();
			if(set != null) {
				Iterator i = set.iterator();
				while (i.hasNext()) {
					Map.Entry entry = (Map.Entry) i.next();
					list.add(new BasicNameValuePair(entry.getKey().toString(), entry
							.getValue().toString()));
				}

				try {
					entity = new UrlEncodedFormEntity(list, "utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public void setMultipartData(Map<String, Object> map) {
		if(map != null) {
			Set<String> set = map.keySet();
			if(set != null) {
				Iterator<String> i = set.iterator();

				MultipartEntity builder = new MultipartEntity();


				while (i.hasNext()) {
					String key = i.next();
					Object value = map.get(key);
					if (value instanceof File) {
						File file = (File) value;

						FileBody bin = new FileBody(file,
								FileContentTypeHelper.getContentType(file.getName()));
						builder.addPart(key, bin);
					} else {
						try {
							builder.addPart(key, new StringBody(value.toString(), Charset.forName("UTF-8")));
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				entity = builder;// .build();
			}
		}


	}

	public void setData(byte[] data, boolean isGzip) {

		ByteArrayEntity formEntity = new ByteArrayEntity(data);
		if (isGzip) {
			try {
				entity = getCompressedEntity(formEntity.getContent());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			entity = formEntity;
		}
	}

	private AbstractHttpEntity getCompressedEntity(InputStream in)
			throws IOException {
		AbstractHttpEntity entity;
		byte[] buffer = new byte[4096];
		int bytesRead = in.read(buffer);

		GZIPOutputStream zipper = null;
		ByteArrayOutputStream arr = null;

		try {
			arr = new ByteArrayOutputStream();
			zipper = new GZIPOutputStream(arr);

			do {
				zipper.write(buffer, 0, bytesRead);
			} while ((bytesRead = in.read(buffer)) != -1);

			zipper.finish();
			entity = new ByteArrayEntity(arr.toByteArray());
			entity.setContentEncoding("gzip");
		} finally {
			if (zipper != null) {
				zipper.close();
			}
			if (arr != null) {
				arr.close();
			}
			if (in != null) {
				in.close();
			}
		}
		return entity;
	}
}
