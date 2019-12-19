package top.cheivin.grpc.starter.properties;

/**
 * 客户端配置
 * user:cheivin
 * date:2019/12/18
 */
public class CommonGrpcClientProperties {
    /**
     * 是否启用
     */
    private boolean enabled;

    private int timeout = 30000;

    private int retry = 3;

    private int loadBalanceType;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }

    public int getLoadBalanceType() {
        return loadBalanceType;
    }

    public void setLoadBalanceType(int loadBalanceType) {
        this.loadBalanceType = loadBalanceType;
    }
}
