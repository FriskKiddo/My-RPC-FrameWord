package top.ccyy.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS(200, "调用方法成功"),
    FAIL(500,"调用方法失败"),
    ;

    private final int code;
    private final String message;
    }
