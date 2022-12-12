package socket;
import java.io.*;
import java.net.Socket;
import java.util.Vector;
import java.util.*;
import fonction_socket.*;
public class ServerAction implements Runnable{
    Socket socket=null;
    ObjectOutputStream ob;
    ObjectInputStream obe;
    ArrayList<ArrayList<String>> tous = null;

    public ServerAction(Socket sockete,ArrayList<ArrayList<String>> all)throws Exception{
        this.socket = sockete;
        tous = all;
        ob = new ObjectOutputStream(socket.getOutputStream());
        obe = new ObjectInputStream(socket.getInputStream());
    }

    public void run(){
        try{
            ob.writeObject(tous);
            traiterInformation();
        }catch(Exception e){}
    }

    public void traiterInformation()throws Exception{
        FonctionMedia fonc =new FonctionMedia();
        String info = "";
        while(true){
        info = (String) obe.readObject();
            if(info.endsWith(".mp3") || info.endsWith(".mp4")){
                String nameFile = "D:\\music\\" + info;
                byte[] bytes = fonc.getHisByte(new File(nameFile));
                ob.writeInt(bytes.length);
                fonc.tranfertDuFichier(new File(nameFile), ob);
            }else{

            }
        }
    }
}