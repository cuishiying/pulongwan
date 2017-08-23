package com.shanglan.pulongwan.base.sms;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class VerifyCodeServiceImpl implements VerifyCodeService {

	private final static int DEFAULT_CODE_LENGTH = 5;
	private final static int[] LENGTH_NUMBERS = { 1, 10, 100, 1000, 10000, 100000, 1000000 };
	private final static int DEFAULT_TIMEOUT = 10; // 默认10分钟超时
	private final static String KEY_PREFIX = "verifycode::";

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Override
	public String get(String key) {
		String code = generateCode(DEFAULT_CODE_LENGTH);
		redisTemplate.opsForValue().set(KEY_PREFIX + key, code, DEFAULT_TIMEOUT, TimeUnit.MINUTES);
		return code;
	}

	private String generateCode(int length) {
		Assert.isTrue(length <= LENGTH_NUMBERS.length, "Verify Code length must not exceed 6 numbers!");
		return String.format("%0" + length + "d", ThreadLocalRandom.current().nextInt(LENGTH_NUMBERS[length]));
	}

	@Override
	public VerifyResult validate(String key, String verifyCode) {
		String saved = redisTemplate.opsForValue().get(KEY_PREFIX + key);
		if (saved != null && saved.equals(verifyCode)) {
			return VerifyResult.success();
		}
		return VerifyResult.fail();
	}
}
