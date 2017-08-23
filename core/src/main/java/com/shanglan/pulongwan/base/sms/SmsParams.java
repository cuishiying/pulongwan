package com.shanglan.pulongwan.base.sms;

/**
 * @author Wheat 短信模板参数。code为验证码，productName为系统的名称
 */
public class SmsParams {

	private String code;
	private String product;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}
}
