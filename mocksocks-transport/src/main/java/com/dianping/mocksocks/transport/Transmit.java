package com.dianping.mocksocks.transport;

/**
 * @author yihua.huang@dianping.com
 */
public class Transmit {

    private Message request;

    private Message response;

    public Message getRequest() {
        return request;
    }

    public void setRequest(Message request) {
        this.request = request;
    }

    public Message getResponse() {
        return response;
    }

    public void setResponse(Message response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "Transmit{" +
                "request=" + request +
                ", response=" + response +
                '}';
    }
}
