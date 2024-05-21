package com.song.online_judge.judge.codesandbox;

import com.song.online_judge.judge.codesandbox.impl.ExampleCodeSendBox;
import com.song.online_judge.judge.codesandbox.impl.RemoteCodeSendBox;
import com.song.online_judge.judge.codesandbox.impl.ThirdPartyCodeSendBox;
import com.song.online_judge.judge.codesandbox.model.ExecuteCodeRequest;
import com.song.online_judge.judge.codesandbox.model.ExecuteCodeResponse;
import org.springframework.beans.factory.annotation.Value;

/**
 * 代码沙箱创建工厂
 */
public class CodeSandBoxFactory {

    /**
     * 创建代码沙箱
     *
     * @param type 沙箱类型
     * @return 对应类型的代码沙箱
     */
    public static CodeSandBox createCodeSandBox(CodeSandBoxTypeEnum type) {
        switch (type) {
            case EXAMPLE:
                return new ExampleCodeSendBox();
            case REMOTE:
                return new RemoteCodeSendBox();
            case THIRD_PARTY:
                return new ThirdPartyCodeSendBox();
            default:
                return new ExampleCodeSendBox();
        }
    }

    /**
     * 创建代理的代码沙箱
     * @param type
     * @return
     */
    public static CodeSandBox createProxyCodeSandBox(CodeSandBoxTypeEnum type) {
        switch (type) {
            case EXAMPLE:
                return new CodeSandBoxProxy(new ExampleCodeSendBox());
            case REMOTE:
                return new CodeSandBoxProxy(new RemoteCodeSendBox());
            case THIRD_PARTY:
                return new CodeSandBoxProxy(new ThirdPartyCodeSendBox());
            default:
                return new CodeSandBoxProxy(new ExampleCodeSendBox());
        }
    }
}
