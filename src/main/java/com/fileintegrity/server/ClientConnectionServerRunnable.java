package com.fileintegrity.server;

import java.io.*;
import java.net.*;


public class ClientConnectionServerRunnable implements Runnable {
    String name = null;
    Socket link = null;
    

    public ClientConnectionServerRunnable(String name, Socket link){
        this.name = name;
        this.link = link;
    }

    @Override
    public void run() {
        // Implementation for handling client connection
        try{
            BufferedReader input = new BufferedReader(new InputStreamReader(link.getInputStream()));
            PrintWriter output = new PrintWriter(link.getOutputStream(), true);
            //reads the text from the buffered reader
            String message = input.readLine();
            //prints message to consoloe
            System.out.println("Message received from: " + name + ": " + message);
            //sends message back to client
            output.println("Echo message: " + message);


        }
        catch(IOException e){
            e.printStackTrace();
        }
        //no matter the outcome of try/catch, we close the connection to the client
        finally{
            try{
                System.out.println("Closing connectiong for client:" + name);
                link.close();
            }
            catch(IOException e){
                System.out.println("Unable to disconnect client: " + name);
                System.exit(1);
            }
        }
    }
}
