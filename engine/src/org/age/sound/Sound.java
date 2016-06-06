package org.age.sound;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import java.io.InputStream;

public class Sound {

  public static void playSound(String name) {
    try {
      InputStream inputStream = Sound.class.getResourceAsStream("/fiveweek/" + name + ".wav");
      AudioStream audioStream = new AudioStream(inputStream);
      AudioPlayer.player.start(audioStream);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
