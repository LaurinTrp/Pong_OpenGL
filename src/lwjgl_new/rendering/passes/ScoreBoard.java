package lwjgl_new.rendering.passes;


import static org.lwjgl.opengl.GL11C.GL_FLOAT;
import static org.lwjgl.opengl.GL11C.GL_NEAREST;
import static org.lwjgl.opengl.GL11C.GL_RGBA;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11C.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11C.glBindTexture;
import static org.lwjgl.opengl.GL11C.glDeleteTextures;
import static org.lwjgl.opengl.GL11C.glDrawElements;
import static org.lwjgl.opengl.GL11C.glGenTextures;
import static org.lwjgl.opengl.GL11C.glGetIntegerv;
import static org.lwjgl.opengl.GL11C.glTexImage2D;
import static org.lwjgl.opengl.GL11C.glTexParameteri;
import static org.lwjgl.opengl.GL12C.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13C.glActiveTexture;
import static org.lwjgl.opengl.GL15C.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15C.GL_DYNAMIC_READ;
import static org.lwjgl.opengl.GL15C.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15C.glBindBuffer;
import static org.lwjgl.opengl.GL15C.glBufferData;
import static org.lwjgl.opengl.GL15C.glDeleteBuffers;
import static org.lwjgl.opengl.GL15C.glGenBuffers;
import static org.lwjgl.opengl.GL20C.GL_MAX_VERTEX_ATTRIBS;
import static org.lwjgl.opengl.GL20C.glDeleteProgram;
import static org.lwjgl.opengl.GL20C.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glGetUniformLocation;
import static org.lwjgl.opengl.GL20C.glUniform1i;
import static org.lwjgl.opengl.GL20C.glUniform4fv;
import static org.lwjgl.opengl.GL20C.glUseProgram;
import static org.lwjgl.opengl.GL20C.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30C.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30C.glBindFramebuffer;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;
import static org.lwjgl.opengl.GL30C.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30C.glGenVertexArrays;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.system.MemoryUtil;

import glm.mat._4.Mat4;
import glm.vec._4.Vec4;
import lwjgl_new.main.ImageLoader;
import lwjgl_new.rendering.shaders.ShaderProgram;

/**
 *
 * @author djanssen
 */
public class ScoreBoard {

    private boolean init = false;

    private int vao = 0, vbo = 0, tex = 0;

    private ShaderProgram program = null;


    private void init() {

       
    	initVertices();
    	initShader();
    	loadTextures();

        

        init = true;
    }
    
    
    
    private void initVertices() {

         //  create vertex array
         float[] vertices = new float[]{
             //  vertex 0 (TL)
             -0.28f, 0.5f, 0.0f, 1.0f,   //  pos
             0.0f, 1.0f, 0.0f, 0.0f,     //  uv-coords
             //  vertex 1 (BL)
             -0.28f, -0.5f, 0.0f, 1.0f,  //  pos
             0.0f, 0.5f, 0.0f, 0.0f,     //  uv-coords
             //  vertex 2 (BR)
             0.28f, -0.5f, 0.0f, 1.0f,   //  pos
             0.33333f, 0.5f, 0.0f, 0.0f, //  uv-coords
             //  vertex 3 (TR)
             0.28f, 0.5f, 0.0f, 1.0f,    //  pos
             0.3333f, 1.0f, 0.0f, 0.0f   //  uv-coords
         };

         //  create VAO, VBO and EBO
         vao = glGenVertexArrays();
         vbo = glGenBuffers();

         glBindVertexArray(vao);
         {
             //  upload VBO
             glBindBuffer(GL_ARRAY_BUFFER, vbo);
             glBufferData(GL_ARRAY_BUFFER, vertices, GL_DYNAMIC_READ);

             //  define Vertex Attributes
             //  position
             glEnableVertexAttribArray(0);
             glVertexAttribPointer(0, 4, GL_FLOAT, false, 8 * Float.BYTES, 0 * Float.BYTES);
             //  texture coodinates (uv-coordinates)
             glEnableVertexAttribArray(1);
             glVertexAttribPointer(1, 4, GL_FLOAT, false, 8 * Float.BYTES, 4 * Float.BYTES);
         }
         glBindVertexArray(0);
    }
    
    private void initShader() {
        //  compile and upload shader
        program = new ShaderProgram(System.getProperty("RESOURCE") + "scoreboard\\vertex.glsl", System.getProperty("RESOURCE") + "scoreboard\\fragment.glsl");
    }

    private void loadTextures() {
    	tex = ImageLoader.loadTextureFromResource(System.getProperty("RESOURCE") + "textures\\earth_image.png");
    }
    
    public void render() {
        
        if (!init) {
            init();
        }

        if (!init) {
            return;
        }

        {
            
            program.start();
            {
                glActiveTexture(GL_TEXTURE0);
                glBindTexture(GL_TEXTURE_2D, tex);
//                glUniform1i(program.texId, 0);

                glBindVertexArray(vao);
                {
//                    glUniform4fv(program.offset, (new Vec4(0, 0, 0, 0)).to(vec4_buffer));
                    glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
                }
                glBindVertexArray(0);
            }
            program.stop();
        }
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void dispose() {

        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
        glDeleteTextures(tex);
        vao = 0;
        vbo = 0;
        tex = 0;

        if (program != null) {
            program.cleanUp();
        }

        init = false;
    }

}
