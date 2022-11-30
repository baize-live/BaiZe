import Bean.PlayMusic;
import Bean.Vars;
import View.LoginWindow;


public class Program {

    public static void main(String[] args) {
        new Thread(new PlayMusic(Vars.LocalData + "music/bgm.wav", true, -30)).start();
        LoginWindow.getInstance();
        LoginWindow.automaticLog();
    }

}
