package com.common.network;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import com.common.utils.DeviceUtil;
import com.common.utils.EnCryptionUtils;
import com.common.utils.LogUtil;
import com.common.utils.VersionInfoUtils;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import okio.Buffer;

/**
 * Created by jacktian on 15/8/19.
 */
public class HttpUtil {

	public static final String USER_AGENT = "WJK Android store v2.0";
	public static final String TAG = HttpUtil.class.getSimpleName();

	static OkHttpClient client;

	/**
	 * get方式
	 *
	 * @param url
	 * @return
	 */
	public static String httpGet(Context context, String url) {
		IdentityHashMap<String, String> params = new IdentityHashMap<>();
		if (url != null && url.contains("?")) {
			Uri uri = Uri.parse(url);
			String uriScheme = uri.getScheme();
			String uriAuthority = uri.getAuthority();
			String uriPath = uri.getPath();
			for (String key : uri.getQueryParameterNames()) {
				params.put(key, uri.getQueryParameter(key));
				if (LogUtil.isDebug) Log.e(TAG, String.format("%s=%s", key, params.get(key)));
			}
			params = securityCheckParams(context, params, uriPath);
			url = uriScheme + "://" + uriAuthority + Config.INTERFACE_PATH
					+ "?" + String.format("%s=%s", Config.INTERFACE_NAME, params.get(Config.INTERFACE_NAME));
			if (LogUtil.isDebug) Log.e(TAG, "url = " + url);

			Request request = new Request.Builder()
					.addHeader("User-Agent", USER_AGENT)
					.url(url)
					.build();
			Response response;
			try {
				response = getCustomClient().newCall(request).execute();
				if (response.isSuccessful()) {
					return response.body().string();
				}
			} catch (IOException e) {
				LogUtil.e("HttpUtil", e.toString());
			}
		}
		return "";
	}

	/**
	 * get方式
	 *
	 * @param url
	 * @return
	 */
	public static String httpGet(String url, String lastModified, Context context) {

		IdentityHashMap<String, String> params = new IdentityHashMap<>();
		if (url != null && url.contains("?")) {
			Uri uri = Uri.parse(url);
			String uriScheme = uri.getScheme();
			String uriAuthority = uri.getAuthority();
			String uriPath = uri.getPath();
			for (String key : uri.getQueryParameterNames()) {

				params.put(key, uri.getQueryParameter(key));
			}
			params = securityCheckParams(context, params, uriPath);
			url = uriScheme + "://" + uriAuthority + Config.INTERFACE_PATH
					+ "?" + String.format("%s=%s", Config.INTERFACE_NAME, params.get(Config.INTERFACE_NAME));
			Request request = new Request.Builder()
					.addHeader("User-Agent", USER_AGENT)
					.addHeader("If-Modified-Since", lastModified)
					.url(url)
					.build();

			Response response;
			try {
				response = getCustomClient().newCall(request).execute();
				if (response.isSuccessful()) {
					LastModified.saveLastModified(context, url.hashCode() + "", response.header("Last-Modified"));
					return response.body().string();
				}
			} catch (IOException e) {
				LogUtil.e("HttpUtil", e.toString());
			}
		}
		return "";
	}

	/**
	 * post方式
	 *
	 * @param url
	 * @param postParameters
	 * @return
	 */
	public static String httpPost(Context context, String url, IdentityHashMap<String, String> postParameters) {
		try {
			FormEncodingBuilder builder = new FormEncodingBuilder();
			if (postParameters == null) {
				postParameters = new IdentityHashMap<>();
			}
			if (url != null) {
				Uri uri = Uri.parse(url);
				String uriScheme = uri.getScheme();
				String uriAuthority = uri.getAuthority();
				String uriPath = uri.getPath();
				for (String key : uri.getQueryParameterNames()) {

					postParameters.put(key, uri.getQueryParameter(key));
				}
				postParameters = securityCheckParams(context, postParameters, uriPath);
				url = uriScheme + "://" + uriAuthority + Config.INTERFACE_PATH;
				if (LogUtil.isDebug) Log.e(TAG, url);
				Set<String> set = postParameters.keySet();
				for (String key : set) {
					builder.add(key, postParameters.get(key));
				}
				RequestBody formBody = builder.build();
				Request request = new Request.Builder()
						.addHeader("User-Agent", USER_AGENT)
						.url(url)
						.post(formBody)
						.build();

				Response response = getCustomClient().newCall(request).execute();
				if (response.isSuccessful()) {
					return response.body().string();
				}
			}
		} catch (Exception e) {
			LogUtil.e("HttpUtil", e.toString());
		} finally {
		}
		return "";
	}

