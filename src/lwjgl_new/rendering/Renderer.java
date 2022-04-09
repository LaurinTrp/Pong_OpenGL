
package lwjgl_new.rendering;

import lwjgl_new.rendering.passes.Background;
import lwjgl_new.rendering.passes.Ball;
import lwjgl_new.rendering.passes.Pong;

import org.lwjgl.glfw.GLFW;
import static org.lwjgl.opengl.GL11.*;

import lwjgl_new.gui.Window;
import lwjgl_new.rendering.passes.*;

/**
 *
 * @author ltrapp
 */
public class Renderer {

	private Pong player1;
	private Pong player2;
	private Ball ball;
	private Background background;
	private ScoreBoard scoreBoard;

	public Renderer() {
		player1 = new Pong(20f, 100f, -300, 0);
		player2 = new Pong(20f, 100f, 300, 0);
		ball = new Ball(20f, 20f, 0, 0);
		background = new Background();
		scoreBoard = new ScoreBoard();
		
	}

	public void render() {

		ball.setCollidedPlayer(-1);

		ball.updatePosition();
		ball.checkWindowBoundries();

		ball.checkCollision(player1, 0);
		ball.checkCollision(player2, 1);

		if (ball.checkForPoint()) {
			pointScored();
		}

		player1.render();
		player2.render();
		ball.render();

		scoreBoard.render();

		try {
			Thread.sleep(10);
		} catch (InterruptedException ex) {
		}
	}

	private void pointScored() {
		scoreBoard.reloadTexture();
		player1.resetOffset();
		player2.resetOffset();

		long end = System.currentTimeMillis();

		for (int i = 3; i > 0; i--) {
			long start = System.currentTimeMillis();
			glClear(GL_COLOR_BUFFER_BIT);
			player1.render();
			player2.render();
			ball.render();

			scoreBoard.render();
			background.reloadTimerTexture(i);
			background.render();

			GLFW.glfwSwapBuffers(Window.window);
			GLFW.glfwPollEvents();

			while (end - start < 1000) {
				end = System.currentTimeMillis();
				GLFW.glfwPollEvents();
			}
		}

	}

	public void dispose() {
		player1.dispose();
		player2.dispose();
		ball.dispose();
		background.dispose();
		scoreBoard.dispose();
	}

	public void updateKeys(boolean... keys) {
		player1.updateKeys(keys[0], keys[1]);
		player2.updateKeys(keys[2], keys[3]);
	}

}
