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

    private double velX, velY;

    private Vec2 offset = new Vec2();

    private Vec4 color = new Vec4(1.0f, 1.0f, 1.0f, 1.0f);

    private double timer = 0;

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

        calculateVelocity();

        init = true;
    }

    private void calculateVelocity() {
        Random random = new Random();

        float randomX = (random.nextFloat() > 0.5) ? 1 + random.nextFloat() : -1 - random.nextFloat();
        float randomY = (random.nextFloat() > 0.5) ? 1 + random.nextFloat() : -1 - random.nextFloat();
        System.out.println(randomX + "\t" + randomY);
        velX = (float) randomX / Window.width * 2;
        velY = (float) randomY / Window.height * 2;

        timer = 0;
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
            checkTopBottom = true;
        }
    }

    boolean checkTopBottom = true;

    public void checkCollision(Pong pong) {

        if (offset.y - height <= pong.getOffset().y + pong.getHeight()
                && offset.y + height >= pong.getOffset().y - pong.getHeight()
                && offset.x - width <= pong.getOffset().x + pong.getWidth()
                && offset.x + width >= pong.getOffset().x - pong.getWidth()) {
            velX = -velX;
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

                    if (timer < Math.pow(5, -4)) {
                        velX += (velX < 0) ? -timer : timer;
                        velY += (velY < 0) ? -timer : timer;
                        timer += 1d / 100000000d;
                    }
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
