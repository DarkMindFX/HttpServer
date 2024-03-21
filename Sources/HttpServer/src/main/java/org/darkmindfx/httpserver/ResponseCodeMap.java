package org.darkmindfx.httpserver;

import javax.swing.*;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;

public class ResponseCodeMap {
    private static Dictionary<Integer, String> respHeaders;

    public static String getResponseHeader(Integer respCode) {
        if(respHeaders == null) {
            populateRespHeaders();
        }
        return respHeaders.get(respCode);
    }

    static private void populateRespHeaders() {
        respHeaders = new Hashtable<>();
        respHeaders.put(200, "200 OK");
        respHeaders.put(400, "400 BadRequest");
        respHeaders.put(401, "401 Unauthorized");
        respHeaders.put(401, "403 Forbidden");
        respHeaders.put(404, "404 NotFound");
        respHeaders.put(500, "500 FileNotFound");
    }

}
