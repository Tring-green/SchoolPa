package com.example.schoolpa.Bean;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class Account implements Parcelable {

	private String userId;// 账号
	private String name;// 用户名
	private String icon;// 用户图像
	private int sex;// 性别 0:未设置 1:女 2:男 3:其他
	private String sign;// 用户个性签名
	private String area;// 用户所在区域
	private String token;// 用户与服务器交互的唯一标
	private boolean current;// 是否是当前用户

	public Account() {

	}

	public void transcoding() {
		try {
			name = URLDecoder.decode(name, "utf-8");
			sign = URLDecoder.decode(sign, "utf-8");
			area = URLDecoder.decode(area, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean isCurrent() {
		return current;
	}

	public void setCurrent(boolean current) {
		this.current = current;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	private Account(Parcel parcel) {
		Bundle val = parcel.readBundle();

		userId = val.getString("userId");
		name = val.getString("name");
		icon = val.getString("icon");
		sex = val.getInt("sex");
		sign = val.getString("sign");
		area = val.getString("area");
		token = val.getString("token");
		current = val.getBoolean("current");
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Bundle val = new Bundle();
		val.putString("userId", userId);
		val.putString("name", name);
		val.putString("icon", icon);
		val.putInt("sex", sex);
		val.putString("sign", sign);
		val.putString("area", area);
		val.putString("token", token);
		val.putBoolean("current", current);
		dest.writeBundle(val);
	}

	public static final Creator<Account> CREATOR = new Creator<Account>() {

		@Override
		public Account[] newArray(int size) {
			return new Account[size];
		}

		@Override
		public Account createFromParcel(Parcel source) {
			return new Account(source);
		}
	};

	@Override
	public String toString() {
		return "Account [userId=" + userId + ", name=" + name + ", icon="
				+ icon + ", sex=" + sex + ", sign=" + sign + ", area=" + area
				+ ", token=" + token + ", current=" + current + "]";
	}

}