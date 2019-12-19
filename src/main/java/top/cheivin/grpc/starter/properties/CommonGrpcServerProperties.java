package top.cheivin.grpc.starter.properties;

/**
 * 服务端配置
 * user:cheivin
 * date:2019/12/18
 */
public class CommonGrpcServerProperties {
    /**
     * 是否启用服务
     */
    private boolean enabled;
    /**
     * 服务端口
     */
    private int port = 29999;

    private int timeout = 30000;

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
