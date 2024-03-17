package org.darkmindfx.httpserver;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        try
        {
            PortListener listener = new PortListener(8081);
            listener.listen();

        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }

    }
}