package top.ccyy.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;
import top.ccyy.util.NacosUtil;

import java.util.List;
import java.util.Random;

/**
 * 随机轮询
 *
 * @author chen
 */
public class RandomLoadBalancer implements LoadBalancer {

    @Override
    public Instance select(String serviceName) {
        List<Instance> allInstances = NacosUtil.getAllInstances(serviceName);
        return allInstances.get(new Random().nextInt(allInstances.size()));
    }
}
