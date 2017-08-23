package com.shanglan.pulongwan.base.sms;

import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taobao.api.ApiException;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

@Service
public class SmsServiceImpl implements SmsService {
	private final static Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);

	@Autowired
	private SmsProperties smsProperties;

	@Autowired
	private TaobaoClient client;

	private ObjectMapper objectMapper;

	public SmsServiceImpl() {
		objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_EMPTY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.irelint.b2c.sms.SmsService#send(java.lang.String,
	 * java.lang.String, com.irelint.b2c.sms.SmsParams)
	 */
	@Override
	@Async
	public Future<Boolean> send(String mobile, String templateCode, SmsParams params) {
		try {
			AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
			req.setSmsType("normal");
			req.setSmsFreeSignName(smsProperties.getSignName());
			String jsonParams = objectMapper.writeValueAsString(params);
			logger.info("send sms to [{}], template [{}] with params [{}]", mobile, templateCode, jsonParams);
			req.setSmsParamString(jsonParams);
			req.setRecNum(mobile);
			req.setSmsTemplateCode(templateCode);
			AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
			return new AsyncResult<>(rsp.isSuccess());
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Parse SmsParams Error", e);
		} catch (ApiException e) {
			logger.error("Call Alidayu API exception", e);
			return new AsyncResult<>(false);
		}
	}
}
