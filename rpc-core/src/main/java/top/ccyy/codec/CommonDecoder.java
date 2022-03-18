package top.ccyy.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.ccyy.entity.RpcRequest;
import top.ccyy.entity.RpcResponse;
import top.ccyy.enumeration.PackageType;
import top.ccyy.enumeration.RpcError;
import top.ccyy.exception.RpcException;
import top.ccyy.serializer.CommonSerializer;

import java.util.List;

/**
 * @author FriskKiddo
 */
public class CommonDecoder extends ReplayingDecoder {

    private static final Logger logger = LoggerFactory.getLogger(CommonDecoder.class);
    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int magic = in.readInt();
        if (magic != MAGIC_NUMBER) {
            logger.error("不识别协议包：{}", magic);
            throw new RpcException(RpcError.UNKNOWN_PROTOCOL);
        }
        int packageTypeCode = in.readInt();
        Class<?> packageClass = null;
        if (packageTypeCode == PackageType.REQUEST_TYPE.getCode()) {
            packageClass = RpcRequest.class;
        } else if (packageTypeCode == PackageType.RESPONSE_TYPE.getCode()) {
            packageClass = RpcResponse.class;
        } else {
            logger.error("不识别数据包：{}", packageTypeCode);
            throw new RpcException(RpcError.UNKNOWN_PACKAGE);
        }
        int serializerCode = in.readInt();
        CommonSerializer serializer = CommonSerializer.getByCode(serializerCode);
        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.readBytes(bytes);
        Object object = serializer.deserialize(bytes, packageClass);
        out.add(object);
    }
}
