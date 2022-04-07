/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lwjgl_new.rendering;

import com.mycompany.lwjgl_new.rendering.passes.Ball;
import com.mycompany.lwjgl_new.rendering.passes.Pong;

/**
 *
 * @author ltrapp
 */
public class Renderer {

    private Pong player1;
    private Pong player2;
    private Ball ball;
    
    public Renderer() {
        player1 = new Pong(20f, 100f, -300, 0);
        player2 = new Pong(20f, 100f, 300, 0);
        ball = new Ball(20f, 20f, 0, 0);
    }
    
    public void render(){        
        player1.render();     
        player2.render();
        ball.checkCollision(player1);
//        ball.checkCollision(player2);
        ball.render();
    }
    
    public void dispose(){
        player1.dispose();
        player2.dispose();
        ball.dispose();
    }
    
    public void updateKeys(boolean... keys){
        player1.updateKeys(keys[0], keys[1]);
        player2.updateKeys(keys[2], keys[3]);
    }
    
}
