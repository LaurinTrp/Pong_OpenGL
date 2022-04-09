package lwjgl_new.rendering.passes;

import static org.lwjgl.opengl.GL11C.GL_FLOAT;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11C.glDeleteTextures;
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
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11C;
import org.lwjgl.opengl.GL20;

import glm.vec._2.Vec2;
import lwjgl_new.gui.Window;
import lwjgl_new.rendering.Texture;
import lwjgl_new.rendering.shaders.ShaderProgram;

/**
 *
 * @author djanssen
 */
public class ScoreBoard {

	private boolean init = false;

	private int vao = 0, vbo = 0, tex = 0;

	private ShaderProgram program = null;

	private Texture texture;

	private float width, height;
	private Vec2 offset = new Vec2();
	float realWidth = 400f;
	float realHeight = 150f;

	public ScoreBoard() {

		width = realWidth / Window.width;
		height = realHeight / Window.height;

		offset.set(0, 250f / (Window.height / 2f));

	}

	private void init() {

		initVertices();
		initShader();
		loadTextures();

		init = true;
	}

	private void initVertices() {

		// create vertex array
		float[] vertices = new float[] {
				// vertex 0 (TL)
				-width / 2f, height / 2f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, // uv-coords
				// vertex 1 (BL)
				-width / 2f, -height / 2f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, // uv-coords
				// vertex 2 (BR)
				width / 2f, -height / 2f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, // uv-coords

				// vertex 0 (TL)
				-width / 2f, height / 2f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, // uv-coords
				// vertex 1 (BL)
				width / 2f, -height / 2f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, // uv-coords
				// vertex 2 (BR)
				width / 2f, height / 2f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, // uv-coords
		};

		// create VAO, VBO and EBO
		vao = glGenVertexArrays();
		vbo = glGenBuffers();

		glBindVertexArray(vao);
		{
			// upload VBO
			glBindBuffer(GL_ARRAY_BUFFER, vbo);
			glBufferData(GL_ARRAY_BUFFER, vertices, GL_DYNAMIC_READ);

			// define Vertex Attributes
			// position
			glEnableVertexAttribArray(0);
			glVertexAttribPointer(0, 4, GL_FLOAT, false, 8 * Float.BYTES, 0 * Float.BYTES);

			// texture
			glEnableVertexAttribArray(1);
			glVertexAttribPointer(1, 4, GL_FLOAT, false, 8 * Float.BYTES, 4 * Float.BYTES);
		}
		glBindVertexArray(0);
	}

	private void initShader() {
		// compile and upload shader
		program = new ShaderProgram("scoreboard", "vertex.glsl", "fragment.glsl");
	}

	private void loadTextures() {
		texture = new Texture();
		reloadTexture();
	}

	public void reloadTexture() {
		String scoreString = (int) Ball.score.x + ":" + (int) Ball.score.y;

		BufferedImage image = new BufferedImage((int) realWidth, (int) realHeight, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2d = image.createGraphics();
		g2d.setColor(Color.WHITE);
		g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, (int) (image.getHeight())));

		FontMetrics fm = g2d.getFontMetrics();

		g2d.drawString(scoreString, image.getWidth() / 2f - fm.stringWidth(scoreString) / 2f, image.getHeight() - 10);

		texture.readBufferedImage(image);
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
				glUniform1i(program.getUniformLocation("texSampler"), 0);
				glActiveTexture(GL_TEXTURE_2D);
				texture.bind();

				glBindVertexArray(vao);
				{
					GL20.glUniform2fv(program.getUniformLocation("offset"), offset.toFA_());

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
