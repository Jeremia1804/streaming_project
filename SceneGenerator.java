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

public class SceneGenerator {    
    final Label currentlyPlaying = new Label();
    final Label currentlyTimePlaying = new Label();
    final ProgressBar progress = new ProgressBar(0.4);
    Slider slider = new Slider();
    Button play;
    MediaView mediaView;
    ImageView playing;
    ImageView pauser;
    private ChangeListener<Duration> progressChangeListener;

    public void alefaso(File file){
      List<MediaPlayer> players = new ArrayList<MediaPlayer>();
      final File dir = new File("D:\\ITU\\L2\\JAVA\\projet_streaming_1804\\repository");
      System.out.println("tong ve");
      for (String files : dir.list(new FilenameFilter() {
        public boolean accept(File dir, String name) {
          System.out.println("ireoty aauh: " + name);
          if(name.endsWith(".mp4") || name.endsWith(".mp3")) return true;
          return false;
        }
      })) players.add(createPlayer("file:///" + (dir + "\\" + files).replace("\\", "/").replaceAll(" ", "%20")));

      play.setGraphic(pauser);
      mediaView.getMediaPlayer().stop();
      players.get(0).currentTimeProperty().removeListener(progressChangeListener);
      mediaView.setMediaPlayer(players.get(0));
      mediaView.getMediaPlayer().setVolume(slider.getValue()/100);
      System.out.println(file.getAbsolutePath());
      mediaView.getMediaPlayer().play();
    }
  
