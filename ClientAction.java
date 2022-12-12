package socket;
import java.io.*;
import java.net.Socket;
import java.util.Vector;
import java.util.*;
import fonction_socket.*;
import video.*;
import fenetre.*;
public class ClientAction implements Runnable{
    Socket socket=null;
    ObjectOutputStream ob;
    ObjectInputStream obe;
    ArrayList<ArrayList<String>> tous = null;
    Windows win;
    SceneGenerator scene;
    FonctionMedia fonc;
    Thread threadP = Thread.currentThread();
    File file;
    Thread thT;
    Thread tu;

    public ClientAction(Socket sockete)throws Exception{
        this.socket = sockete;
        win = new Windows(this);
        fonc = new FonctionMedia();
        scene = win.getSceneGenerator();
        ob = new ObjectOutputStream(socket.getOutputStream());
        obe = new ObjectInputStream(socket.getInputStream());
        ArrayList<ArrayList<String>> lis =(ArrayList<ArrayList<String>>) obe.readObject();
        tous = lis;
        tsyHaiko(tous);
    }

    public void playingVideo(String hira){
        if(thT!=null){
            thT.stop();
            tu.stop();
            thT = null;
            tu=null;
            System.out.println("null kay le izy");
        }
        String music = hira;
        String extension = ".mp3";
        if(music.endsWith(".mp4")) extension = ".mp4";
        String pa = "./repository/test"+extension;
        try {
            ob.writeObject(music);
            ob.flush();
            System.out.println("READING INT AUDIO ");
            int tailleFile = obe.readInt();
            System.out.println("Size of the File :" + tailleFile);
            System.out.println("AUDIOOOO");
            thT = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // audio.getMyFile()
                        fonc.copyingSocket(file = new File(pa), tailleFile, obe, threadP);
                        
                    } catch (ClassNotFoundException | IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            thT.start();
        } catch (Exception e1) {
            System.out.println(e1);
        }
             
        synchronized (threadP) {
            try {
                threadP.wait();
                System.out.println("Synchronized---MP4");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("GO video");
        tu = new Thread(new Runnable() {
            public void run(){
                scene.alefaso(file);
            }
        });

        tu.start();

    }

    public void run(){
        try{
            traiterInformation();
        }catch(Exception e){}
    }

    public void traiterInformation()throws Exception{
        FonctionMedia fonc =new FonctionMedia();
    }

     public void tsyHaiko(ArrayList<ArrayList<String>> lis){
                win.tsyHaiko(lis);
    }
}