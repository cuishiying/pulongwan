package com.shanglan.pulongwan.base.sms;

/**
 * @author Wheat 短信模板编码
 */
public class SmsProperties {

	private String registerCode;
	private String passwordCode;
	private String loginCode;
	private String createPasswordCode;
	private String signName;

	public SmsProperties(String registerCode, String passwordCode, String loginCode, String createPasswordCode,
			String signName) {
		this.registerCode = registerCode;
		this.passwordCode = passwordCode;
		this.loginCode = loginCode;
		this.createPasswordCode = createPasswordCode;
		this.signName = signName;
	}

	/**
	 * 注册验证码的模板编码
	 */
	public String getRegisterCode() {
		return registerCode;
	}

	/**
	 * 修改密码验证码的模板编码
	 */
	public String getPasswordCode() {
		return passwordCode;
	}

	/**
	 * 登录验证码的模板编码
	 */
	public String getLoginCode() {
		return loginCode;
	}

	/**
	 * @return 后台创建账户，告知用户密码的短信模板编码
	 */
	public String getCreatePasswordCode() {
		return createPasswordCode;
	}

	/**
	 * @return 短信签名
	 */
	public String getSignName() {
		return signName;
	}
}