    public Scene createScene() {
      final VBox layout = new VBox();
  
      // determine the source directory for the playlist
      final File dir = new File("D:\\music");
      if (!dir.exists() || !dir.isDirectory()) {
        System.out.println("Cannot find video source directory: " + dir);
        Platform.exit();
        return null;
      }
  
      // create some media players.
      final List<MediaPlayer> players = new ArrayList<MediaPlayer>();
      for (String file : dir.list(new FilenameFilter() {
        public boolean accept(File dir, String name) {
          if(name.endsWith(".mp4") || name.endsWith(".mp3")) return true;
          return false;
        }
      })) players.add(createPlayer("file:///" + (dir + "\\" + file).replace("\\", "/").replace(".\repository", "").replaceAll(" ", "%20")));
      if (players.isEmpty()) {
        System.out.println("No audio found in " + dir);
        Platform.exit();
        return null;
      }
  
      // create a view to show the mediaplayers.
      int he = 20;
      mediaView = new MediaView(players.get(1));
      playing = new ImageView(new Image("play.png"));
      pauser = new ImageView(new Image("pause.png"));
      ImageView next = new ImageView(new Image("next.png"));
      ImageView stop = new ImageView(new Image("stop.png"));
      ImageView volumes = new ImageView(new Image("volume.png"));
      ImageView music = new ImageView(new Image("music.png"));
      final Button skip = new Button();
      play = new Button();
      final Button restart = new Button();
      final Button volume = new Button();
      play.setGraphic(playing);
      skip.setGraphic(next);
      restart.setGraphic(stop);
      volume.setGraphic(volumes);
      music.setFitWidth(640); music.setPreserveRatio(true);
      stop.setFitHeight(15); stop.setPreserveRatio(true);
      next.setFitHeight(15); next.setPreserveRatio(true);
      volumes.setFitHeight(15); volumes.setPreserveRatio(true);
      playing.setFitHeight(20); playing.setPreserveRatio(true);
      pauser.setFitHeight(he); pauser.setPreserveRatio(true); pauser.setFitWidth(30);
      skip.setPrefSize(Region.USE_COMPUTED_SIZE,15);
      skip.setPrefWidth(5); skip.setPrefHeight(20);
      play.setPrefSize(Region.USE_COMPUTED_SIZE,15);
      play.setPrefWidth(10);
      restart.setPrefSize(Region.USE_COMPUTED_SIZE,15);
      restart.setPrefWidth(10);
      volume.setPrefSize(Region.USE_COMPUTED_SIZE,15);
      volume.setPrefWidth(10);
      
      slider.setMin(0);
      slider.setMax(100);
      slider.setValue(10);
      // play each audio file in turn.
      for (int i = 0; i < players.size(); i++) {
        MediaPlayer player     = players.get(i);
        player.setVolume(1);
        MediaPlayer nextPlayer = players.get((i + 1) % players.size());
        player.setOnEndOfMedia(new Runnable() {
          @Override public void run() {
            player.currentTimeProperty().removeListener(progressChangeListener);
            mediaView.setMediaPlayer(nextPlayer);
            nextPlayer.play();
          }
        });
      }
  
      // allow the user to skip a track.
      skip.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent actionEvent) {
          final MediaPlayer curPlayer = mediaView.getMediaPlayer();
          MediaPlayer nextPlayer = players.get((players.indexOf(curPlayer) + 1) % players.size());
          mediaView.setMediaPlayer(nextPlayer);
          curPlayer.currentTimeProperty().removeListener(progressChangeListener);
          curPlayer.stop();
          if (playing==play.getGraphic()) {
            mediaView.getMediaPlayer().pause();
            play.setGraphic(pauser);
          }
          nextPlayer.play();
        }
      });
  
      //restart the arreter 
      restart.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent actionEvent) {
          final MediaPlayer curPlayer = mediaView.getMediaPlayer();
          mediaView.setMediaPlayer(curPlayer);
          curPlayer.stop();
          play.setGraphic(playing);
        }
      });
  
      // allow the user to play or pause a track.
      play.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent actionEvent) {
          if (pauser==play.getGraphic()) {
            mediaView.getMediaPlayer().pause();
            play.setGraphic(playing);
          } else {
            mediaView.getMediaPlayer().play();
            mediaView.getMediaPlayer().setVolume(slider.getValue()/100);
            play.setGraphic(pauser);
          }
        }
      });
  
      // display the name of the currently playing track.
      mediaView.mediaPlayerProperty().addListener(new ChangeListener<MediaPlayer>() {
        public void changed(ObservableValue<? extends MediaPlayer> observableValue, MediaPlayer oldPlayer, MediaPlayer newPlayer) {
          setCurrentlyPlaying(newPlayer); 
        }
      });
  
      // start playing the first track.
      mediaView.setMediaPlayer(players.get(0));
      mediaView.setFitWidth(640.0);
      mediaView.setFitHeight(480.0);
    //  mediaView.getMediaPlayer().play();
      setCurrentlyPlaying(mediaView.getMediaPlayer());
  
      // silly invisible button used as a template to get the actual preferred size of the Pause button.
        // Button invisiblePause = new Button("Pause");
        // invisiblePause.setVisible(false);
        // play.prefHeightProperty().bind(invisiblePause.heightProperty());
        // play.prefWidthProperty().bind(invisiblePause.widthProperty());
  
      // layout the scene.
      // String style = String.format(
      //       "-track-color: linear-gradient(to right,"+
      //       "-fx-accent 0%%,"+
      //       "-fx-accent %1$.1f%%,"+
      //       "-default-track-color %1$.1f%%,"+
      //       "-default-track-color 100%%);",10.  
      //     );
      //     slider.setStyle(style);
      play.setStyle("-fx-background-color: black; -fx-border-radius:15px");
      skip.setStyle("-fx-background-color: black");
      restart.setStyle("-fx-background-color: black");
      volume.setStyle("-fx-background-color: black");
      layout.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-alignment: center;");
      HBox hob = HBoxBuilder.create().children(play,skip,restart,volume,slider,currentlyTimePlaying).build();
      

      hob.setStyle("-fx-background-color: black; -fx-text-color:white");hob.setMaxWidth(640);hob.setMinHeight(40);hob.setMaxHeight(40);
      HBox titre = HBoxBuilder.create().children(currentlyPlaying).build();
      titre.setPrefHeight(40);
      titre.setStyle("-fx-background-color: black; -fx-font-size: 13; -fx-text-color:white");titre.setMinWidth(640);titre.setMinHeight(40);titre.setMaxHeight(40);
        layout.getChildren().addAll(
        titre,
        VBoxBuilder.create().spacing(0).alignment(Pos.CENTER).children(
          mediaView,
          progress,
          hob
          ).build()
      );
      
      currentlyPlaying.setTextFill(javafx.scene.paint.Color.web("#ffffff"));
      currentlyTimePlaying.setTextFill(javafx.scene.paint.Color.web("#ffffff"));
      skip.setTranslateY(5); play.setTranslateY(3); restart.setTranslateY(5);
      currentlyTimePlaying.setTranslateX(296); currentlyTimePlaying.setTranslateY(10);
      currentlyTimePlaying.setFont(new Font("Arial",12));
      currentlyPlaying.setTranslateX(10);
      volume.setTranslateX(50); volume.setTranslateY(5);
      slider.setTranslateX(56); slider.setTranslateY(10);
      progress.setMaxWidth(640);
      progress.setMaxHeight(10);
      progress.setMinHeight(10);
      progress.setStyle("-fx-accent: red; -fx-control-inner-background:black; -fx-text-box-border:black; -fx-background-color:black");
      HBox.setHgrow(progress, Priority.ALWAYS);
      
      return new Scene(layout, 640, 600);
    }
  
      private String intToString(double min, double sec){
        int minu = (int) min;
        int seconde = (int) sec%60;
        return donnerSeconde(minu)+":"+ donnerSeconde(seconde);
      }
  
      private String donnerSeconde(int sec){
          if(sec>9) return String.valueOf(sec);
          return "0"+ String.valueOf(sec);
      }
    /** sets the currently playing label to the label of the new media player and updates the progress monitor. */
    private void setCurrentlyPlaying(final MediaPlayer newPlayer) { 
      // System.out.println(slider.getValue());
      // newPlayer.stop();
      // newPlayer.play();
      progress.setProgress(0);
      progressChangeListener = new ChangeListener<Duration>() {
        @Override public void changed(ObservableValue<? extends Duration> observableValue, Duration oldValue, Duration newValue) {
          newPlayer.setVolume(slider.getValue()/100);
          progress.setProgress(1.0 * newPlayer.getCurrentTime().toMillis() / newPlayer.getTotalDuration().toMillis());
          String actuel = intToString( newPlayer.getCurrentTime().toMinutes(), newPlayer.getCurrentTime().toSeconds());
          String total = intToString( newPlayer.getTotalDuration().toMinutes(), newPlayer.getTotalDuration().toSeconds());
          currentlyTimePlaying.setText(actuel+" / "+total);
        }
      };
      newPlayer.currentTimeProperty().addListener(progressChangeListener);
  
      String source = newPlayer.getMedia().getSource();
      source = source.substring(0, source.length() - ".mp4".length());
      source = source.substring(source.lastIndexOf("/") + 1).replaceAll("%20", " ");
      currentlyPlaying.setText(source);
    }
  
    /** @return a MediaPlayer for the given source which will report any errors it encounters */
    private MediaPlayer createPlayer(String aMediaSrc) {
      System.out.println("Creating player for: " + aMediaSrc);
      final MediaPlayer player = new MediaPlayer(new Media(aMediaSrc));
      player.setOnError(new Runnable() {
        @Override public void run() {
          System.out.println("Media error occurred: " + player.getError());
        }
      });
      player.setVolume(20);
      return player;
    }
  }