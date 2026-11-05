package com.fileintegrity.crypto;

import java.io.*;
import java.net.*;

public class TCPEchoClient {
    private static InetAddress host;
    private static final int PORT = 1234;

    //main method where we get the local host and run the client connection
    public static void main(String[] args){
        try{
            host = InetAddress.getLocalHost();
        }
        catch(UnknownHostException e){
            System.out.println("Host ID not found!");
            System.exit(1);
        }
        run();

    }
    //run method where we create a socket and connect to server
    private static void run(){
        Socket link = null;
        //try/catch to create a socket and connect to the server and add input and output for read/write
        try{
            link = new Socket(host, PORT);
            BufferedReader input = new BufferedReader(new InputStreamReader(link.getInputStream()));
            PrintWriter output = new PrintWriter(link.getOutputStream(), true);

            //set up stream to send message to server
            BufferedReader userEntry = new BufferedReader(new InputStreamReader(System.in));
            String message = null;
            String response = null;

            System.out.println("Enter message to send to server..");
            message = userEntry.readLine();
            //send message to server
            output.println(message);
            //get response from server
            response = input.readLine();
            System.out.println("SERVER RESPONSE>> " + response);


        }
        catch(IOException e){
            e.printStackTrace();
        }
        finally{
            try{
                System.out.println("Closing connection...");
                link.close();
            }
            catch(IOException e){
                System.out.println("Unable to disconnect");
                System.exit(1);
            }
        }
    }
    
}
