package com.example.schoolpa.lib;

public class SPURL {

	 public static String BASE_HTTP = "http://10.0.2.2:8000/";
	 public static String BASE_SP_HOST = "10.0.2.2";
	 public static int BASE_SP_PORT = 9000;
	 public static String BASE_QR = BASE_HTTP + "/QR/";


	/**
	 * 登录部分的url地址
	 */
	public final static String URL_HTTP_LOGIN = BASE_HTTP + "login";
	public final static String URL_HTTP_REGISTER = BASE_HTTP + "register";
	public final static String URL_HTTP_LOGOUT = BASE_HTTP + "logout";

	/**
	 * 搜索用户
	 */
	public final static String URL_HTTP_SEARCH_CONTACT = BASE_HTTP
			+ "/user/search";

}
