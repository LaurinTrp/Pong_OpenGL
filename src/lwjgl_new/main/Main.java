package lwjgl_new.main;
import lwjgl_new.gui.Window;


/**
 *
 * @author ltrapp
 */
public class Main {
    public static void main(String[] args) {
    	
    	System.setProperty("RESOURCE", "C:\\Users\\ltrapp\\git\\OpenGL-Pong\\src\\main\\java\\com\\mycompany\\resources\\");
    	
        Window window = new Window();
        window.createWindow();
        window.loop();
        window.dispose();
    }
}
