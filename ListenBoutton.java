package ecouteur;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JButton;

import elementdeFrame.Liste;

public class ListenBoutton implements ActionListener{
    JButton audio;
    JButton video;
    JButton all;
    Liste li;
    public void setTousBoutton(JButton au,JButton vi, JButton a, Liste u){
        audio = au; video = vi; all = a; li = u;
    }
    public void actionPerformed(ActionEvent e){
        JButton but =(JButton) e.getSource();
        String choix = but.getText();
        int con = titre(choix);
        if(con==0){
            li.actionNow(but.getText());
        }
    }

    public int titre(String choix){
        if(choix.equalsIgnoreCase("audio")){
            audio.setBackground(Color.yellow);
            video.setBackground(Color.white);
            all.setBackground(Color.white);
            li.changerListe(0);
            return 1;
        }else if(choix.equalsIgnoreCase("video")){
            audio.setBackground(Color.white);
            video.setBackground(Color.yellow);
            all.setBackground(Color.white);
            li.changerListe(1);
            return 1;
        }else if(choix.equalsIgnoreCase("all")){
            audio.setBackground(Color.white);
            video.setBackground(Color.white);
            all.setBackground(Color.yellow);
            li.changerListe(2);
            return 1;
        }
        return 0;
    }
}