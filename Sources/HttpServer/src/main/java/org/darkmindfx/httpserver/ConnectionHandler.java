package org.darkmindfx.httpserver;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConnectionHandler implements Runnable {
    private Socket socket;
    private Thread trdHandler;

    public ConnectionHandler(Socket socket) throws IOException {
        this.socket = socket;

        trdHandler = new Thread(this);
        trdHandler.start();
    }

    @Override
    public void run() {

        try {
            List<String> rawHeader = readHeader();
            RequestHeader header = new RequestHeader(rawHeader);
        }
        catch(Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    private List<String> readHeader() throws IOException {
        DataInputStream inStream = new DataInputStream(socket.getInputStream());
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


}
