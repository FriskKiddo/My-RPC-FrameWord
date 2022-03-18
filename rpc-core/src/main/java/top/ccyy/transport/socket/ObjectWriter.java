package top.ccyy.transport.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.ccyy.entity.RpcRequest;
import top.ccyy.enumeration.PackageType;
import top.ccyy.serializer.CommonSerializer;

import java.io.IOException;
import java.io.OutputStream;

public class ObjectWriter {

    private static final Logger logger = LoggerFactory.getLogger(ObjectWriter.class);
    private static final int MAGIC_NUMBER = 0xCBCBCBCB;

    public static void write(OutputStream out, Object obj, CommonSerializer serializer) throws IOException {
        out.write(intToByte(MAGIC_NUMBER));
        if (obj instanceof RpcRequest) {
            out.write(intToByte(PackageType.REQUEST_TYPE.getCode()));
        } else {
            out.write(intToByte(PackageType.RESPONSE_TYPE.getCode()));
        }
        out.write(serializer.getCode());
        byte[] data = serializer.serialize(obj);
        out.write(data);
        out.flush();
    }

    public static byte[] intToByte(int src) {
        byte[] dest = new byte[4];
        dest[0] = (byte) ((src >> 24) & 0xff);
        dest[1] = (byte) ((src >> 16) & 0xff);
        dest[2] = (byte) ((src >> 8) & 0xff);
        dest[3] = (byte) (src & 0xff);
        return dest;
    }

}
