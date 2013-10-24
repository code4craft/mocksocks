package com.dianping.mocksocks.proxy.monitor;

import org.jboss.netty.channel.Channel;

import java.net.InetSocketAddress;

/**
 * @author yihua.huang@dianping.com
 */
public class ConnectionStatus {

	private long startTime;

	private long endTime;

	private int bytesSend;

	private int bytesReceive;

	private final Channel channel;

	private String status;

	public static final String SUCCESS = "success";

	public static final String FAIL = "fail";

	public ConnectionStatus(Channel channel) {
		this.channel = channel;
		this.startTime = System.currentTimeMillis();
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public int getBytesSend() {
		return bytesSend;
	}

	public synchronized void setBytesSend(int bytesSend) {
		this.bytesSend = bytesSend;
	}

	public synchronized void addBytesSend(long bytesSend) {
		this.bytesSend += bytesSend;
	}

	public int getBytesReceive() {
		return bytesReceive;
	}

	public synchronized void setBytesReceive(int bytesReceive) {
		this.bytesReceive = bytesReceive;
	}

	public synchronized void addBytesReceive(long bytesReceive) {
		this.bytesReceive += bytesReceive;
	}

	public String getStatus() {
		return status;
	}

    public InetSocketAddress getAddress(){
        return (InetSocketAddress)channel.getRemoteAddress();
    }

	public ConnectionStatus setStatus(String status) {
		this.status = status;
		return this;
	}

	public ConnectionStatus close() {
		this.status = "closed";
		endTime = System.currentTimeMillis();
		return this;
	}

	@Override
	public String toString() {
		return "ConnectionStatus{remote=" + channel.getRemoteAddress() + ",startTime="
				+ StatusFormatter.formatTimeStamp(startTime) + ", endTime=" + StatusFormatter.formatTimeStamp(endTime)
				+ ", timeCost=" + (endTime > 0 ? StatusFormatter.formatTimePeriod(endTime - startTime) : 0)
				+ ", bytesSend=" + StatusFormatter.formatBytes(bytesSend) + ", bytesReceive="
				+ StatusFormatter.formatBytes(bytesReceive) + ", status=" + status + '}';
	}
}
