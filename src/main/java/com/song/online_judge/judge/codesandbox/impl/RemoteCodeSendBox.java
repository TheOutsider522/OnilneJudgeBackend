package com.song.online_judge.judge.codesandbox.impl;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.song.online_judge.common.ErrorCode;
import com.song.online_judge.exception.BusinessException;
import com.song.online_judge.judge.codesandbox.CodeSandBox;
import com.song.online_judge.judge.codesandbox.model.ExecuteCodeRequest;
import com.song.online_judge.judge.codesandbox.model.ExecuteCodeResponse;
import org.apache.commons.lang3.StringUtils;

/**
 * 远程代码沙箱(实际调用)
 */
public class RemoteCodeSendBox implements CodeSandBox {

    private static final String AUTH_REQUEST_HEADER = "auth";

    private static final String AUTH_REQUEST_SECRET = "secretKey";

    /**
     * 执行代码
     *
     * @param request
     * @return
     */
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest request) {
        System.out.println("远程代码沙箱执行代码");
        String url = "http://192.168.109.43:8080/execute";
        String request_json = JSONUtil.toJsonStr(request);
        HttpResponse remoteCodeSandBoxResponse = HttpUtil.createPost(url).header(AUTH_REQUEST_HEADER, AUTH_REQUEST_SECRET).body(request_json).execute();
        String resBody = remoteCodeSandBoxResponse.body();
        System.out.println("resBody:" + resBody);
        if (StringUtils.isBlank(resBody)) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR, "远程代码沙箱接口调用失败");
        }
        ExecuteCodeResponse response = JSONUtil.toBean(resBody, ExecuteCodeResponse.class);
        return response;
    }
}
