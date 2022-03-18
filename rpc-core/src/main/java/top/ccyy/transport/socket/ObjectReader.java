package top.ccyy.transport.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.ccyy.entity.RpcRequest;
import top.ccyy.entity.RpcResponse;
import top.ccyy.enumeration.PackageType;
import top.ccyy.enumeration.RpcError;
import top.ccyy.exception.RpcException;
import top.ccyy.serializer.CommonSerializer;

import java.io.IOException;
import java.io.InputStream;

/**
 * Socket方式从输入流中读取字节并反序列化
 */
public class ObjectReader {

    private static final Logger logger = LoggerFactory.getLogger(ObjectReader.class);
    private static final int MAGIC_NUMBER = 0xCBCBCBCB;

    public static Object read(InputStream in) throws IOException {
        byte[] intBytes = new byte[4];
        in.read(intBytes);
        int magic = bytesToInt(intBytes);
        if (MAGIC_NUMBER != magic) {
            logger.error("不识别协议包: {}", magic);
            throw new RpcException(RpcError.UNKNOWN_PROTOCOL);
        }
        in.read(intBytes);
        int packageType = bytesToInt(intBytes);
        Class<?> packageClass;
        if (packageType == PackageType.REQUEST_TYPE.getCode()) {
            packageClass = RpcRequest.class;
        } else if (packageType == PackageType.RESPONSE_TYPE.getCode()) {
            packageClass = RpcResponse.class;
        } else {
            logger.error("未识别包类型: {}", packageType);
            throw new RpcException(RpcError.UNKNOWN_PROTOCOL);
        }
        in.read(intBytes);
        int serializerCode = bytesToInt(intBytes);
        CommonSerializer serializer = CommonSerializer.getByCode(serializerCode);
        if (serializer == null) {
            logger.error("未识别序列化器: {}", serializerCode);
            throw new RpcException(RpcError.UNKNOWN_SERIALIZER);
        }
        in.read(intBytes);
        int length = bytesToInt(intBytes);
        byte[] data = new byte[length];
        in.read(data);
        return serializer.deserialize(data, packageClass);
    }

    public static int bytesToInt(byte[] src) {
        return ((src[0] & 0xff) << 24) | ((src[1] & 0xff) << 16) | ((src[2] & 0xff) << 8) | ((src[3] & 0xff));
    }

}
