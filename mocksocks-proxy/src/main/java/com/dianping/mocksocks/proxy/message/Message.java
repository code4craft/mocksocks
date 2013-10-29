package com.dianping.mocksocks.proxy.message;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @author yihua.huang@dianping.com
 */
public class Message {

	private String protocol;

	public enum MessageType {
		Request, Response;
	}

	private ChannelBuffer channelBuffer;

	private MessageType type;

	public Message(ChannelBuffer channelBuffer, MessageType type) {
		this.channelBuffer = channelBuffer;
		this.type = type;
	}

	public String getProtocol() {
		return protocol;
	}

	public ChannelBuffer getChannelBuffer() {
		return channelBuffer;
	}

	public MessageType getType() {
		return type;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public void addChannelBuffer(ChannelBuffer channelBuffer) {
		this.channelBuffer = ChannelBuffers.wrappedBuffer(this.channelBuffer, channelBuffer);
	}

	public void setChannelBuffer(ChannelBuffer channelBuffer) {
		this.channelBuffer = channelBuffer;
	}

	public void setType(MessageType type) {
		this.type = type;
	}
}
