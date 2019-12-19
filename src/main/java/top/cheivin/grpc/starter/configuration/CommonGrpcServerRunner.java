package top.cheivin.grpc.starter.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import top.cheivin.grpc.GrpcServer;
import top.cheivin.grpc.core.ServiceInfoManage;

import javax.annotation.Resource;
import java.util.List;

/**
 * 服务启动端
 * user:cheivin
 * date:2019/12/19
 */
public class CommonGrpcServerRunner implements CommandLineRunner, Ordered {
    @Resource
    private GrpcServer server;
    @Resource
    private ServiceInfoManage serviceInfoManage;

    @Override
    public void run(String... args) throws Exception {
        List<Object> serviceBeans = CommonGrpcServiceScannerRegistrar.getgRpcServiceInfoBeanList();
        serviceBeans.forEach(serviceInfoManage::addService);
        server.start();
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}
