package org.darkmindfx.httpserver;

public class HttpRequest {

    private RequestHeader header;

    private byte[] body;

    public HttpRequest(RequestHeader header) {
        this.setHeader(header);
    }

    public RequestHeader getHeader() { return this.header; }

    public void setHeader(RequestHeader header) { this.header = header; }

    public byte[] getBody() { return this.body; }

    public void setBody(byte[] body) { this.body = body; }
}
