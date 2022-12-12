package fenetre;
import javax.swing.*;
import java.awt.*;
import elementdeFrame.*;
import socket.ClientAction;

import java.util.*;
import video.*;

public class Windows extends JFrame{
  Liste panelListe;
  Lecteur lecture;
  ClientAction clientAction;
  SceneGenerator sceneGenerator;
    public Windows(ClientAction c){
      setClientAction(c);
      lecture = new Lecteur();
      setSceneGenerator(lecture.getSceneGenerator());
    }

    public void setClientAction(ClientAction c){
      clientAction = c;
    }

    public ClientAction getClientAction(){
      return clientAction;
    }

    public void setSceneGenerator(SceneGenerator scf ){
        sceneGenerator = scf;
    }

    public SceneGenerator getSceneGenerator(){
      return sceneGenerator;
    }

    public void initAndShowGUI(ArrayList<ArrayList<String>> lis) {
    // This method is invoked on Swing thread
    
        JPanel panel = new JPanel();
        panel.setBackground(Color.white);
        JPanel panelLecteur = new JPanel();
        panelLecteur.setBackground(Color.white);
        panelListe = new Liste(lis,clientAction);
         panelListe.setBackground(Color.white);
        panel.setLayout(null);
        panel.add(panelLecteur); panel.add(panelListe);
        panelLecteur.setBounds(0,0,800,800);
        panelLecteur.add(lecture);
        this.add(panel);
        this.setBounds(200, 100, 800, 250);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);

  }

  public void tsyHaiko(ArrayList<ArrayList<String>> lis){
    SwingUtilities.invokeLater(new Runnable() {
        @Override public void run() {
          initAndShowGUI(lis);
        }
      });
}

}