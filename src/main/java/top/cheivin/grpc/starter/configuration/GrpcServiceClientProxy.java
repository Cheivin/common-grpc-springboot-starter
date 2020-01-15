package top.cheivin.grpc.starter.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.cglib.proxy.InvocationHandler;
import top.cheivin.grpc.GrpcClient;
import top.cheivin.grpc.annotation.GrpcService;
import top.cheivin.grpc.core.GrpcRequest;
import top.cheivin.grpc.exception.InvokeException;

import java.lang.reflect.Method;

/**
 * 接口代理类，用于向远程服务发起调用
 * user:cheivin
 * date:2019/12/18
 */
public class GrpcServiceClientProxy<T> implements InvocationHandler {
    private static final Logger log = LoggerFactory.getLogger(GrpcServiceClientProxy.class);

    private GrpcClient client;
    private BeanFactory beanFactory;
    private GrpcRequest requestTemplate;

    public GrpcServiceClientProxy(Class<T> serviceClass, BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        requestTemplate(serviceClass);
    }

    private void requestTemplate(Class<T> serviceClass) {
        GrpcService annotation = serviceClass.getAnnotation(GrpcService.class);
        GrpcRequest request = new GrpcRequest();
        request.setServiceName(annotation.service());
        request.setVersion(annotation.version());
        this.requestTemplate = request;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws InvokeException {
        GrpcRequest request = new GrpcRequest();
        request.setServiceName(this.requestTemplate.getServiceName());
        request.setVersion(this.requestTemplate.getVersion());
        request.setMethodName(method.getName());
        request.setArgs(args);
        if (client == null) {
            setClient();
        }
        return client.invoke(request, method.getReturnType());
    }

    // 缓存client
    private synchronized void setClient() {
        if (client == null) {
            client = beanFactory.getBean(GrpcClient.class);
        }
    }


}
