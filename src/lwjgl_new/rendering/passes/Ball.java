/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lwjgl_new.rendering.passes;

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
import static org.lwjgl.opengl.GL20C.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;
import static org.lwjgl.opengl.GL30C.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30C.glGenVertexArrays;

import java.util.Random;

import static org.lwjgl.opengl.GL20.*;

import glm.vec._2.Vec2;
import glm.vec._4.Vec4;
import lwjgl_new.gui.Window;
import lwjgl_new.rendering.shaders.ShaderProgram;

/**
 *
 * @author djanssen
 */
public class Ball {

	private boolean init = false;

	private int vao = 0, vbo = 0, vaoBackground = 0, vboBackground = 0;
	private ShaderProgram program, programBackground;

	private float width, height;

	private double velX, velY;

	private Vec2 offset = new Vec2();
	private Vec2 collisionPosition = new Vec2();

	private Vec4 color = new Vec4(1.0f, 1.0f, 1.0f, 1.0f);
	public static Vec2 score = new Vec2();

	private int collidedPlayer = -1;

	private boolean frameCollision = false;
	private Vec2 frameCollisionPosition = new Vec2();

	boolean checkTopBottom = true;
	private double timer;

	public Ball(float width, float height, float offsetX, float offsetY) {
		this.width = width / Window.width;
		this.height = height / Window.height;
		offset.set(offsetX / Window.width * 2, offsetY / Window.height * 2);
	}

	private void initVertices() {
		float[] vertices = new float[] { -width, height, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, -width, -height, 0.0f,
				1.0f, 1.0f, 1.0f, 1.0f, 1.0f, width, -height, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,
				//
				-width, height, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, width, -height, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,
				width, height, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, };

		float[] background = new float[] { -1, 1, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, -1, -1, 0.0f, 1.0f, 1.0f, 1.0f,
				1.0f, 1.0f, 1, -1, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,
				//
				-1, 1, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1, -1, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1, 1, 0.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 1.0f, };

		// create VAO
		vao = glGenVertexArrays();
		vbo = glGenBuffers();

		glBindVertexArray(vao);
		{
			// upload VBO
			glBindBuffer(GL_ARRAY_BUFFER, vbo);
			glBufferData(GL_ARRAY_BUFFER, vertices, GL_DYNAMIC_READ);

			// define Vertex Attributes
			glEnableVertexAttribArray(0);
			glVertexAttribPointer(0, 4, GL_FLOAT, false, 8 * 4, 0 * 4);

			glEnableVertexAttribArray(1);
			glVertexAttribPointer(1, 4, GL_FLOAT, false, 8 * 4, 4 * 4);
		}
		glBindVertexArray(0);

		vaoBackground = glGenVertexArrays();
		vboBackground = glGenBuffers();
		glBindVertexArray(vaoBackground);
		{
			glBindBuffer(GL_ARRAY_BUFFER, vboBackground);
			glBufferData(GL_ARRAY_BUFFER, background, GL_DYNAMIC_READ);

			glEnableVertexAttribArray(0);
			glVertexAttribPointer(0, 4, GL_FLOAT, false, 8 * 4, 0 * 4);

			glEnableVertexAttribArray(1);
			glVertexAttribPointer(1, 4, GL_FLOAT, false, 8 * 4, 4 * 4);
		}
		glBindVertexArray(0);

	}

	private void init() {
		initVertices();

		// compile and upload shader
		String path = System.getProperty("RESOURCE");
		program = new ShaderProgram(path + "ball\\vertex.glsl", path + "ball\\fragment.glsl");
		programBackground = new ShaderProgram(path + "background\\vertex.glsl", path + "background\\fragment.glsl");

		calculateVelocity();

		init = true;
	}

	private void calculateVelocity() {
		Random random = new Random();

		float randomX = (random.nextFloat() > 0.5) ? 1 + random.nextFloat() : -1 - random.nextFloat();
		float randomY = (random.nextFloat() > 0.5) ? 1 + random.nextFloat() : -1 - random.nextFloat();
		velX = (float) randomX / Window.width * 2;
		velY = (float) randomY / Window.height * 2;

	}

	public void updatePosition() {
		offset.set(offset.x + velX, offset.y + velY);
	}

	public boolean checkWindowBoundries() {
		if (offset.y + height > 1 || offset.y - height < -1) {
			frameCollision = true;
			frameCollisionPosition = new Vec2(offset);
			velY = -velY;
			return true;
		}
		return false;
	}

	public boolean checkForPoint() {
		if (offset.x + width > 1 || offset.x - width < -1) {
			if (offset.x < 0) {
				score.set(score.x, score.y + 1);
			} else {
				score.set(score.x + 1, score.y);
			}

			frameCollision = true;
			frameCollisionPosition = new Vec2(offset);
			offset.set(0, 0);
			calculateVelocity();

			return true;
		}
		return false;
	}

	public boolean checkCollision(Pong pong, int player) {
		if (offset.y - height <= pong.getOffset().y + pong.getHeight()
				&& offset.y + height >= pong.getOffset().y - pong.getHeight()
				&& offset.x - width <= pong.getOffset().x + pong.getWidth()
				&& offset.x + width >= pong.getOffset().x - pong.getWidth()) {
			
			velX = -velX;
			Vec2 tempOffset = new Vec2(offset);
			Vec2 tempOffsetPong = new Vec2(pong.getOffset());
			collisionPosition = new Vec2((tempOffset.x + tempOffsetPong.x) / 2f,
					(tempOffset.y + tempOffsetPong.y) / 2f);
			collidedPlayer = player;
			return true;
		}
		return false;
	}

	public void render() {

		// check init -> init
		if (!init) {
			init();
		}

		if (!init) {
			return;
		}

		{

			programBackground.start();
			{
				glBindVertexArray(vaoBackground);
				{
					checkWindowBoundries();
					glUniform1i(programBackground.getUniformLocation("wallCollision"), checkWindowBoundries() ? 1 : 0);
					glUniform1i(programBackground.getUniformLocation("player"), collidedPlayer);
					glUniform2fv(programBackground.getUniformLocation("collisionPosition"), collisionPosition.toFA_());
					glDrawArrays(GL_TRIANGLES, 0, 6);
				}
				glBindVertexArray(0);
			}
			programBackground.stop();

			program.start();
			{
				glBindVertexArray(vao);
				{
					glUniform2fv(program.getUniformLocation("offset"), offset.toFA_());
					glUniform4fv(program.getUniformLocation("myColor"), color.toFA_());
					glDrawArrays(GL_TRIANGLES, 0, 6);
					speedIncrease();

				}
				glBindVertexArray(0);
			}
			program.stop();

		}
	}

	private void speedIncrease() {
		if (timer < Math.pow(5, -4)) {
			velX += (velX < 0) ? -timer : timer;
			velY += (velY < 0) ? -timer : timer;
			timer += Math.pow(10, -8);
		}
	}

	public void setCollidedPlayer(int collidedPlayer) {
		this.collidedPlayer = collidedPlayer;
	}

	public void setCollision(boolean collision) {
		this.frameCollision = collision;
	}

	public boolean isFrameCollision() {
		return frameCollision;
	}

	public Vec2 getFrameCollisionPosition() {
		return frameCollisionPosition;
	}

	public Vec2 getCollisionPosition() {
		return collisionPosition;
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

		vaoBackground = 0;
		vboBackground = 0;

		program.cleanUp();
		programBackground.cleanUp();

		init = false;
	}
}
