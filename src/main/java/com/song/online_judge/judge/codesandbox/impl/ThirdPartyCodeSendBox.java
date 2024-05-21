package com.song.online_judge.judge.codesandbox.impl;

import com.song.online_judge.judge.codesandbox.CodeSandBox;
import com.song.online_judge.judge.codesandbox.model.ExecuteCodeRequest;
import com.song.online_judge.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * 第三方代码沙箱(调用现成的代码沙箱)
 */
public class ThirdPartyCodeSendBox implements CodeSandBox {
    /**
     * 执行代码
     *
     * @param request
     * @return
     */
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest request) {
        System.out.println("第三方代码沙箱执行代码");
        return null;
    }
}
