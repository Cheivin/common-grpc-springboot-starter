package top.cheivin.grpc.starter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import top.cheivin.grpc.annotation.GrpcService;
import top.cheivin.grpc.core.GrpcRequest;
import top.cheivin.grpc.core.ServiceInfo;
import top.cheivin.grpc.core.ServiceInfoManage;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Spring的Grpc服务信息管理器
 * user:cheivin
 * date:2019/12/18
 */
public class SpringServiceInfoManage implements ServiceInfoManage {
    private static Logger log = LoggerFactory.getLogger(SpringServiceInfoManage.class);

    private HashMap<String, ServiceInfo> serviceInfoMap = new HashMap<>();
    private Map<String, Object> instanceMap = new HashMap<>();

    @Resource
    private ApplicationContext context;

    @Override
    public boolean addService(Class clz) {
        GrpcService info;
        out:
        do {
            // 从当前类找注解
            info = (GrpcService) clz.getAnnotation(GrpcService.class);
            if (info == null) {
                // 从当前类接口找注解
                Class[] interfaces = clz.getInterfaces();
                for (Class anInterface : interfaces) {
                    info = (GrpcService) anInterface.getAnnotation(GrpcService.class);
                    if (info != null) {
                        // spring中使用接口类
                        clz = anInterface;
                        break out;
                    }
                }
                // 向上找父类
                clz = clz.getSuperclass();
            }
            // 父类为顶级类则跳出查找
        } while (!clz.equals(Object.class));
        if (info == null) {
            return false;
        }
        return addService(ServiceInfo.builder()
                .clz(clz)
                .serviceName("".equals(info.service()) ? clz.getSimpleName() : info.service())
                .version(info.version())
                .weight(info.weight())
                .alias("".equals(info.alias()) ? clz.getSimpleName() : info.alias())
                .build());
    }

    @Override
    public boolean addService(ServiceInfo info) {
        // 获取bean
        Object bean = context.getBean(info.getClz());
        String key = info.getServiceName() + ":" + info.getVersion();
        if (serviceInfoMap.putIfAbsent(key, info) == null) {
            log.info("common grpc addService:{}", info);
            // 缓存bean对象
            instanceMap.put(key, bean);
            return true;
        }
        return false;
    }

    @Override
    public Object getInstance(GrpcRequest request) {
        return instanceMap.get(request.getServiceName() + ":" + request.getVersion());
    }

    @Override
    public Collection<ServiceInfo> getServiceInfos() {
        return serviceInfoMap.values();
    }

}
