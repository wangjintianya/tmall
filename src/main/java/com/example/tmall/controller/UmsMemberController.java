package com.example.tmall.controller;

import com.example.tmall.common.api.CommonResult;
import com.example.tmall.service.UmsMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 会员登陆注册管理
 */
@Api(tags = "UmsMemberController", description = "会员登陆注册管理")
@RestController
@RequestMapping("/sso")
public class UmsMemberController {
    @Autowired
    private UmsMemberService umsMemberService;

    @ApiOperation("获取验证码")
    @RequestMapping(value = "/getAuthCode", method = RequestMethod.GET)
    public CommonResult getAuthCode(@RequestParam String telphone) {
        return umsMemberService.generateAuthCode(telphone);
    }

    @ApiOperation("校验验证码")
    @RequestMapping(value = "/verifyAuthCode", method = RequestMethod.POST)
    public CommonResult verifyAuthCode(@RequestParam String telphone, @RequestParam String authCode) {
        return umsMemberService.verifyAuthCode(telphone, authCode);
    }
}
