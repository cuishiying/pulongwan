package com.shanglan.pulongwan.base.sms;

import java.util.concurrent.Future;

/**
 * @author Wheat
 * 短信接口
 */
public interface SmsService {

	/**
	 * <b>发送一条短信</b>
	 * <p>短信的内容由模板定义，发送方只要传入模板编号和模板参数即可</p>
	 * <p>模板编号从SmsProperties中获取，如果需要新的模板，需要去阿里大鱼后台设置</p>
	 * @param mobile 接收人手机号
	 * @param templateCode 模板编码
	 * @param params 模板参数
	 * @return 是否发送成功
	 */
	Future<Boolean> send(String mobile, String templateCode, SmsParams params);
}
