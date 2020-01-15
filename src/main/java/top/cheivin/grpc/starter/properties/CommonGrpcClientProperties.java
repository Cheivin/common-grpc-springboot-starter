package top.cheivin.grpc.starter.properties;

import top.cheivin.grpc.handle.DataFormat;

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

    /**
     * 超时时间
     */
    private int timeout = 30000;

    /**
     * 默认重试次数
     */
    private int retry = 3;

    /**
     * 负载均衡类型
     */
    private int loadBalanceType;

    /**
     * 数据序列化方式
     */
    private DataFormat dataFormat = DataFormat.JAVA_BYTES;

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

    public DataFormat getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(DataFormat dataFormat) {
        this.dataFormat = dataFormat;
    }
}
