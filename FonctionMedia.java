package fonction_socket;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Vector;

public class FonctionMedia{
    public FonctionMedia(){

    }

    public byte[] getHisByte(File file) throws Exception {  
        byte[] bytes = null;
        try {
            Path pa = Paths.get(file.getAbsolutePath());
            bytes = Files.readAllBytes(pa);
            System.out.println(("Bytes of the file : " + bytes.length));
        } catch (Exception e) {
            throw e;
        }
        return bytes;
    }

    public void tranfertDuFichier(File file, ObjectOutputStream dataOut) throws IOException {
        Path path = Paths.get(file.getAbsolutePath());
        System.out.println(path);
        int divider = 10;
        byte[] bytes = Files.readAllBytes(path);
        if (bytes.length > 1000000) {
            divider += bytes.length / 70000;
        }
        ByteBuffer byteBuff = ByteBuffer.wrap(bytes);
        Vector<byte[]> bytesList = new Vector<byte[]>();
        int totalSend = 0;
        for (int i = 0; i < divider; i++) {
            if (i == divider - 1) {
                int tailleFinaux = bytes.length - totalSend;
                bytesList.add(new byte[tailleFinaux]);
                break;
            } else {
                bytesList.add(new byte[bytes.length / divider]);
            }
            totalSend += bytes.length / divider;
        }
        for (int i = 0; i < bytesList.size(); i++) {
            byteBuff.get(bytesList.get(i), 0, bytesList.get(i).length);
        }
        for (int i = 0; i < bytesList.size(); i++) {
            System.out.println("Sending " + i);
            dataOut.writeObject(bytesList.get(i));
        }
        System.out.println("File sended ");
    }

    public double getPourcentage(double valueMax, double percent) {
        // System.out.println(percent + "% of " + valueMax + " is = " + (valueMax *
        // percent) / 100);
        return (valueMax * percent) / 100;
    }

    public void copyingSocket(File file, int fullSize, ObjectInputStream dataIn, Thread th)
            throws ClassNotFoundException, IOException {
        System.out.println("Full size of the file " + fullSize);
        int bytesReceive = 0;
        int tour = 0;
        boolean notified = false;
        while (bytesReceive < fullSize) {
            Object obj = dataIn.readObject();
            if (obj instanceof byte[]) {
                byte[] bytes = (byte[]) obj;
                // System.out.println("Copying..");
                if (bytesReceive == 0) {
                    transfertByte(file, bytes, false);
                    bytesReceive += bytes.length;
                } else {
                    transfertByte(file, bytes, true);
                    bytesReceive += bytes.length;
                    if (notified == false && bytesReceive >= getPourcentage(fullSize, 25)) {
                        synchronized (th) {
                            th.notify();
                            notified = true;
                        }
                    }
                }
                 System.out.println("Copying..Taille du fichier " + bytesReceive);
            }
            tour++;
        }
        System.out.println("Copying Finish = " + file.getName());
    }

    public void transfertByte(File file, byte[] bytes, boolean append) throws IOException {
        OutputStream os = null;
        if (append) {
            os = new FileOutputStream(file, true);
        } else {
            os = new FileOutputStream(file);
        }
        os.write(bytes);
        os.close();
    }

}