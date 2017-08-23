package com.shanglan.pulongwan.base.sms;

import java.io.Serializable;

public class VerifyResult implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean success;
	
	private String message;

	public boolean isSuccess() {
		return success;
	}
	
	public String getMessage() {
		return message;
	}

	public static VerifyResult success() {
		VerifyResult result = new VerifyResult();
		result.success = true;
		return result;
	}

	public static VerifyResult fail() {
		VerifyResult result = new VerifyResult();
		result.success = false;
		result.message = "验证码不正确或已经超时";
		return result;
	}
}
