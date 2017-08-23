package com.shanglan.pulongwan.base.sms;

/**
 * @author wheat
 * 验证码接口
 */
public interface VerifyCodeService {

	/**
	 * 根据key获取一个验证码，会保存在缓存里
	 * @param key 一般是手机号
	 * @return 生成的验证码，有一定的有效期
	 */
	String get(String key);
	
	/**
	 * 判断验证码是否正确
	 * @param key
	 * @param verifyCode
	 * @return 验证结果
	 */
	VerifyResult validate(String key, String verifyCode);
}