	/**
	 * post方式传输文件
	 *
	 * @param context
	 * @param url
	 * @param postParameters
	 * @param file
	 * @return
	 */
	public static String httpPost(Context context, String url, IdentityHashMap<String, String> postParameters, String streamName, File file) {
		try {
			if (file.exists() && file.isFile() && url != null) {
				FileNameMap fileNameMap = URLConnection.getFileNameMap();
				String type = fileNameMap.getContentTypeFor(file.getAbsolutePath());
				Uri uri = Uri.parse(url);
				String uriScheme = uri.getScheme();
				String uriAuthority = uri.getAuthority();
				String uriPath = uri.getPath();
				postParameters = securityCheckParams(context, postParameters);
				url = uriScheme + "://" + uriAuthority + Config.INTERFACE_PATH + uriPath;
				Set<String> set = postParameters.keySet();
				MultipartBuilder multipartBuilder = new MultipartBuilder().type(MultipartBuilder.FORM);
				for (String key : set) {
					multipartBuilder.addFormDataPart(key, postParameters.get(key));
				}
				multipartBuilder.addFormDataPart(streamName, file.getName(), RequestBody.create(MediaType.parse(type), file));
				RequestBody requestBody = multipartBuilder.build();
				Request request = new Request.Builder()
						.addHeader("User-Agent", USER_AGENT)
						.url(url)
						.post(requestBody)
						.build();

				Response response = getCustomClient().newCall(request).execute();
				if (response.isSuccessful()) {
					return response.body().string();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * put方式
	 *
	 * @param url
	 * @param postParameters
	 * @return
	 */
	public static String httpPut(Context context, String url, IdentityHashMap<String, String> postParameters) {
		try {
			FormEncodingBuilder builder = new FormEncodingBuilder();
			if (postParameters == null) {
				postParameters = new IdentityHashMap<String, String>();
			}
			if (url != null && url.contains("?")) {
				Uri uri = Uri.parse(url);
				String uriScheme = uri.getScheme();
				String uriAuthority = uri.getAuthority();
				String uriPath = uri.getPath();
				for (String key : uri.getQueryParameterNames()) {

					postParameters.put(key, uri.getQueryParameter(key));
				}
				url = uriScheme + "://" + uriAuthority;
				postParameters = securityCheckParams(context, postParameters, uriPath);
				Set<String> set = postParameters.keySet();
				for (String key : set) {
					builder.add(key, Uri.encode(postParameters.get(key), "UTF-8"));
				}
				RequestBody formBody = builder.build();
				Request request = new Request.Builder()
						.addHeader("User-Agent", USER_AGENT)
						.url(url)
						.put(formBody)
						.build();

				Response response = getCustomClient().newCall(request).execute();
				if (response.isSuccessful()) {
					return response.body().string();
				}
			}
		} catch (Exception e) {
			LogUtil.e("HttpUtil", e.toString());
		} finally {
		}
		return "";

	}

	/**
	 * delete方式
	 *
	 * @param url
	 * @return
	 */
	public static String httpDelete(Context context, String url, IdentityHashMap<String, String> postParameters) {
		try {
			FormEncodingBuilder builder = new FormEncodingBuilder();
			if (postParameters == null) {
				postParameters = new IdentityHashMap<String, String>();
			}
			if (url != null && url.contains("?")) {
				Uri uri = Uri.parse(url);
				String uriScheme = uri.getScheme();
				String uriAuthority = uri.getAuthority();
				String uriPath = uri.getPath();
				for (String key : uri.getQueryParameterNames()) {

					postParameters.put(key, uri.getQueryParameter(key));
				}
				url = uriScheme + "://" + uriAuthority;
				postParameters = securityCheckParams(context, postParameters, uriPath);
				Set<String> set = postParameters.keySet();
				for (String key : set) {
					builder.add(key, Uri.encode(postParameters.get(key), "UTF-8"));
				}
				RequestBody formBody = builder.build();
				Request request = new Request.Builder()
						.addHeader("User-Agent", USER_AGENT)
						.url(url)
						.method("DELETE", formBody)
						.build();

				Response response = getCustomClient().newCall(request).execute();
				if (response.isSuccessful()) {
					return response.body().string();
				}
			}
		} catch (Exception e) {
			LogUtil.e("HttpUtil", e.toString());
		} finally {
		}
		return "";

	}


	/**
	 * 数据安全检查
	 *
	 * @param params
	 * @return
	 */
	private static IdentityHashMap<String, String> securityCheckParams(Context context, IdentityHashMap<String, String> params, String method) {
		if (params == null)
			params = new IdentityHashMap<>();

		params.put("buildModel", Build.MODEL);
		params.put("appType", Config.APP_TYPE);
		params.put("deviceId", DeviceUtil.getDeviceId(context));
		params.put("deviceVersion", Build.VERSION.RELEASE);
		params.put("appVersion", VersionInfoUtils.getVersion(context));

		IdentityHashMap<String, String> newParam = new IdentityHashMap<>();
		newParam.put(Config.INTERFACE_NAME, getSidParam(params, method));
		return newParam;
	}

	private static IdentityHashMap<String, String> securityCheckParams(Context context, IdentityHashMap<String, String> params) {
		if (params == null)
			params = new IdentityHashMap<>();

		params.put("buildModel", Build.MODEL);
		params.put("appType", Config.APP_TYPE);
		params.put("deviceId", DeviceUtil.getDeviceId(context));
		params.put("deviceVersion", Build.VERSION.RELEASE);
		params.put("appVersion", VersionInfoUtils.getVersion(context));

		String result = "";
		StringBuilder strParams = new StringBuilder();
		List<Map.Entry<String, String>> infoIds = new ArrayList<>(params.entrySet());
		Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
			public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
				return o1.getKey().compareTo(o2.getKey());
			}
		});
		for (Map.Entry<String, String> entry : infoIds) {
			if (LogUtil.isDebug) Log.e(TAG, "entry : " + entry);
			strParams.append(String.format("%s%s", entry.getKey(), Uri.encode(entry.getValue(), "UTF-8")));
		}
		try {
			result = Base64.encodeToString(EnCryptionUtils.hexString(EnCryptionUtils.eccryptSHA1(strParams.toString().toLowerCase())).getBytes(), Base64.DEFAULT).trim();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		params.put("sign", result);
		return params;
	}

	private static String getSidParam(IdentityHashMap<String, String> params, String method) {
		StringBuilder str = new StringBuilder(); //待签名字符串
		StringBuilder strParams = new StringBuilder();
		String result = "";
		//先将参数以其参数名的字典序升序进行排序
		//ksort($params);
		if (params != null) {
			List<Map.Entry<String, String>> infoIds = new ArrayList<>(params.entrySet());
			//排序

			Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
				public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
					return o1.getKey().compareTo(o2.getKey());
				}
			});

			//遍历排序后的参数数组中的每一个key/value对
			for (Map.Entry<String, String> entry : infoIds) {
				if (LogUtil.isDebug) Log.e(TAG, "entry : " + entry);
				str.append(String.format("%s", Uri.encode(entry.getValue().replace("-", "--"), "UTF-8")));
				str.append(".-.");
				strParams.append(String.format("%s%s", entry.getKey(), Uri.encode(entry.getValue().replace("-", "--"), "UTF-8")));
			}
			if (LogUtil.isDebug) LogUtil.e(TAG, "method=" + method);
			str.append(Config.INTERFACE_VERSION);
			StringBuilder sbResult = new StringBuilder();
			//使用NO_WRAP会自动去掉全部的换行符
			sbResult.append(Base64.encodeToString(str.toString().getBytes(), Base64.DEFAULT).trim());
			sbResult = sbResult.reverse();
			sbResult.append("=");

			sbResult.append(Base64.encodeToString(Config.INTERFACE_CODE.getBytes(), Base64.DEFAULT).trim());
			String interfacePath = Base64.encodeToString(method.getBytes(), Base64.DEFAULT).trim();
			StringBuilder sbPath = new StringBuilder(interfacePath);
			if (sbPath != null && sbPath.length() > 1) {
				sbPath.insert(1, 'w');
			}
			sbResult.append(sbPath);
			sbResult.append("=");
			try {
				sbResult.append(Base64.encodeToString(EnCryptionUtils.hexString(EnCryptionUtils.eccryptSHA1(strParams.toString().toLowerCase())).getBytes(), Base64.DEFAULT).trim());
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			result = sbResult.toString().trim();
		}
		return result;
	}

