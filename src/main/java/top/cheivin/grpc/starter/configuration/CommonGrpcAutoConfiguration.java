package top.cheivin.grpc.starter.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import top.cheivin.grpc.GrpcClient;
import top.cheivin.grpc.GrpcServer;
import top.cheivin.grpc.core.Discover;
import top.cheivin.grpc.core.Registry;
import top.cheivin.grpc.core.ServiceInfoManage;
import top.cheivin.grpc.handle.Caller;
import top.cheivin.grpc.handle.DefaultCaller;
import top.cheivin.grpc.handle.DefaultInvoker;
import top.cheivin.grpc.handle.Invoker;
import top.cheivin.grpc.starter.SpringServiceInfoManage;
import top.cheivin.grpc.starter.properties.CommonGrpcProperties;
import top.cheivin.grpc.zookeeper.ZkDiscover;
import top.cheivin.grpc.zookeeper.ZkRegistry;

import javax.annotation.Resource;

/**
 * 自动配置
 * user:cheivin
 * date:2019/12/18
 */
@EnableConfigurationProperties({CommonGrpcProperties.class})
public class CommonGrpcAutoConfiguration {
    private Logger log = LoggerFactory.getLogger(CommonGrpcAutoConfiguration.class);

    @Resource
    private CommonGrpcProperties properties;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = CommonGrpcProperties.PREFIX + ".server", name = "enabled", havingValue = "true")
    public ServiceInfoManage serviceInfoManage() {
        log.info("common grpc config server: ServiceInfoManage");
        return new SpringServiceInfoManage();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = CommonGrpcProperties.PREFIX + ".server", name = "enabled", havingValue = "true")
    public Registry registry() {
        log.info("common grpc config server: ZkRegistry");
        return new ZkRegistry(properties.getHost(), properties.getServer().getTimeout());
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = CommonGrpcProperties.PREFIX + ".server", name = "enabled", havingValue = "true")
    public Invoker invoker() {
        log.info("common grpc config server: Invoker");
        return new DefaultInvoker();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = CommonGrpcProperties.PREFIX + ".server", name = "enabled", havingValue = "true")
    public GrpcServer grpcServer(Registry registry, ServiceInfoManage serviceInfoManage) {
        return GrpcServer.from(registry, serviceInfoManage)
                .port(properties.getServer().getPort())
                .permitKeepAliveWithoutCalls(true)
                .build();
    }

    @Bean
    @DependsOn("grpcServer")
    @ConditionalOnProperty(prefix = CommonGrpcProperties.PREFIX + ".server", name = "enabled", havingValue = "true")
    public CommonGrpcServerRunner commonGrpcServerRunner() {
        return new CommonGrpcServerRunner();
    }


    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = CommonGrpcProperties.PREFIX + ".client", name = "enabled", havingValue = "true")
    public Discover discover() {
        return new ZkDiscover(properties.getHost(), properties.getClient().getTimeout(), properties.getClient().getLoadBalanceType());
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = CommonGrpcProperties.PREFIX + ".client", name = "enabled", havingValue = "true")
    public Caller caller() {
        log.info("common grpc config client: Caller");
        return new DefaultCaller(properties.getClient().getRetry());
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = CommonGrpcProperties.PREFIX + ".client", name = "enabled", havingValue = "true")
    public GrpcClient grpcClient(Discover discover, Caller caller) {
        log.info("common grpc config client: GrpcClient");
        return new GrpcClient(discover, caller, properties.getClient().getRetry());
    }
}
