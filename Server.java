package socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
       ServerSocket listener = null;
       Socket socketOfServer = null;
       File dir = new File("D:\\music");
        ArrayList<String> all;
        ArrayList<String> audio;
        ArrayList<String> video;
        ArrayList<ArrayList<String>> tous; 

       public Server()throws Exception{
        audio = new ArrayList<String>(); video = new ArrayList<String>(); all = new ArrayList<String>(); tous = new ArrayList<ArrayList<String>>();
        addAudio(); addVideo(); addAll();  addTous(); 
        setConnexion();
       }

        public void setConnexion()throws Exception{
            try {
                listener = new ServerSocket(9998);
                System.out.println("Server is waiting to accept user...");
                while(true){
                socketOfServer = listener.accept();
                new Thread(new ServerAction(socketOfServer,tous)).start();
                }
            }catch (Exception e) {
                System.out.println(e);
            }     
        }

        public void addTous(){
            tous.add(audio);
            tous.add(video);
            tous.add(all);
          }
      
          public void addAudio(){
              if (!dir.exists() || !dir.isDirectory()) {
              System.out.println("Cannot find video source directory: " + dir);
              all = null; audio=null; video = null;
              System.out.println("fa aon|");
            }
            else{
            
            for (String file : dir.list(new FilenameFilter() {
              public boolean accept(File dir, String name) {
                if(name.endsWith(".mp3")){
                    
                  return true;
                }
                return false;
            }
        })) audio.add((file).replace("\\", "/").replaceAll(" ", "%20"));  
        System.out.println(audio.get(0));
        }
          }
      
          public void addVideo(){
            if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("Cannot find video source directory: " + dir);
            all = null; audio=null; video = null;
            }
      
            for (String file : dir.list(new FilenameFilter() {
              public boolean accept(File dir, String name) {
                if(name.endsWith(".mp4")) return true;
                return false;
              }
            })) video.add((file).replace("\\", "/").replaceAll(" ", "%20"));
        }
      
        public void addAll(){
          if (!dir.exists() || !dir.isDirectory()) {
          System.out.println("Cannot find video source directory: " + dir);
          all = null; audio=null; video = null;
          }
      
          for (String file : dir.list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
              if(name.endsWith(".mp4") || name.endsWith(".mp3")) return true;
              return false;
            }
          })) all.add((file).replace("\\", "/").replaceAll(" ", "%20"));
      }
       
}