package com.example.wechatpaydemo.v3.anno;

import java.lang.annotation.*;

/**
 * 字符串
 * @author qingzhou
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SplitIndex {

    /**
     * 填充下标
     */
    int value();
}
