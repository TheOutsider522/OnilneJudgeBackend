package com.song.online_judge.judge.codesandbox;

import com.song.online_judge.judge.codesandbox.model.ExecuteCodeRequest;
import com.song.online_judge.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * 代码沙箱接口定义
 */
public interface CodeSandBox {

    /**
     * 执行代码
     *
     * @param request
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest request);
}
