package com.song.online_judge.judge.codesandbox;

import com.song.online_judge.judge.codesandbox.model.ExecuteCodeRequest;
import com.song.online_judge.judge.codesandbox.model.ExecuteCodeResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 代码沙箱代理
 */
@Slf4j
public class CodeSandBoxProxy implements CodeSandBox {

    private final CodeSandBox codeSandBox;

    public CodeSandBoxProxy(CodeSandBox codeSandBox) {
        this.codeSandBox = codeSandBox;
    }

    /**
     * 执行代码
     *
     * @param request
     * @return
     */
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest request) {
        log.info("代码沙箱请求信息: {}", request.toString());
        ExecuteCodeResponse response = codeSandBox.executeCode(request);
        log.info("代码沙箱响应信息:{}", response.toString());
        return response;
    }
}
