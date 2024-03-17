package org.darkmindfx.httpserver;

import java.util.*;

public class RequestHeader {

    private EHttpMethod method = EHttpMethod.UNDEFINED;

    private String requestString;

    private String resource;

    private String httpVer;
    private Dictionary<String, String> headers;

    private Dictionary<String, String> requestParams;

    public RequestHeader(List<String> rawHeader) {
        parseRawHeader(rawHeader);
    }

    public EHttpMethod getMethod() { return this.method; }

    public void setMethod(EHttpMethod method) { this.method = method; }

    public String getResource() { return this.resource; }

    public void setResource(String resource) { this.resource = resource; }

    public String getRequestString() { return this.requestString; }

    public void setRequestString(String requestString) { this.requestString = requestString; }

    public Dictionary<String, String> getHeaders() { return this.headers; }

    public String getHttpVersion() { return this.httpVer; }

    public void setHttpVersion(String httVer) { this.httpVer = httVer; }

    public void setHeaders(Dictionary<String, String> headers) { this.headers = headers; }

    public Dictionary<String, String> getRequestParams() { return requestParams;  }

    public void setRequestParams(Dictionary<String, String> requestParams) { this.requestParams = requestParams; }

    private void parseRawHeader(List<String> rawHeader) {
        for(String line : rawHeader) {
            if(method == EHttpMethod.UNDEFINED) {

                String[] parts = line.split(" ");
                this.setMethod( EHttpMethod.valueOf(parts[0]) );
                this.setRequestString( parts[1] );
                this.setHttpVersion( parts[2] );

                this.extractResurce(this.requestString);

            }
            else {
                if(headers == null) {
                    headers = new Hashtable<String, String>();
                }
                String[] parts = line.split(": ");
                if(parts.length > 1) {
                    headers.put(parts[0].trim(), parts[1].trim());
                }
            }
        }
    }

    private void extractResurce(String requestString) {
        String[] parts = requestString.split("\\?");
        if(parts.length >= 1) {
            this.resource = parts[0];
            if(parts.length > 1) {
                extractRequestParam(parts[1]);
            }
        }
    }

    private void extractRequestParam(String rawParams) {
        Dictionary<String, String> reqParams = new Hashtable<String, String>();

        String[] parts = rawParams.split("&");
        for(int i = 0; i < parts.length; ++i) {
            String[] kv = parts[i].split("=");
            if(kv.length > 1) {
                reqParams.put(kv[0], java.net.URLDecoder.decode(kv[1]));
            }
        }

        this.setRequestParams(reqParams);
    }
}
