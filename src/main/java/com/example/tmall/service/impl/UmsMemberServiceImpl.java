package com.example.tmall.service.impl;

import com.example.tmall.common.api.CommonResult;
import com.example.tmall.service.RedisService;
import com.example.tmall.service.UmsMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Random;

@Service
public class UmsMemberServiceImpl implements UmsMemberService {

    @Value("${redis.key.prefix.authCode}")
    private String REDIS_KEY_PREFIX_AUTH_CODE;
    @Value("${redis.key.expire.authCode}")
    private Long AUTH_CODE_EXPIRE_SECONDS;
    @Autowired
    private RedisService redisService;

    @Override
    public CommonResult generateAuthCode(String telphone) {
        long expire = redisService.getExpire(REDIS_KEY_PREFIX_AUTH_CODE);
        if (expire > AUTH_CODE_EXPIRE_SECONDS / 3) {
            return CommonResult.success(
                    redisService.get(REDIS_KEY_PREFIX_AUTH_CODE + telphone), "获取成功");
        }
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            stringBuilder.append(random.nextInt(10));
        }
        String code = stringBuilder.toString();

        // 将验证码绑定手机号并存储到redis
        redisService.set(REDIS_KEY_PREFIX_AUTH_CODE + telphone, code);
        redisService.expire(REDIS_KEY_PREFIX_AUTH_CODE + telphone, AUTH_CODE_EXPIRE_SECONDS);

        return CommonResult.success(code, "获取成功");
    }

    @Override
    public CommonResult verifyAuthCode(String telphone, String authCode) {

        if (StringUtils.isEmpty(authCode)) {
            return CommonResult.failed("请输入验证码");
        }
        String authCodeSys = redisService.get(REDIS_KEY_PREFIX_AUTH_CODE + telphone);

        return authCode.equals(authCodeSys) ?
                CommonResult.success(null, "验证码校验成功")
                : CommonResult.failed("验证码有误");
    }
}
