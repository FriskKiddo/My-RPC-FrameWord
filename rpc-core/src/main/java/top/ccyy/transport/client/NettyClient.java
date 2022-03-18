package top.ccyy.transport.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.ccyy.entity.RpcRequest;
import top.ccyy.entity.RpcResponse;
import top.ccyy.enumeration.RpcError;
import top.ccyy.exception.RpcException;
import top.ccyy.factory.SingletonFactory;
import top.ccyy.loadbalancer.RoundRobinLoadBalancer;
import top.ccyy.register.NacosServiceDiscovery;
import top.ccyy.register.ServiceDiscovery;
import top.ccyy.serializer.CommonSerializer;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * @author FriskKiddo
 */
public class NettyClient implements RpcClient {

    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);
    private final ServiceDiscovery serviceDiscovery = new NacosServiceDiscovery(new RoundRobinLoadBalancer());
    private final UnprocessedRequests unprocessedResponseFuture;
    private final CommonSerializer serializer;

    public NettyClient() {
        this(CommonSerializer.DEFAULT_SERIALIZER);
    }
    public NettyClient(int serializer) {
        this.serializer = CommonSerializer.getByCode(serializer);
        unprocessedResponseFuture = SingletonFactory.getInstance(UnprocessedRequests.class);
    }

    @Override
    public CompletableFuture<RpcResponse> sendRequest(RpcRequest rpcRequest) {
        CompletableFuture<RpcResponse> completeFuture = new CompletableFuture<>();
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest.getInterfaceName());
        //todo 添加中断异常
        try {
            Channel channel = ChannelProvider.get(inetSocketAddress, serializer);
//            if (!channel.isActive()) {
//                throw new RpcException(RpcError.CANNOT_CONNECT_SERVER);
//            }
            unprocessedResponseFuture.put(rpcRequest.getRequestId(), completeFuture);
            channel.writeAndFlush(rpcRequest).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    logger.info(String.format("客户端发送消息: %s", rpcRequest.toString()));
                } else {
                    future.channel().close();
                    completeFuture.completeExceptionally(future.cause());
                    logger.error("发送消息时出现错误", future.cause());
                }
            });
        } catch (InterruptedException e) {
            unprocessedResponseFuture.remove(rpcRequest.getRequestId());
            logger.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
        }

        return completeFuture;
    }
}
