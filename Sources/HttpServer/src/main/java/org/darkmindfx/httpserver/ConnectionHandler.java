package org.darkmindfx.httpserver;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class ConnectionHandler implements Runnable {
    private Socket socket;
    private Thread trdHandler;

    DataInputStream inStream;

    DataOutputStream outStream;

    private Properties serverProperties;

    private UUID handlerId;

    private ResourceCache cache;


    public ConnectionHandler(Socket socket, Properties serverProperties, ResourceCache cache, UUID handlerId) throws IOException {
        this.socket = socket;
        this.handlerId = handlerId;
        this.serverProperties = serverProperties;
        this.cache = cache;

        trdHandler = new Thread(this);
        trdHandler.start();
    }

    @Override
    public void run() {

        try {

            this.inStream = new DataInputStream(socket.getInputStream());
            this.outStream = new DataOutputStream(socket.getOutputStream());

            List<String> rawHeader = readHeader();
            RequestHeader header = new RequestHeader(rawHeader);

            HttpRequest request = new HttpRequest(header);

            if(header.getMethod() == EHttpMethod.POST || header.getMethod() == EHttpMethod.PUT) {
                request.setBody(readPayload(header));
            }

            handleRequest(request);
        }
        catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void handleRequest(HttpRequest request) throws IOException {
        int respCode = 500;
        byte[] content = null;
        try {
            content = cache.getResourceContent(request.getHeader().getResource());

            respCode = 200;
        }
        catch (FileNotFoundException exNotFound) {
            respCode = 404;
        }
        catch (Exception ex) {
            respCode = 500;
        }

        sendResponse(respCode, content);

        outStream.flush();
        outStream.close();
        inStream.close();
        this.socket.close();
    }

    private void sendResponse(int respCode, byte[] content) throws IOException {
        StringBuffer sbResponse = new StringBuffer();
        sbResponse.append("HTTP/1.1 ");
        switch (respCode) {
            case 200:
                sbResponse.append("200 OK");
                break;
            case 404:
                sbResponse.append("404 NotFound");
                break;
            case 500:
                sbResponse.append("500 InternalServerError");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + respCode);
        }
        sbResponse.append("\n");
        sbResponse.append("Server: DarkMindFX.HttpServer\n");
        sbResponse.append("Content-Type: text/html\n");
        if(content != null && content.length > 0) {
            sbResponse.append( String.format("Content-Length: %d\n", content.length));
        }
        sbResponse.append("\n");

        outStream.write(sbResponse.toString().getBytes());
        if(content != null && content.length > 0) {
            outStream.write(content);
        }


    }

    private List<String> readHeader() throws IOException {

        boolean endOfReqFound = false;
        List<String> header = new ArrayList<String>();
        do {
            String line = inStream.readLine();
            if(!line.isEmpty()) {
                header.add(line);
            }
            else {
                endOfReqFound = true;
            }

        } while(!endOfReqFound);

        return header;
    }

    private byte[] readPayload(RequestHeader header) throws IOException  {
        int contentLength = Integer.parseInt(header.getHeaders().get("Content-Length"));
        byte[] payload = null;
        this.inStream.read(payload, 0, contentLength);

        return payload;
    }


}
