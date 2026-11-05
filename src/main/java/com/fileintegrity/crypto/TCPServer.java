package com.fileintegrity.crypto;

import java.io.*;
import java.net.*;

public class TCPServer {

    private static ServerSocket servSocket;
    private static final int PORT = 1234;
    private static int clientCount = 0;

    public static void main(String[] args){
        //try/catch block to open port
        System.out.println("Opening port.../n");
        try{
            servSocket = new ServerSocket(PORT);
            System.out.println("Server started listeing on port: " + PORT);

        }
        catch(IOException e){
            System.out.println("Unable to attach to port: " + PORT);
            System.exit(1);
        }
        //this is to keep the server running unless the user closes
        do{
            run();
        }while(true);
    }
        // in our run(), we will accept new connections on the server socket, count how many connections and run a thread for each client connection 
    private static void run(){
        Socket link = null;
        try{
            link = servSocket.accept();
            clientCount++;
            Runnable resource = new ClientConnectionServerRunnable("Client: " + clientCount,link);
        Thread t = new Thread(resource);
        t.start();


        }
        catch(IOException e){
            e.printStackTrace();
            try{
                System.out.println("Closing connection...");
                servSocket.close();
            }
            catch(IOException e2){
                System.out.println("Unable to disconnect");
                System.exit(1);
            }
        }
        


    }


    
}
