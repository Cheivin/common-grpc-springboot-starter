package top.cheivin.grpc.starter.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.StringUtils;
import top.cheivin.grpc.annotation.GrpcService;

import java.util.*;

/**
 * 注解扫描，bean装配
 * user:cheivin
 * date:2019/12/18
 */
public class CommonGrpcServiceScannerRegistrar implements BeanFactoryAware, ImportBeanDefinitionRegistrar, ResourceLoaderAware {
    private static Logger log = LoggerFactory.getLogger(CommonGrpcServiceScannerRegistrar.class);

    private BeanFactory beanFactory;
    private ResourceLoader resourceLoader;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }


    private ClassPathBeanDefinitionScanner getScanner(BeanDefinitionRegistry registry) {
        return new ClassPathBeanDefinitionScanner(registry, false) {
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                // 只扫描独立的接口
                return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
            }
        };
    }


    static List<Object> gRpcServiceInfoBeanList = new ArrayList();

    public static List<Object> getgRpcServiceInfoBeanList() {
        return gRpcServiceInfoBeanList;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 获取路径扫描器
        ClassPathBeanDefinitionScanner scanner = getScanner(registry);
        scanner.setResourceLoader(this.resourceLoader);
        scanner.addIncludeFilter(new AnnotationTypeFilter(GrpcService.class));

        // 获取扫描包路径
        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(ComponentScan.class.getName());
        if (annotationAttributes == null) {
            log.warn("annotationAttributes is null");
            return;
        }
        String[] basePackages = (String[]) annotationAttributes.get("basePackages");
        if (basePackages == null || basePackages.length == 0) {
            basePackages = new String[]{importingClassMetadata.getClassName().substring(0, importingClassMetadata.getClassName().lastIndexOf('.'))};
        }

        log.info("common grpc scan packages:{}",basePackages);

        // 遍历扫描包
        Set<BeanDefinition> beanDefinitionSet = new HashSet<>();
        Arrays.stream(basePackages).forEach(pack -> beanDefinitionSet.addAll(scanner.findCandidateComponents(pack)));
        for (BeanDefinition beanDefinition : beanDefinitionSet) {
            if (beanDefinition instanceof AnnotatedBeanDefinition) {
                String className = beanDefinition.getBeanClassName();
                if (StringUtils.isEmpty(className)) {
                    continue;
                }
                // 创建代理类
                try {
                    Class<?> target = Class.forName(className);
                    if (target.isAnnotation()) {
                        log.warn("is annotation break:{}", className);
                        continue;
                    }
                    try {
                        // 能获取到bean说明有实现类，为服务提供者
                        Object bean = beanFactory.getBean(target);
                        log.info("find service provider with bean:{}", bean);
                        gRpcServiceInfoBeanList.add(bean);
                    } catch (BeansException e) {
                        // 不能获取bean则为服务消费者，生成代理对象调用远程服务
                        DefaultListableBeanFactory factory = (DefaultListableBeanFactory) beanFactory;
                        InvocationHandler invocationHandler = new GrpcServiceClientProxy<>(target, factory);
                        Object proxy = Proxy.newProxyInstance(target.getClassLoader(), new Class[]{target}, invocationHandler);
                        //将其注册进容器中
                        factory.registerSingleton(className, proxy);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

    }


}
