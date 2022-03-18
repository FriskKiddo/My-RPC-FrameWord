package top.ccyy.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import top.ccyy.entity.RpcRequest;
import top.ccyy.enumeration.PackageType;
import top.ccyy.serializer.CommonSerializer;

/**
 * @author FriskKiddo
 */
public class CommonEncoder extends MessageToByteEncoder {

    private static final int MAGIC_NUMBER = 0xCAFEBABE;
    private final CommonSerializer serializer;

    public CommonEncoder(CommonSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object in, ByteBuf out) throws Exception {
        out.writeInt(MAGIC_NUMBER);
        if (in instanceof RpcRequest) {
            out.writeInt(PackageType.REQUEST_TYPE.getCode());
        } else {
            out.writeInt(PackageType.RESPONSE_TYPE.getCode());
        }
        out.writeInt(serializer.getCode());
        byte[] bytes = serializer.serialize(in);
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }
}
