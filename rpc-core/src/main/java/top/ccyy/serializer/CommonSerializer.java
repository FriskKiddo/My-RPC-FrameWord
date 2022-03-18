package top.ccyy.serializer;


/**
 * @author FriskKiddo
 */
public interface CommonSerializer {

    Integer KRYO_SERIALIZER = 0;
    Integer JSON_SERIALIZER = 1;
    Integer HESSIAN_SERIALIZER = 2;
    Integer PROTOBUF_SERIALIZER = 3;

    Integer DEFAULT_SERIALIZER = KRYO_SERIALIZER;

    //todo 4种序列化器

    /**
     * 根据码获取序列化器
     * @param code
     * @return
     */
    static CommonSerializer getByCode(int code) {
        switch (code) {
            case 0:
                return new KryoSerializer();
            case 1:
                return new JsonSerializer();
            case 2:
                return new HessianSerializer();
            case 3:
                return new ProtobufSerializer();
            default:
                return null;
        }
    }

    /**
     * 获取序列化器的码
     * @return
     */
    int getCode();

    /**
     * 对传入实体进行序列化
     * @param message
     * @return
     */
    byte[] serialize(Object message);

    /**
     * 根据包类型对字节数组完成反序列化
     * 反序列化需要得知字符数组是Request还是Response
     * @param bytes
     * @param packageClass
     * @return
     */
    Object deserialize(byte[] bytes, Class<?> packageClass);
}
