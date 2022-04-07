/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lwjgl_new.rendering.passes;

import com.mycompany.lwjgl_new.gui.Window;
import com.mycompany.lwjgl_new.rendering.shaders.ShaderProgram;
import glm.vec._2.Vec2;
import glm.vec._4.Vec4;
import java.util.Random;
import static org.lwjgl.opengl.GL11C.GL_FLOAT;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11C.glDrawArrays;
import static org.lwjgl.opengl.GL15C.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15C.GL_DYNAMIC_READ;
import static org.lwjgl.opengl.GL15C.glBindBuffer;
import static org.lwjgl.opengl.GL15C.glBufferData;
import static org.lwjgl.opengl.GL15C.glDeleteBuffers;
import static org.lwjgl.opengl.GL15C.glGenBuffers;
import org.lwjgl.opengl.GL20;
import static org.lwjgl.opengl.GL20C.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;
import static org.lwjgl.opengl.GL30C.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30C.glGenVertexArrays;

/**
 *
 * @author djanssen
 */
public class Ball {

    private boolean init = false;

    private int vao = 0, vbo = 0;
    private ShaderProgram program;

    private float width, height;

    private float velX, velY;

    private Vec2 offset = new Vec2();
    
    private Vec4 color = new Vec4(1.0f, 1.0f, 1.0f, 1.0f);
    

    public Ball(float width, float height, float offsetX, float offsetY) {
        this.width = width / Window.width;
        this.height = height / Window.height;
        offset.set(offsetX / Window.width * 2, offsetY / Window.height * 2);
    }

    private void initVertices() {
        float[] vertices = new float[]{
            -width, height, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,
            -width, -height, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,
            width, -height, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,
            //
            -width, height, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,
            width, -height, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,
            width, height, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,};

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
        String path = "C:\\Users\\ltrapp\\Documents\\NetBeansProjects\\LWJGL_New\\src\\main\\java\\com\\mycompany\\resources\\ball\\";
        program = new ShaderProgram(path + "vertex.vs", path + "fragment.fs");

//        calculateVelocity();

        init = true;
    }

    private void calculateVelocity() {
        Random random = new Random();

        int randomX = 0;
        while (randomX == 0) {
            randomX = random.nextInt(4) - 2;
        }
        int randomY = 0;
        while (randomY == 0) {
            randomY = random.nextInt(4) - 2;
        }

        velX = (float) randomX / Window.width * 2;
        velY = (float) randomY / Window.height * 2;
    }

    private void updatePosition() {
        offset.set(offset.x + velX, offset.y + velY);
    }

    private void checkWindowBoundries() {
        boolean reset = false;
        if (offset.y + height > 1 || offset.y - height < -1) {
            velY = -velY;
        }
        if (offset.x + width > 1 || offset.x - width < -1) {
            offset.set(0, 0);
            calculateVelocity();
        }
    }
    
    public void checkCollision(Pong pong){
//        if(offset.y - height < pong.getOffset().y + pong.getHeight()/2 &&
//                offset.y + height > pong.getOffset().y - pong.getHeight()/2 &&
//                offset.x - width/2 < pong.getOffset().x + pong.getWidth()/2 &&
//                offset.x + width/2 > pong.getOffset().x - pong.getWidth()/2 ){
//            velX = -velX;
//        }
        System.out.println(pong.getOffset());
        if(offset.y - height < pong.getOffset().y + pong.getHeight()/2){
            color.set(new Vec4(1.0, 0.0, 0.0, 1.0));
        }else{
            color.set(new Vec4(1.0, 1.0, 1.0, 1.0));
        }
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
                    updatePosition();
                    checkWindowBoundries();
                    GL20.glUniform2fv(program.getUniformLocation("offset"), offset.toFA_());
                    GL20.glUniform4fv(program.getUniformLocation("myColor"), color.toFA_());
                    glDrawArrays(GL_TRIANGLES, 0, 6);
                }
                glBindVertexArray(0);
            }
            program.stop();
        }
    }

    public Vec2 getOffset() {
        return offset;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
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
