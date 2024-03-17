package org.darkmindfx.httpserver;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ConnectionHandler implements Runnable {
    private Socket socket;
    private Thread trdHandler;

    DataInputStream inStream;

    DataOutputStream outStream;

    private UUID handlerId;


    public ConnectionHandler(Socket socket, UUID handlerId) throws IOException {
        this.socket = socket;
        this.handlerId = handlerId;

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

    private void handleRequest(HttpRequest request) {

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
