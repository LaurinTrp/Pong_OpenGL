/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lwjgl_new.rendering.passes;

import com.mycompany.lwjgl_new.rendering.shaders.ShaderProgram;
import static org.lwjgl.opengl.GL11C.GL_FLOAT;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11C.glDrawArrays;
import static org.lwjgl.opengl.GL15C.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15C.GL_DYNAMIC_READ;
import static org.lwjgl.opengl.GL15C.glBindBuffer;
import static org.lwjgl.opengl.GL15C.glBufferData;
import static org.lwjgl.opengl.GL15C.glDeleteBuffers;
import static org.lwjgl.opengl.GL15C.glGenBuffers;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform2fv;
import static org.lwjgl.opengl.GL20C.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;
import static org.lwjgl.opengl.GL30C.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30C.glGenVertexArrays;

/**
 *
 * @author djanssen
 */
public class Background {

    private boolean init = false;

    private int vao = 0, vbo = 0;
    private ShaderProgram program;

    private Ball ball;
    private int collidedPlayer = -1;
    private boolean frameCollision = false;
    
    private double timer;

    public Background(Ball ball) {
        this.ball = ball;
    }

    private void initVertices() {
        float[] vertices = new float[]{
            -1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
            -1.0f, -1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
            1.0f, -1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
            //
            -1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
            1.0f, -1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f,};

        //  create VAO
        vao = glGenVertexArrays();
        vbo = glGenBuffers();

        glBindVertexArray(vao);
        {
            //  upload VBO
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, vertices, GL_DYNAMIC_READ);

            //  define Vertex Attributes
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 4, GL_FLOAT, false, 8 * 4, 0 * 4);

            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, 4, GL_FLOAT, false, 8 * 4, 4 * 4);
        }
        glBindVertexArray(0);

    }

    private void init() {
        initVertices();

        //  compile and upload shader
        String path = "C:\\Users\\ltrapp\\Documents\\NetBeansProjects\\LWJGL_New\\src\\main\\java\\com\\mycompany\\resources\\background\\";
        program = new ShaderProgram(path + "vertex.vs", path + "fragment.fs");

        init = true;
    }

    public void render() {

        //  check init -> init
        if (!init) {
            init();
        }

        if (!init) {
            return;
        }

        //  render vertices (vbo) of VAO vao to framebuffer and use the shader "simple-triangle"
        {
            program.start();
            {
                glBindVertexArray(vao);
                {
                    glUniform1i(program.getUniformLocation("wallCollision"), frameCollision ? 1 : 0);
                    if (frameCollision) {
                        glUniform2fv(program.getUniformLocation("collisionPosition"), ball.getFrameCollisionPosition().toFA_());
                    } else if (collidedPlayer != -1) {
                        glUniform2fv(program.getUniformLocation("collisionPosition"), ball.getCollisionPosition().toFA_());
                    }
                    glUniform1i(program.getUniformLocation("player"), collidedPlayer);
                    glDrawArrays(GL_TRIANGLES, 0, 6);
                }
                glBindVertexArray(0);
            }
            program.stop();
        }
    }
    
    private void timerReset(){
        timer = Math.pow(10, -1);
    }
    
    private void timerCountdown(){
        timer-= Math.pow(10, -7);
    }

    public void setCollidedPlayer(int collidedPlayer) {
        this.collidedPlayer = collidedPlayer;
    }

    public void setFrameCollision(boolean frameCollision) {
        this.frameCollision = frameCollision;
    }

    public void dispose() {

        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
        vao = 0;
        vbo = 0;

        program.cleanUp();

        init = false;
    }
}
