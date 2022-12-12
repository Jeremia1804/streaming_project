package elementdeFrame;
import javax.swing.*;
import java.awt.Color;
import java.awt.GridLayout;
import java.io.*;
import java.util.*;
import ecouteur.*;
import socket.ClientAction;

import javax.swing.*;
public class Liste extends JPanel{
  ArrayList<ArrayList<String>> tous= null; 
  int choix=2; 
  ListenBoutton listener;
  JButton audios;
  JButton videos;
  JButton alls;
  JScrollPane jsp;
  JPanel paneli;
  JButton[] but;
  ClientAction clientAction;



    public Liste(ArrayList<ArrayList<String>> lis, ClientAction c){
      clientAction = c;
      tous = lis;
      paneli = new JPanel();
      audios = new JButton("Audio");videos = new JButton("Video");alls = new JButton("All");
      listener = new ListenBoutton();
      listener.setTousBoutton(audios,videos,alls,this);
      audios.addActionListener(listener); videos.addActionListener(listener); alls.addActionListener(listener);
        setBounds(950,0,400,800);
        setLayout(null);
        jsp = manamboatra();
        add(choix()); add(jsp);
    }

    public ClientAction getClientAction(){
      return clientAction;
    }

    public void actionNow(String hira){
        clientAction.playingVideo(hira);
    }

    public void changerListe(int e){
      choix = e;
      for(int i=0; i<but.length; i++){
        paneli.remove(but[i]);
        paneli.validate();
      }
      prepare();
    }

      public JPanel choix(){
          JPanel panel = new JPanel();
          panel.setBounds(10,30,380,20);
          panel.setLayout(new GridLayout(1,3));
          panel.setBackground(Color.white);
          
          audios.setBackground(Color.white);
          videos.setBackground(Color.white);
          alls.setBackground(Color.yellow);
          audios.setBorder(BorderFactory.createEmptyBorder());
          videos.setBorder(BorderFactory.createEmptyBorder());
          alls.setBorder(BorderFactory.createEmptyBorder());
          panel.add(audios); panel.add(videos); panel.add(alls);
    
          return panel;
      }
    
      public void prepare(){
          paneli.setBounds(10,50,380,700);
          paneli.setBackground(Color.white);
          ArrayList<String> ito =(ArrayList<String>) tous.get(choix);
          but = new JButton[ito.size()];
          for(int i=0 ; i<ito.size();i++){
            but[i] = new JButton(ito.get(i));
            but[i].setFocusPainted(false);
            but[i].setBackground(Color.white);
            //   but.setBorder(BorderFactory.createEmptyBorder());
            but[i].setVerticalTextPosition(SwingConstants.TOP);
            but[i].setHorizontalAlignment(SwingConstants.LEFT);
            but[i].addActionListener(listener);
            paneli.add(but[i]);
          }
          paneli.setLayout(new GridLayout(ito.size(),1));
      }

        public JScrollPane manamboatra(){
          prepare();
          int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
          int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS;
            JScrollPane jspe = new JScrollPane(paneli,v,h);
            jspe.setBounds(10,50,380,500);
          return jspe;
      }
}