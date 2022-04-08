package lwjgl_new.gui;

import lwjgl_new.rendering.Renderer;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;


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
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
           if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE){
               glfwSetWindowShouldClose(window, true);
           } 
           if(key == GLFW_KEY_W && action == GLFW_PRESS){
               keyW = true;
           }
           if(key == GLFW_KEY_W && action == GLFW_RELEASE){
               keyW = false;
           }
           if(key == GLFW_KEY_S && action == GLFW_PRESS){
               keyS = true;
           }
           if(key == GLFW_KEY_S && action == GLFW_RELEASE){
               keyS = false;
           }
           
           if(key == GLFW_KEY_I && action == GLFW_PRESS){
               keyI = true;
           }
           if(key == GLFW_KEY_I && action == GLFW_RELEASE){
               keyI = false;
           }
           if(key == GLFW_KEY_K && action == GLFW_PRESS){
               keyK = true;
           }
           if(key == GLFW_KEY_K && action == GLFW_RELEASE){
               keyK = false;
           }
        });
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        
        glfwShowWindow(window);
    }
    
    public void loop(){
        GL.createCapabilities();
        glClearColor(0, 0, 0, 1);
        
        renderer = new Renderer();
        
        while(!glfwWindowShouldClose(window)){
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            
            renderer.updateKeys(keyW, keyS, keyI, keyK);
            
            renderer.render();
            
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }
    
    public void dispose(){
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        
        glfwTerminate();
    }
    
}
