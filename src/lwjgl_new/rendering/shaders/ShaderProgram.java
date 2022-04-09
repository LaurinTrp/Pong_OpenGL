/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lwjgl_new.rendering.shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import lwjgl_new.main.OS_Specifics;

/**
 *
 * @author ltrapp
 */
public class ShaderProgram {

    private int programID;
    private int vertexShaderID;
    private int fragmentShaderID;

    public ShaderProgram(String folder, String vertexFile, String fragmentFile) {
    	String resourceFolder = System.getProperty("RESOURCE");
        vertexShaderID = loadShader(resourceFolder + folder + OS_Specifics.getFilepathSeperator() + vertexFile, GL20.GL_VERTEX_SHADER);
        fragmentShaderID = loadShader(resourceFolder + folder + OS_Specifics.getFilepathSeperator() + fragmentFile, GL20.GL_FRAGMENT_SHADER);
        programID = GL20.glCreateProgram();
        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);
        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);
    }

    
    public int getUniformLocation(String uniform){
        return GL20.glGetUniformLocation(programID, uniform);
    }
    
    public void start() {
        GL20.glUseProgram(programID);
    }

    public void stop() {
        GL20.glUseProgram(0);
    }

    public void cleanUp() {
        stop();
        GL20.glDetachShader(programID, vertexShaderID);
        GL20.glDetachShader(programID, fragmentShaderID);
        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragmentShaderID);
        GL20.glDeleteProgram(programID);
    }

    private static int loadShader(String file, int type) {
        StringBuilder shaderSource = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append("//\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);
        if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
            System.err.println("Could not compile shader!");
            System.exit(-1);
        }
        return shaderID;
    }
}
