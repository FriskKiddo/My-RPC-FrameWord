package top.ccyy.hook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.ccyy.factory.ThreadPoolFactory;
import top.ccyy.util.NacosUtil;

public class ShutdownHook {

    private static final Logger logger = LoggerFactory.getLogger(ShutdownHook.class);

    private static final ShutdownHook shutDownHook = new ShutdownHook();

    public static ShutdownHook getShutDownHook() {
        return shutDownHook;
    }

    public void addClearAllHook() {
        logger.info("关闭后将自动注销所有服务");
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
                NacosUtil.deregister();
                ThreadPoolFactory.shutdownAll();

        }));
    }
}
