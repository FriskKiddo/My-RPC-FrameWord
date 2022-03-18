package top.ccyy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author FriskKiddo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequest implements Serializable {

    /**
     * 请求号
     */
    private String requestId;
    /**
     * 请求接口名
     */
    private String interfaceName;

    /**
     * 请求方法名
     */
    private String methodName;

    /**
     * 请求参数
     */
    private Object[] parameters;

    /**
     * 请求参数类型
     */
    private Class<?>[] paramTypes;
    /**
     * 是否是心跳包
     */
    private Boolean heartBeat;

}