	/**
	 * 初始化网络请求Client对象
	 */
	private static OkHttpClient getCustomClient() {
		if (client == null) {
			synchronized (HttpUtil.class) {
				if (client == null) {
					client = new OkHttpClient();

					if (LogUtil.isDebug) {
						client.networkInterceptors().add(new StethoInterceptor());
					}
//                    setCertificateForString();
					client.setConnectTimeout(10, TimeUnit.SECONDS);
					client.setWriteTimeout(10, TimeUnit.SECONDS);
					client.setReadTimeout(30, TimeUnit.SECONDS);
//                    client.setHostnameVerifier(new HostnameVerifier() {
//                        @Override
//                        public boolean verify(String hostname, SSLSession session) {
//
//                            return true;
//                        }
//                    });
				}
			}
		}
		return client;
	}

	public static void setCertificates(Context context) {
		try {

			CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			keyStore.load(null);
			int index = 0;
			InputStream certificate = context.getAssets().open("client.crt");
			String certificateAlias = Integer.toString(index++);
			keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));

			try {
				if (certificate != null)
					certificate.close();
			} catch (IOException e) {
			}

			SSLContext sslContext = SSLContext.getInstance("TLS");
			TrustManagerFactory trustManagerFactory = TrustManagerFactory.
					getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustManagerFactory.init(keyStore);
			//初始化keystore
			KeyStore clientKeyStore = KeyStore.getInstance("BKS");
			clientKeyStore.load(context.getAssets().open("configclient.bks"), "wjk123654".toCharArray());

			KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			keyManagerFactory.init(clientKeyStore, "wjk123654".toCharArray());

			sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
			client.setSslSocketFactory(sslContext.getSocketFactory());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void setCertificateForString() {
		try {
			CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			keyStore.load(null);
			int index = 0;
			InputStream certificate = new Buffer()
					.writeUtf8(Config.HTTPS_SHA256)
					.inputStream();
			String certificateAlias = Integer.toString(index++);
			keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));

			try {
				if (certificate != null)
					certificate.close();
			} catch (IOException e) {
			}

			SSLContext sslContext = SSLContext.getInstance("TLS");

			TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

			trustManagerFactory.init(keyStore);
			sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
			client.setSslSocketFactory(sslContext.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static class Config {

		//接口客户端编号
		public static final String APP_TYPE = "2_1";
		//域名
		public static final String DOMAIN_NAME = "wjika.com";
		//        public static final String DOMAIN_NAME = "123.57.33.106";
		// 接口版本
		public static final String INTERFACE_VERSION = "1.0";
		//接口编码
		public static final String INTERFACE_CODE = "000";
		//接口参数名称
		public static final String INTERFACE_NAME = "sid";
		//接口初始path
//        public static final String INTERFACE_PATH = "/card";
//        public static final String INTERFACE_PATH = "/wjapp_mobile_2/card";
		public static final String INTERFACE_PATH = "/wjapp_mobile/card";
		//HTTPS证书的sha1加密串
	    /*public static final String HTTPS_SHA1 = "-----BEGIN CERTIFICATE-----"+"\n"
                +"MIIDbzCCAlegAwIBAgIEVLb5izANBgkqhkiG9w0BAQsFADBoMQwwCgYDVQQLEwN3"+"\n"
                +"amsxEzARBgoJkiaJk/IsZAEZFgNCSUQxGzAZBgoJkiaJk/IsZAEZFgtTZXJ2ZXJI"+"\n"
                +"dHRwczEMMAoGA1UEChMDQklEMRgwFgYDVQQDEw8xMDEuMjAwLjEyNC4xMDEwHhcN"+"\n"
                +"MTYwMTIxMTAzODU2WhcNMzYwMTE2MTAzODU2WjBoMQwwCgYDVQQLEwN3amsxEzAR"+"\n"
                +"BgoJkiaJk/IsZAEZFgNCSUQxGzAZBgoJkiaJk/IsZAEZFgtTZXJ2ZXJIdHRwczEM"+"\n"
                +"MAoGA1UEChMDQklEMRgwFgYDVQQDEw8xMDEuMjAwLjEyNC4xMDEwggEiMA0GCSqG"+"\n"
                +"SIb3DQEBAQUAA4IBDwAwggEKAoIBAQCnZDaymQoslOi0DtURq50mJBAztx8P+xRZ"+"\n"
                +"xN4Xz9qiyOP6bkiz8NWC9cvDQtZZ+7Wr0+/k0IyviQKGUa7tOQr39x+oErzLUCyp"+"\n"
                +"3hVtb6Mk5qSobwDNKVs/WDbTiHKMFTTJBrT4E1JWZOkMhzYuWUt4A5f0W/L2LH2g"+"\n"
                +"RGXc073ji2MWha0kbLv9V8YFxGjhPFH4Hh2C8IL0vPE2ne7op9jMGUGZ/qc28Umb"+"\n"
                +"L+J7OWpjgulfko4rxKH6NhO31DdFIy+urbk8260VFm88ut/+Ks9rdVmktCHyqenO"+"\n"
                +"8Kcm7/fu0Xot8xjXeZevbGMmh2Q8hyIC07Zex83OB+5bveg6cBlBAgMBAAGjITAf"+"\n"
                +"MB0GA1UdDgQWBBR5GWoVu5kC4pEyMS8gF0VnkaSPzDANBgkqhkiG9w0BAQsFAAOC"+"\n"
                +"AQEAET80g62C8o5kzQCa/EEqHz06fbfZXYaInv6olQHN49Bgzp+1rIrOMl1iM1nQ"+"\n"
                +"kGiyUff5dohmDzCY/WGKYOAnbxnWDmJjfwlWUa/GvTQNlwOaJ66CiztpyAyQlHEc"+"\n"
                +"9O5kyt4Q/yDYndRTQDownlCDjNAj8K6XBDK/CZBMQJMxlvlerYqrakJcq9G880SI"+"\n"
                +"DAIcEdaKa0P6Ai+o7PvUOmdSIbyAwwcEB0BMGUZxKbnJ3bxVMXy12htOn0jxI5c4"+"\n"
                +"IQ10H5dNaGY+Zi+4LAnttZzK+EVx7vOl5hrr23TsM01tcRosD4vNqOkQJn/EcyFX"+"\n"
                +"4+qwnRGVGc+LKxjRE0NTkXg3tg=="+"\n"
                +"-----END CERTIFICATE-----";*/
		public static final String HTTPS_SHA256 = "-----BEGIN CERTIFICATE-----\n" +
				"MIIDazCCAlOgAwIBAgIETFExjzANBgkqhkiG9w0BAQsFADBmMQwwCgYDVQQLEwN3\n" +
				"amsxEzARBgoJkiaJk/IsZAEZFgNCSUQxGzAZBgoJkiaJk/IsZAEZFgtTZXJ2ZXJI\n" +
				"dHRwczEMMAoGA1UEChMDQklEMRYwFAYDVQQDEw0xMjMuNTcuMzMuMTA2MB4XDTE2\n" +
				"MDMxODA3MDk0MVoXDTM2MDMxMzA3MDk0MVowZjEMMAoGA1UECxMDd2prMRMwEQYK\n" +
				"CZImiZPyLGQBGRYDQklEMRswGQYKCZImiZPyLGQBGRYLU2VydmVySHR0cHMxDDAK\n" +
				"BgNVBAoTA0JJRDEWMBQGA1UEAxMNMTIzLjU3LjMzLjEwNjCCASIwDQYJKoZIhvcN\n" +
				"AQEBBQADggEPADCCAQoCggEBAKSgUVQiJJNyURVMb2WIIT3ElyH6a89IUyJw7cp4\n" +
				"nV7NBffCS7QO0N4xCqhh377fhZxxd10s7gPdZxpxdUa6k0glxCn/SW6LiIkIpjDn\n" +
				"qazN3OnAJ3tL1oZca/nePKeQ1DwWEX7ju/SkPcP49r1BXFsSy7G4BtscrBGBw6tF\n" +
				"NQKAkVVm+UOiyVPJqcIimnaq7prZR/zY00f1tcAbMJf31pdP6zKOh8uFdlkD58nI\n" +
				"mV2wDz4T3w5vPPXEmdcw+naGeOaRVYMvujF+XmxdDNlYMHLXdNoFfLvP7uGw1bsU\n" +
				"eMzvuZva8ILk8IRd95HbeSaAWh8MmJsFXDmzLPA62F8r8n0CAwEAAaMhMB8wHQYD\n" +
				"VR0OBBYEFDFPWExbMDS9HhgyXJCkV46VyKKYMA0GCSqGSIb3DQEBCwUAA4IBAQCQ\n" +
				"QSXUOD+sqYFjfbcwiD+Uc4QNf4xasA7QWqeDmrbt8oaGkNKZcvXb9gXcAgZvuYOL\n" +
				"/lx51aHRQ8ExSjVpsUCOACJGmk8pgMXt4aPEUjT/xQB0xwi2ajlS1721N8HK5/Tw\n" +
				"Ln93vLsM6VUMSSZyVlgb3JYv2ZADyuPijGaZ33lDXI7q/uZaJY3t8uWSyBObCdcY\n" +
				"eKezR/foC93SRpV8+nzBK73NJ6FNmZ2djcq12iWHYUiIdsuNIOBoifY3VgKZzMwo\n" +
				"ug8E0f7KFvYcPGjYQS3XomSVuk48vE5PBpZy6G+6B+TKbcXJNV354GuyWIb/vNTd\n" +
				"TN7bUW5+ZBw1RxCAqjOb\n" +
				"-----END CERTIFICATE-----\n";
	}
}
