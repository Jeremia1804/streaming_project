package socket;
import java.io.*;
import java.net.*;
import java.util.*;
import fenetre.*;

public class Client {
       Socket socketOfClient = null;
       ObjectOutputStream obb;
       ObjectInputStream ob;
       String serverHost = "localhost";
      

       public Client()throws Exception{
        seConnecter();
       }

    public void seConnecter()throws Exception{
        try {
            socketOfClient = new Socket(serverHost, 9998);
            new Thread(new ClientAction(socketOfClient)).start();
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + serverHost);
            return;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }
    }


}