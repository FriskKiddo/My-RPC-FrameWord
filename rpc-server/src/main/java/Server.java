import top.ccyy.HelloService;
import top.ccyy.HelloServiceImpl;
import top.ccyy.transport.server.NettyServer;
import top.ccyy.transport.server.RpcServer;

/**
 * 规定服务名为接口名
 *
 */
public class Server {
    public static void main(String[] args) {
        //for example
        RpcServer server = new NettyServer("127.0.0.1", 8888);
        HelloService service = new HelloServiceImpl();
        String serviceName = service.getClass().getInterfaces()[0].getCanonicalName();
        System.out.println(serviceName);
        server.publishService(service, serviceName);
        server.start();
    }
}
