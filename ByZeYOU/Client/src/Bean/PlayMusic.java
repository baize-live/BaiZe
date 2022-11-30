package Bean;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class PlayMusic implements Runnable {
    String file;
    boolean loop;
    float value;

    public PlayMusic(String file, boolean loop, float value) {
        this.file = file;
        this.loop = loop;
        if (value < -80) value = -80;
        if (value > 6) value = 6;
        this.value = value;
        this.value = -80;
    }

    @Override
    public void run() {
        do {
            try {
                AudioInputStream cin = AudioSystem.getAudioInputStream(new File(file));
                AudioFormat format = cin.getFormat();
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
                SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
                line.open(format);

                FloatControl gainControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(value);

                line.start();
                int nBytesRead = 0;
                byte[] buffer = new byte[512];
                while (true) {
                    nBytesRead = cin.read(buffer, 0, buffer.length);
                    if (nBytesRead <= 0)
                        break;
                    line.write(buffer, 0, nBytesRead);
                }
                line.drain();
            } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
                e.printStackTrace();
                System.out.println("音乐播放异常");
            }
        } while (loop);
    }
}
