package org.darkmindfx.httpserver;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {

        try
        {
            String cfgFile = args[0];
            if(cfgFile != null) {
                System.out.println(String.format("Using config: %s", cfgFile));
                FileInputStream fis = new FileInputStream(cfgFile);
                Properties props = new Properties();
                props.load(fis);

                ResourceCache resCache = new ResourceCache(props);

                PortListener listener = new PortListener(props, resCache);
                listener.listen();
            }
            else {
                System.out.println("ERROR: config file was not provided");
            }

        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }

    }
}