package top.ccyy.transport.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.ccyy.entity.RpcRequest;
import top.ccyy.enumeration.RpcError;
import top.ccyy.exception.RpcException;
import top.ccyy.loadbalancer.LoadBalancer;
import top.ccyy.loadbalancer.RoundRobinLoadBalancer;
import top.ccyy.Discovery.NacosServiceDiscovery;
import top.ccyy.Discovery.ServiceDiscovery;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * socket客户端
 * @author chen
 */
public class SocketClient implements RpcClient {

    private static final Logger logger = LoggerFactory.getLogger(SocketClient.class);
    private final ServiceDiscovery serviceDiscovery;
    private final Integer serializer;

    public SocketClient() {
        this(new RoundRobinLoadBalancer(), DEFAULT_SERIALIZER);
    }

    public SocketClient(Integer serializer) {
        this.serializer = serializer;
        this.serviceDiscovery = new NacosServiceDiscovery();
    }

    public SocketClient(LoadBalancer loadBalancer) {
        this.serializer = DEFAULT_SERIALIZER;
        this.serviceDiscovery = new NacosServiceDiscovery(loadBalancer);
    }

    public SocketClient(LoadBalancer loadBalancer, Integer serializer) {
        this.serviceDiscovery = new NacosServiceDiscovery(loadBalancer);
        this.serializer = serializer;
    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        if (serializer == null) {
            logger.error("serializer not set");
            throw new RpcException(RpcError.SERIALIZER_NOT_SET);
        }
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest.getInterfaceName());
        try (Socket socket = new Socket()) {
            socket.connect(inetSocketAddress);
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();

        } catch (IOException e) {
            logger.error("socket initial error");
            throw new RpcException(RpcError.SYSTEM_ERROR);
        }
        //todo socket#sendRequest
        return null;
    }

}
