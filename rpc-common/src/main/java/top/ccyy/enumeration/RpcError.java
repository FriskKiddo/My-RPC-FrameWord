package top.ccyy.enumeration;

import javafx.embed.swing.SwingNode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author FriskKiddo
 */

@AllArgsConstructor
@Getter
public enum RpcError {

    REGISTER_SERVICE_FAILED("注册服务失败"),
    CANNOT_CONNECT_REGISTRY("无法连接注册中心"),
    SERVICE_NOT_FOUND("无法找到服务"),
    METHOD_NOT_FOUND("未找到方法"),
    UNKNOWN_PROTOCOL("不识别协议包"),
    UNKNOWN_PACKAGE("不识别数据包"),
    CANNOT_CONNECT_SERVER("无法连接服务器"),
    SERVICE_INVOCATION_FAILURE("调用服务失败"),
    RESPONSE_NOT_MATCH("响应不匹配")
    ;

    private final String content;

}
