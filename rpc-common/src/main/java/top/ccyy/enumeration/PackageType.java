package top.ccyy.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author FriskKiddo
 */

@AllArgsConstructor
@Getter
public enum PackageType {

    /**
     * 请求包类型
     */
    REQUEST_TYPE(1),
    /**
     * 相应包类型
     */
    RESPONSE_TYPE(2),

    ;

    /**
     * 包类型码
     */
    private final int code;
}
