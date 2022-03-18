package top.ccyy.enumeration;

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
    UNKNOWN_SERIALIZER("不识别的序列化器"),
    CANNOT_CONNECT_SERVER("无法连接服务器"),
    SERVICE_INVOCATION_FAILURE("调用服务失败"),
    RESPONSE_NOT_MATCH("响应不匹配"),
    SERIALIZER_NOT_SET("未设置序列化器"),
    SYSTEM_ERROR("系统错误"),
    ;

    private final String content;

}
