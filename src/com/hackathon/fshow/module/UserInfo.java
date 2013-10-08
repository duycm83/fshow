package com.hackathon.fshow.module;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class UserInfo {
	public static final String NAME = "name";
	public static final String EMAIL = "email";
	public static final String PASSWORD = "password";

	private String name;
	private String email;
	private String password;

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name セットする name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email セットする email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password セットする password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public List<NameValuePair> makePostFormData() {
		List<NameValuePair> nameValuePairs = null;
		int validate = checkDataValidate();
		if (validate > 0) {
			nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair(NAME, name));
			nameValuePairs.add(new BasicNameValuePair(EMAIL, email));
			nameValuePairs.add(new BasicNameValuePair(PASSWORD, password));
		}
		return nameValuePairs;
	}

	public int checkDataValidate() {
		int result = 1;
		if (name == null) {
			result = -1;
		} else if (email == null) {
			result = -2;
		} else if (password == null) {
			result = -3;
		}
		return result;
	}
}
