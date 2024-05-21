package com.song.online_judge.judge.codesandbox;

import lombok.Data;
import lombok.Getter;

/**
 * 代码沙箱类型枚举
 */
@Getter
public enum CodeSandBoxTypeEnum {
    /**
     * 示例代码沙箱
     */
    EXAMPLE,

    /**
     * 远程代码沙箱
     */
    REMOTE,

    /**
     * 第三方代码沙箱
     */
    THIRD_PARTY;

}
