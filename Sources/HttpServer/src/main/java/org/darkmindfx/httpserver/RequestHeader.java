package org.darkmindfx.httpserver;

import java.util.*;

public class RequestHeader {

    private EHttpMethod method = EHttpMethod.UNDEFINED;

    private String resource;

    private String httpVer;
    private Dictionary<String, String> params;

    public RequestHeader(List<String> rawHeader) {
        parseRawHeader(rawHeader);
    }

    private void parseRawHeader(List<String> rawHeader) {
        for(String line : rawHeader) {
            if(method == EHttpMethod.UNDEFINED) {

                String[] parts = line.split(" ");
                method = EHttpMethod.valueOf(parts[0]);
                resource = parts[1];
                httpVer = parts[2];

            }
            else {
                if(params == null) {
                    params = new Hashtable<String, String>();
                }
                String[] parts = line.split(": ");
                if(parts.length > 1) {
                    params.put(parts[0].trim(), parts[1].trim());
                }
            }
        }
    }
}
