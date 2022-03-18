package top.ccyy;

public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(HelloObject object) {
        System.out.println("注册的hello方法");
        return "调用方法的返回值" + object.getId();
    }
}
