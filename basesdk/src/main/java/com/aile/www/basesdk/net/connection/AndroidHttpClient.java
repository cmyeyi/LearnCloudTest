/*
 * Baidu.com Inc.
 * Copyright (c) 2000-2014 All Rights Reserved.
 */
package com.aile.www.basesdk.net.connection;

import android.content.Context;

import com.aile.www.basesdk.net.NetConfigure;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public final class AndroidHttpClient implements HttpClient {

    public static final int SO_TIME_OUT = 1500 * 10;
    public static final int CONNECTION_TIME_OUT = 1500 * 10;
    // Gzip of data shorter than this probably won't be worthwhile
    public static long DEFAULT_SYNC_MIN_GZIP_BYTES = 256;

    private static final String TAG = "AndroidHttpClient";

    private boolean mIsLoadCookies = false;

    /**
     * Create a new HttpClient with reasonable defaults (which you can update).
     *
     * @param userAgent to report in your HTTP requests
     * @param context   to use for caching SSL sessions (may be null for no caching)
     * @return AndroidHttpClient for you to use for all your requests.
     */
    public static AndroidHttpClient newInstance(String userAgent, Context context) {

        try {
            KeyStore trustStore;
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());

            trustStore.load(null, null);

            SSLSocketFactory sf = new MySSLSocketFactory(trustStore, NetConfigure.getInstance().isHttpsDebugMode());
            sf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();

            // Turn off stale checking. Our connections break all the time anyway,
            // and it's not worth it to pay the penalty of checking every time.
            HttpConnectionParams.setStaleCheckingEnabled(params, false);

            // Default connection and socket timeout of 20 seconds. Tweak to taste.
            HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIME_OUT);
            HttpConnectionParams.setSoTimeout(params, SO_TIME_OUT);
            HttpConnectionParams.setSocketBufferSize(params, 8192);

            // Increase max total connection to 60
            ConnManagerParams.setMaxTotalConnections(params, 60);
            // Increase default max connection per route to 20
            ConnPerRouteBean connPerRoute = new ConnPerRouteBean(20);
            // Increase max connections for localhost:80 to 20
            HttpHost localhost = new HttpHost("locahost", 80);
            connPerRoute.setMaxForRoute(new HttpRoute(localhost), 20);
            ConnManagerParams.setMaxConnectionsPerRoute(params, connPerRoute);

            // Don't handle redirects -- return them to the caller. Our code
            // often wants to re-POST after a redirect, which we must do ourselves.
            HttpClientParams.setRedirecting(params, false);

            // Use a session cache for SSL sockets
            // SSLSessionCache sessionCache = context == null ? null : new
            // SSLSessionCache(context);
            // SSLCertificateSocketFactory.getDefault (30 * 1000);

            // Set the specified user agent and register standard protocols.
            HttpProtocolParams.setUserAgent(params, userAgent);
            SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            // schemeRegistry.register(new Scheme("https", SSLSocketFactory .getSocketFactory(), 443));
            schemeRegistry.register(new Scheme("https", sf, 443));
            ClientConnectionManager manager = new ThreadSafeClientConnManager(params, schemeRegistry);
            // We use a factory method to modify superclass initialization
            // parameters without the funny call-a-static-method dance.
            return new AndroidHttpClient(manager, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Create a new HttpClient with reasonable defaults (which you can update).
     *
     * @param userAgent to report in your HTTP requests.
     * @return AndroidHttpClient for you to use for all your requests.
     */
    public static AndroidHttpClient newInstance(String userAgent) {
        AndroidHttpClient client = newInstance(userAgent, null /* session cache */);
        return client;
    }

    private final DefaultHttpClient delegate;

    private RuntimeException mLeakedException = new IllegalStateException("AndroidHttpClient created and never closed");

    private AndroidHttpClient(ClientConnectionManager ccm, HttpParams params) {
        this.delegate = new DefaultHttpClient(ccm, params) {
            @Override
            protected HttpContext createHttpContext() {
                // Same as DefaultHttpClient.createHttpContext() minus the
                // cookie store.
                HttpContext context = new BasicHttpContext();
                context.setAttribute(ClientContext.AUTHSCHEME_REGISTRY, getAuthSchemes());
                context.setAttribute(ClientContext.COOKIESPEC_REGISTRY, getCookieSpecs());
                context.setAttribute(ClientContext.CREDS_PROVIDER, getCredentialsProvider());
                context.setAttribute(ClientContext.COOKIE_STORE, getCookieStore());
                return context;
            }
        };
    }

    /**
     * Use customize cookies
     *
     * @param cookies the cookies you want to maintain
     */
    public void loadCookies(CookieStore cookies) {
        this.mIsLoadCookies = true;
        this.delegate.setCookieStore(cookies);
    }

    /**
     * Indicate if the HTTP client loaded user's own cookies(such as login session)
     *
     * @return whether loaded the retained cookies
     */
    public boolean isLoadOwnCookies() {
        return this.mIsLoadCookies;
    }

    public void addRequestInterceptor(HttpRequestInterceptor interceptor) {
        if (interceptor == null) {
            return;
        }
        this.delegate.addRequestInterceptor(interceptor, this.delegate.getRequestInterceptorCount());
    }

    public void removeRequestInterceptor(HttpRequestInterceptor interceptor) {
        if (interceptor == null) {
            return;
        }
        this.delegate.removeRequestInterceptorByClass(interceptor.getClass());
    }

    public void addResponseInterceptor(HttpResponseInterceptor interceptor) {
        if (interceptor == null) {
            return;
        }
        this.delegate.addResponseInterceptor(interceptor, this.delegate.getResponseInterceptorCount());
    }

    public void removeResponseInterceptor(HttpResponseInterceptor interceptor) {
        if (interceptor == null) {
            return;
        }
        this.delegate.removeResponseInterceptorByClass(interceptor.getClass());
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (mLeakedException != null) {
            mLeakedException = null;
        }
    }

    /**
     * Modifies a request to indicate to the server that we would like a gzipped response. (Uses the "Accept-Encoding"
     * HTTP header.)
     *
     * @param request the request to modify
     * @see #getUngzippedContent
     */
    public static void modifyRequestToAcceptGzipResponse(HttpRequest request) {
        request.addHeader("Accept-Encoding", "gzip");
    }

    /**
     * Modifies a request to indicate to the server that we would like a kind of MIME type response.(Uses the
     * "Content-Type" HTTP header.)
     *
     * @param request     the request to modify
     * @param contentType MEME type
     */
    public static void modifyRequestContentType(HttpRequest request, String contentType) {
        request.addHeader("Content-Type", contentType);
    }

    /**
     * Gets the input stream from a response entity. If the entity is gzipped then this will get a stream over the
     * uncompressed data.
     *
     * @param entity the entity whose content should be read
     * @return the input stream to read from
     * @throws java.io.IOException
     */
    public static InputStream getUngzippedContent(HttpEntity entity) throws IOException {
        InputStream responseStream = entity.getContent();
        /*
         * if (responseStream == null) return responseStream; Header header = entity.getContentEncoding(); if (header ==
         * null) return responseStream; String contentEncoding = header.getValue(); if (contentEncoding == null) return
         * responseStream; if (contentEncoding.contains("gzip"))
         */
        responseStream = new GZIPInputStream(responseStream);
        return responseStream;
    }

    /**
     * Release resources associated with this client. You must call this, or significant resources (sockets and memory)
     * may be leaked.
     */
    public void close() {
        if (mLeakedException != null) {
            getConnectionManager().shutdown();
            mLeakedException = null;
        }
    }

    /**
     * Use cmwap gateway(10.0.0.172:80) as proxy for the normal internet connection
     */
    public void useProxyConnection(HttpHost proxy) {
        this.delegate.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
    }

    /**
     * Use default connection (except cmwap sisuation)
     */
    public void useDefaultConnection() {
        this.delegate.getParams().removeParameter(ConnRoutePNames.DEFAULT_PROXY);
    }

    public HttpParams getParams() {
        return delegate.getParams();
    }

    public ClientConnectionManager getConnectionManager() {
        return delegate.getConnectionManager();
    }

    public HttpResponse execute(HttpUriRequest request) throws IOException {
        return delegate.execute(request);
    }

    public HttpResponse execute(HttpUriRequest request, HttpContext context) throws IOException {
        return delegate.execute(request, context);
    }

    public HttpResponse execute(HttpHost target, HttpRequest request) throws IOException {
        return delegate.execute(target, request);
    }

    public HttpResponse execute(HttpHost target, HttpRequest request, HttpContext context) throws IOException {
        return delegate.execute(target, request, context);
    }

    public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler) throws IOException,
            ClientProtocolException {
        return delegate.execute(request, responseHandler);
    }

    public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context)
            throws IOException, ClientProtocolException {
        return delegate.execute(request, responseHandler, context);
    }

    public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler)
            throws IOException, ClientProtocolException {
        return delegate.execute(target, request, responseHandler);
    }

    public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler,
                         HttpContext context) throws IOException, ClientProtocolException {
        return delegate.execute(target, request, responseHandler, context);
    }

    public static AbstractHttpEntity getCompressedEntity(InputStream in) throws IOException {
        AbstractHttpEntity entity;
        byte[] buffer = new byte[4096];

        GZIPOutputStream zipper = null;
        ByteArrayOutputStream arr = null;

        try {
            int bytesRead = in.read(buffer);
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

    public static AbstractHttpEntity getCompressedEntity(byte[] data) throws IOException {
        AbstractHttpEntity entity;
        if (data.length < getMinGzipSize()) {
            entity = new ByteArrayEntity(data);
        } else {
            GZIPOutputStream zipper = null;
            ByteArrayOutputStream arr = null;

            try {
                arr = new ByteArrayOutputStream();
                zipper = new GZIPOutputStream(arr);
                zipper.write(data);
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
            }
        }
        return entity;
    }

    public static long getMinGzipSize() {
        return DEFAULT_SYNC_MIN_GZIP_BYTES; // For now, this is just a constant.
    }

    public static class MySSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore, boolean httpsDebugMode) throws NoSuchAlgorithmException, KeyManagementException,
                KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = null;
            if(!httpsDebugMode){
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                tmf.init((KeyStore) null);
                tm = tmf.getTrustManagers()[0];
            }else{
                tm = new X509TrustManager() {

                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                };
            }

            sslContext.init(null, new TrustManager[]{tm}, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException,
                UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }
}
