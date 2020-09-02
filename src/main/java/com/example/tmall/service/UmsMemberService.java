package com.example.tmall.service;

import com.example.tmall.common.api.CommonResult;

public interface UmsMemberService {
    /**
     * 生成验证码
     *
     * @param telphone
     * @return
     */
    CommonResult generateAuthCode(String telphone);

    /**
     * 验证码是否匹配
     *
     * @param telphone
     * @param authCode
     * @return
     */
    CommonResult verifyAuthCode(String telphone, String authCode);

}
