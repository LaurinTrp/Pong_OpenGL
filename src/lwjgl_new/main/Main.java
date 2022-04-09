package lwjgl_new.main;

import lwjgl_new.gui.Window;

public class Main {

	public static void main(String[] args) {

		String projectPath = System.getProperty("user.dir");
		
		System.setProperty("RESOURCE", projectPath + "#src#resources#".replaceAll("#", OS_Specifics.getFilepathSeperator()));
		
		System.out.println(OS_Specifics.getFilepathSeperator());

		Window window = new Window();
		window.createWindow();
		window.loop();
		window.dispose();
	}
}
