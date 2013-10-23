package com.dianping.mocksocks.proxy.monitor;

import org.jboss.netty.channel.Channel;

import java.util.Date;

/**
 * @author yihua.huang@dianping.com
 */
public class ConnectionStatus {

	private long startTime;

	private long endTime;

	private long bytesSend;

	private long bytesReceive;

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

	public long getBytesSend() {
		return bytesSend;
	}

	public synchronized void setBytesSend(long bytesSend) {
		this.bytesSend = bytesSend;
	}

	public synchronized void addBytesSend(long bytesSend) {
		this.bytesSend += bytesSend;
	}

	public long getBytesReceive() {
		return bytesReceive;
	}

	public synchronized void setBytesReceive(long bytesReceive) {
		this.bytesReceive = bytesReceive;
	}

	public synchronized void addBytesReceive(long bytesReceive) {
		this.bytesReceive += bytesReceive;
	}

	public String getStatus() {
		return status;
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
		return "ConnectionStatus{" + "startTime=" + new Date(startTime) + ", endTime=" + new Date(endTime)
				+ ", timeCost=" + (endTime>0?(endTime - startTime):0) + bytesSend + ", bytesSend=" + bytesSend + ", bytesReceive="
				+ bytesReceive + ", status='" + status + '\'' + ", remote=" + channel.getRemoteAddress() + '}';
	}
}
