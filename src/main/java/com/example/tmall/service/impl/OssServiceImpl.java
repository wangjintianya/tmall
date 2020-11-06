package com.example.tmall.service.impl;

import cn.hutool.json.JSONUtil;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.example.tmall.dto.OssCallbackParam;
import com.example.tmall.dto.OssCallbackResult;
import com.example.tmall.dto.OssPolicyResult;
import com.example.tmall.service.OssService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class OssServiceImpl implements OssService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OssServiceImpl.class);
    @Value("${aliyun.oss.policy.expire}")
    private int ALIYUN_OSS_EXPIRE;
    @Value("${aliyun.oss.maxSize}")
    private int ALIYUN_OSS_MAX_SIZE;
    @Value("${aliyun.oss.callback}")
    private String ALIYUN_OSS_CALLBACK;
    @Value("${aliyun.oss.bucketName}")
    private String ALIYUN_OSS_BUCKET_NAME;
    @Value("${aliyun.oss.endpoint}")
    private String ALIYUN_OSS_ENDPOINT;
    @Value("${aliyun.oss.dir.prefix}")
    private String ALIYUN_OSS_DIR_PREFIX;

    @Autowired
    private OSSClient ossClient;

    /**
     * 签名生成
     *
     * @return
     */
    @Override
    public OssPolicyResult policy() {
        OssPolicyResult ossPolicyResult = new OssPolicyResult();
        // 存储目录
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dir = ALIYUN_OSS_DIR_PREFIX + sdf.format(new Date());
        // 签名有效期
        long expiredTime = System.currentTimeMillis() + ALIYUN_OSS_EXPIRE * 1000;
        Date expiration = new Date(expiredTime);
        // 文件大小
        long maxSize = ALIYUN_OSS_MAX_SIZE * 1024 * 1024;
        // 回调
        OssCallbackParam ossCallbackParam = new OssCallbackParam();
        ossCallbackParam.setCallbackUrl(ALIYUN_OSS_CALLBACK);
        ossCallbackParam.setCallbackBody("filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}");
        ossCallbackParam.setCallbackBodyType("application/x-www-form-urlencoded");
        // 提交节点
        String action = "Http://" + ALIYUN_OSS_BUCKET_NAME + "." + ALIYUN_OSS_ENDPOINT;

        PolicyConditions policyConditions = new PolicyConditions();
        policyConditions.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, maxSize);
        policyConditions.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
        String postPolicy = ossClient.generatePostPolicy(expiration, policyConditions);
        byte[] binaryData = new byte[0];
        try {
            binaryData = postPolicy.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String policy = BinaryUtil.toBase64String(binaryData);
        String signature = ossClient.calculatePostSignature(postPolicy);
        String callbackData = null;
        try {
            callbackData = BinaryUtil.toBase64String(JSONUtil.parse(ossCallbackParam).toString().getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 返回结果
        ossPolicyResult.setAccessKeyId(ossClient.getCredentialsProvider().getCredentials().getAccessKeyId());
        ossPolicyResult.setPolicy(policy);
        ossPolicyResult.setSignature(signature);
        ossPolicyResult.setDir(dir);
        ossPolicyResult.setCallback(callbackData);
        ossPolicyResult.setHost(action);

        return ossPolicyResult;
    }

    @Override
    public OssCallbackResult callback(HttpServletRequest request) {
        OssCallbackResult result = new OssCallbackResult();
        String filename = request.getParameter("filename");
        filename = "http://".concat(ALIYUN_OSS_BUCKET_NAME).concat(".").concat(ALIYUN_OSS_ENDPOINT).concat("/").concat(filename);
        result.setFilename(filename);
        result.setSize(request.getParameter("size"));
        result.setMimeType(request.getParameter("mimeType"));
        result.setWidth(request.getParameter("width"));
        result.setHeight(request.getParameter("height"));

        return result;
    }
}
