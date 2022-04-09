package gui;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_I;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_K;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.opengl.GL;

import rendering.Renderer;

/**
 *
 * @author ltrapp
 */
public class Window {

	public static long window = 0;
	public static final int width = 800, height = 600;
	private Renderer renderer;

	public static boolean keyW, keyS, keyI, keyK;

	public Window() {
	}

	public void createWindow(){
        if (!GLFW.glfwInit()) {
            System.err.println("GLFW not initialized");
            return;
        }
        GLFW.glfwDefaultWindowHints();
        window = GLFW.glfwCreateWindow(width, height, "LWJGL", NULL, NULL);
        glfwSetKeyCallback(window, new KeyHandler());
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        
        glfwShowWindow(window);
    }

	public void loop() {
		GL.createCapabilities();
		glClearColor(0, 0, 0, 1);

		renderer = new Renderer();

		while (!glfwWindowShouldClose(window)) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			renderer.updateKeys(keyW, keyS, keyI, keyK);

			renderer.render();

			glfwSwapBuffers(window);
			glfwPollEvents();
		}
	}

	public void dispose() {
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		glfwTerminate();
	}

	private class KeyHandler implements GLFWKeyCallbackI {

		@Override
		public void invoke(long window, int key, int scancode, int action, int mods) {
			if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
				glfwSetWindowShouldClose(window, true);
			}
			if (key == GLFW_KEY_W && action == GLFW_PRESS) {
				keyW = true;
			}
			if (key == GLFW_KEY_W && action == GLFW_RELEASE) {
				keyW = false;
			}
			if (key == GLFW_KEY_S && action == GLFW_PRESS) {
				keyS = true;
			}
			if (key == GLFW_KEY_S && action == GLFW_RELEASE) {
				keyS = false;
			}

			if (key == GLFW_KEY_I && action == GLFW_PRESS) {
				keyI = true;
			}
			if (key == GLFW_KEY_I && action == GLFW_RELEASE) {
				keyI = false;
			}
			if (key == GLFW_KEY_K && action == GLFW_PRESS) {
				keyK = true;
			}
			if (key == GLFW_KEY_K && action == GLFW_RELEASE) {
				keyK = false;
			}
		}

	}

}
