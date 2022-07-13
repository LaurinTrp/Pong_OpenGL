package main;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import gui.Window;


/**
 *
 * @author ltrapp
 */
public class Main {
    public static void main(String[] args) {
    	String path = "resource";
    	File f = new File(path);
    	System.setProperty("RESOURCE", f.getAbsolutePath() + "\\");
    	
        Window window = new Window();
        window.createWindow();
        window.loop();
        window.dispose();
    }
}
