package top.ccyy.transport.server;

public interface RpcServer {

    void start();

    <T> void publishService(T service, String serviceName);

}
