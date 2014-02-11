package com.dianping.mocksocks.transport;

import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * @author yihua.huang@dianping.com
 */
public class Message {

	public static final int BYTES_PER_LINE = 16;
	private String protocol;
	private Charset charset = Charset.defaultCharset();

	public enum MessageType {
		Request, Response;
	}

	private ChannelBuffer channelBuffer;

	private MessageType type;

	public Message(ChannelBuffer channelBuffer, MessageType type) {
		this.channelBuffer = channelBuffer;
		this.type = type;
	}

	public Message(ChannelBuffer channelBuffer, MessageType type, String protocol) {
		this.protocol = protocol;
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

	@Override
	public String toString() {
		return "Message{" + "protocol='" + protocol + '\'' + ", channelBuffer="
				+ StringUtils.substringBefore(textOutput(), "\n") + ", type=" + type + '}';
	}

	public String textOutput() {
		return channelBuffer.toString("utf-8");
	}

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	public String hexOutput() {
		StringBuilder accum = new StringBuilder();
		int size = channelBuffer.readableBytes();
		int lineCount = (size / BYTES_PER_LINE) + 1;
		ByteBuffer byteBuffer = ByteBuffer.allocate(BYTES_PER_LINE);
		for (int i = 0; i < lineCount; i++) {
			byteBuffer.clear();
			int length = size - i * BYTES_PER_LINE;
			if (length > BYTES_PER_LINE) {
				length = BYTES_PER_LINE;
			}
			accum.append(StringUtils.leftPad(Integer.toHexString(0xff & i), 4, "0").toUpperCase() + " ");
			for (int j = 0; j < length; j++) {
				byte b = channelBuffer.readByte();
				byteBuffer.put(b);
				accum.append(StringUtils.leftPad(Integer.toHexString(0xff & b), 2, "0").toUpperCase() + " ");
			}
			if (length < BYTES_PER_LINE) {
				for (int j = 0; j < BYTES_PER_LINE - length; j++) {
					accum.append("   ");
				}
			}
			byteBuffer.flip();
			accum.append(charset.decode(byteBuffer));
			accum.append("\n");
		}
		return accum.toString();
	}
}
