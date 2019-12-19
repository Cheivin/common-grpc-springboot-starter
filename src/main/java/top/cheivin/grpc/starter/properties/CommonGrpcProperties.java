package top.cheivin.grpc.starter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 配置bean
 * user:cheivin
 * date:2019/12/18
 */
@ConfigurationProperties(prefix = CommonGrpcProperties.PREFIX)
public class CommonGrpcProperties {
    public static final String PREFIX = "common-grpc";

    /**
     * 注册发现中心地址
     */
    private String host = "localhost";
    /**
     * 扫描包
     */
    //private String[] basePackages;
    /**
     * 服务信息
     */
    private CommonGrpcServerProperties server = new CommonGrpcServerProperties();
    /**
     * 客户端信息
     */
    private CommonGrpcClientProperties client = new CommonGrpcClientProperties();

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public CommonGrpcServerProperties getServer() {
        return server;
    }

    public void setServer(CommonGrpcServerProperties server) {
        this.server = server;
    }
/*

    public String[] getBasePackages() {
        return basePackages;
    }

    public void setBasePackages(String[] basePackages) {
        this.basePackages = basePackages;
    }
*/

    public CommonGrpcClientProperties getClient() {
        return client;
    }

    public void setClient(CommonGrpcClientProperties client) {
        this.client = client;
    }
}

