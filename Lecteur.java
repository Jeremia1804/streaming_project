package video;
import java.io.*;
import java.util.*;
import javafx.application.Platform;
import javafx.beans.value.*;
import javafx.embed.swing.JFXPanel;
import javafx.event.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.media.*;
import javafx.util.Duration;

public class Lecteur extends JFXPanel{
  SceneGenerator sceneGenerator;
    public Lecteur() {
      sceneGenerator = new SceneGenerator();
        // This method is invoked on Swing thread  
        Platform.runLater(new Runnable() {
          public void run() {
            initFX();        
          }
        });
      }
    
      public void initFX() {
    // This method is invoked on JavaFX thread
        Scene scener = sceneGenerator.createScene();
        this.setScene(scener);
      }

      public SceneGenerator getSceneGenerator(){
        return sceneGenerator;
      }
    
}


