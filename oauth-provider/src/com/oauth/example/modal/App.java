/**
 * 
 */
package com.oauth.example.modal;

import java.util.UUID;

/**
 * @author DEEPAK
 *
 */
public class App {

	private UUID appId;
	private String appName;
	private String appDescription;
	private String callbackUrl;
	private String appSecret;

	public UUID getAppId() {
		return appId;
	}

	public void setAppId(UUID appId) {
		this.appId = appId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppDescription() {
		return appDescription;
	}

	public void setAppDescription(String appDescription) {
		this.appDescription = appDescription;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

}
