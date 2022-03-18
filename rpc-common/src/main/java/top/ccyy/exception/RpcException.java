package top.ccyy.exception;

import lombok.AllArgsConstructor;
import top.ccyy.enumeration.RpcError;

@AllArgsConstructor
public class RpcException extends RuntimeException {

    public RpcException(RpcError error) {
        super(error.getContent());
    }

    public RpcException(RpcError serviceInvocationFailure, String message) {

    }
}
