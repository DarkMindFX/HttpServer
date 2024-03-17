package org.darkmindfx.httpserver;

import java.net.*;
import java.io.*;
import java.util.Properties;
import java.util.UUID;


public class PortListener {

    private int port = 8081;
    private boolean isRunning = true;

    private Properties serverProperties;

    public PortListener(Properties props) {
        this.serverProperties = props;
        this.port = Integer.parseInt(props.getProperty("SERVER_PORT"));
    }

    public void listen() throws IOException {

        System.out.println(String.format("Listening port %d", this.port));

        ServerSocket serverSocket = new ServerSocket(this.port);

        while(this.isRunning) {
            Socket socket = serverSocket.accept();

            System.out.println("New connection received");

            ConnectionHandler connHandler = new ConnectionHandler(socket, this.serverProperties, UUID.randomUUID());
        }
    }

    public boolean isRunning() { return this.isRunning; }

    public void setRunning(boolean isRunning) { this.isRunning = isRunning; }
}
