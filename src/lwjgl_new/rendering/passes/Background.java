package lwjgl_new.rendering.passes;

import static org.lwjgl.opengl.GL11C.GL_FLOAT;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11C.glDrawArrays;
import static org.lwjgl.opengl.GL13C.glActiveTexture;
import static org.lwjgl.opengl.GL15C.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15C.GL_DYNAMIC_READ;
import static org.lwjgl.opengl.GL15C.glBindBuffer;
import static org.lwjgl.opengl.GL15C.glBufferData;
import static org.lwjgl.opengl.GL15C.glDeleteBuffers;
import static org.lwjgl.opengl.GL15C.glGenBuffers;
import static org.lwjgl.opengl.GL20C.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glUniform1i;
import static org.lwjgl.opengl.GL20C.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;
import static org.lwjgl.opengl.GL30C.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30C.glGenVertexArrays;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import lwjgl_new.gui.Window;
import lwjgl_new.main.OS_Specifics;
import lwjgl_new.rendering.Texture;
import lwjgl_new.rendering.shaders.ShaderProgram;

/**
 *
 * @author djanssen
 */
public class Background {

	private boolean init = false;

	private int vao = 0, vbo = 0;
	private ShaderProgram program;
	private Texture texture;

	private boolean showTimer = true;
	float realWidth = 50f;
	float realHeight = 100f;
	private float width, height;

	public Background() {
		width = realWidth / Window.width;
		height = realHeight / Window.height;

		init();
	}

	private void init() {
		initVertices();
		initTextures();
		// compile and upload shader
		program = new ShaderProgram("background", "vertex.glsl", "fragment.glsl");
		init = true;
	}

	private void initVertices() {
		float[] counter = new float[] { -width, height, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, -width, -height, 0.0f, 1.0f,
				0.0f, 1.0f, 0.0f, 1.0f, width, -height, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f,
				//
				-width, height, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, width, -height, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f,
				width, height, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, };

		// create VAO
		vao = glGenVertexArrays();
		vbo = glGenBuffers();

		glBindVertexArray(vao);
		{
			// upload VBO
			glBindBuffer(GL_ARRAY_BUFFER, vbo);
			glBufferData(GL_ARRAY_BUFFER, counter, GL_DYNAMIC_READ);

			// define Vertex Attributes
			glEnableVertexAttribArray(0);
			glVertexAttribPointer(0, 4, GL_FLOAT, false, 8 * 4, 0 * 4);

			glEnableVertexAttribArray(1);
			glVertexAttribPointer(1, 4, GL_FLOAT, false, 8 * 4, 4 * 4);
		}
		glBindVertexArray(0);

	}

	private void initTextures() {
		texture = new Texture();
		reloadTimerTexture(0);
	}

	public void reloadTimerTexture(int time) {
		BufferedImage image = new BufferedImage((int) realWidth, (int) realHeight, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2d = image.createGraphics();
		g2d.setColor(Color.RED);
		g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, (int) (image.getHeight())));

		FontMetrics fm = g2d.getFontMetrics();

		g2d.drawString("" + time, image.getWidth() / 2f - fm.stringWidth("" + time) / 2f, image.getHeight() - 10);

		texture.readBufferedImage(image);
	}

	public void render() {

		if (!init) {
			return;
		}

		// render vertices (vbo) of VAO vao to framebuffer and use the shader
		// "simple-triangle"
		{
			program.start();
			{

				glUniform1i(program.getUniformLocation("texSampler"), 0);
				glActiveTexture(GL_TEXTURE_2D);
				texture.bind();

				glBindVertexArray(vao);
				{
					glUniform1i(program.getUniformLocation("showTimer"), showTimer ? 1 : 0);
					glDrawArrays(GL_TRIANGLES, 0, 6);
				}
				glBindVertexArray(0);

				texture.unbind();
			}
			program.stop();
		}
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
