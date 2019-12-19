package top.cheivin.grpc.starter.annotation;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import top.cheivin.grpc.starter.configuration.CommonGrpcAutoConfiguration;
import top.cheivin.grpc.starter.configuration.CommonGrpcServiceScannerRegistrar;

import java.lang.annotation.*;

/**
 * 启动服务的注解
 * user:cheivin
 * date:2019/12/18
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Import({CommonGrpcServiceScannerRegistrar.class, CommonGrpcAutoConfiguration.class})
@ComponentScan
public @interface EnableScanGrpcService {
    @AliasFor(value = "basePackages", annotation = ComponentScan.class)
    String[] basePackages() default {};
}
