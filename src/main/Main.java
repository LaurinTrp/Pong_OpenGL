package main;

import java.io.File;

import gui.Window;

public class Main {

	public static void main(String[] args) {

		String projectPath = System.getProperty("user.dir");
		
		System.setProperty("RESOURCE", projectPath + File.separator + "resources" + File.separator);
		
		Window window = new Window();
		window.createWindow();
		window.loop();
		window.dispose();
	}
}
