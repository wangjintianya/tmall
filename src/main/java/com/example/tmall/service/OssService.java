package com.example.tmall.service;

import com.example.tmall.dto.OssCallbackParam;
import com.example.tmall.dto.OssCallbackResult;
import com.example.tmall.dto.OssPolicyResult;

import javax.servlet.http.HttpServletRequest;

public interface OssService {
    /**
     * 上传策略生成
     */
    OssPolicyResult policy();

    /**
     * 上传成功回调
     */

    OssCallbackResult callback(HttpServletRequest httpServletRequest);
}
