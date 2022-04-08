
package lwjgl_new.rendering;

import lwjgl_new.rendering.passes.Background;
import lwjgl_new.rendering.passes.Ball;
import lwjgl_new.rendering.passes.Pong;
import lwjgl_new.rendering.passes.ScoreBoard;	

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
        background = new Background(ball);
        scoreBoard = new ScoreBoard();
    }

    public void render() {
        player1.render();
        player2.render();

        ball.setCollidedPlayer(-1);

        ball.updatePosition();
        ball.checkWindowBoundries();
        
        ball.checkCollision(player1, 0);
        ball.checkCollision(player2, 1);
        
        if(ball.checkForPoint());
        
        scoreBoard.render();
        ball.render();
        try {
            Thread.sleep(10);
        } catch (InterruptedException ex) {
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