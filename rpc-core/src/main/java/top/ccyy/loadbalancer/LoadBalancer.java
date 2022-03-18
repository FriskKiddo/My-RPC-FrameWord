package top.ccyy.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;
import org.slf4j.Logger;

import java.util.List;

public interface LoadBalancer {


//    Instance select(List<String> ServiceAddressList);

    Instance select(String serviceName);

}
