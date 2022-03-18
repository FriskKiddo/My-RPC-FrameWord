package top.ccyy.loadbalancer;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.extern.slf4j.Slf4j;
import top.ccyy.enumeration.RpcError;
import top.ccyy.exception.RpcException;
import top.ccyy.util.NacosUtil;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 一致性哈希
 *
 * @author FriskKiddo
 */
@Slf4j
public class ConsistentHashLoadBalance implements LoadBalancer {

    private final ConcurrentHashMap<String, ConsistentHashSelector> selectors = new ConcurrentHashMap<>();

    @Override
    public Instance select(String serviceName) {
        List<Instance> instanceList;
        instanceList = NacosUtil.getAllInstances(serviceName);
        int identityHashCode = System.identityHashCode(instanceList);
        ConsistentHashSelector selector = selectors.get(serviceName);
        if (selector == null || selector.identityHashCode != identityHashCode) {
            selectors.put(serviceName, new ConsistentHashSelector(instanceList, Integer.MAX_VALUE, identityHashCode));
            selector = selectors.get(serviceName);
        }

        return selector.doSelect(serviceName);
    }

    static class ConsistentHashSelector {

        private final TreeMap<Long, Instance> virtualInvokers;

        //节点辨识
        private final int identityHashCode;

        /**
         * @param invokers         节点列表
         * @param replicaNumber    哈希环大小
         * @param identityHashCode 标识码
         */
        ConsistentHashSelector(List<Instance> invokers, int replicaNumber, int identityHashCode) {
            this.virtualInvokers = new TreeMap<>();
            this.identityHashCode = identityHashCode;
            int virtualNode = 4;  //虚拟节点数目

            for (Instance invoker : invokers) {
                for (int i = 0; i < replicaNumber / virtualNode; i++) {
                    byte[] digest = md5(invoker.getClusterName() + i);
                    for (int h = 0; h < 4; h++) {
                        long m = hash(digest, h);
                        virtualInvokers.put(m, invoker);
                    }
                }
            }
        }

        static byte[] md5(String key) {
            MessageDigest md;
            try {
                md = MessageDigest.getInstance("MD5");
                byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
                md.update(bytes);
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }

            return md.digest();
        }


        static long hash(byte[] digest, int idx) {
            return ((long) (digest[3 + idx * 4] & 255) << 24 | (long) (digest[2 + idx * 4] & 255) << 16 | (long) (digest[1 + idx * 4] & 255) << 8 | (long) (digest[idx * 4] & 255)) & 4294967295L;
        }

        public Instance doSelect(String rpcServiceName) {
            byte[] digest = md5(rpcServiceName);
            return selectForKey(hash(digest, 0));
        }

        public Instance selectForKey(long hashCode) {

            Map.Entry<Long, Instance> entry = virtualInvokers.tailMap(hashCode, true).firstEntry();
            if (entry == null) {
                entry = virtualInvokers.firstEntry();
            }
            return entry.getValue();
        }
    }
}
