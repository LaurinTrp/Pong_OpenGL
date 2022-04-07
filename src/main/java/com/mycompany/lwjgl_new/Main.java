/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lwjgl_new;

import com.mycompany.lwjgl_new.gui.Window;


/**
 *
 * @author ltrapp
 */
public class Main {
    public static void main(String[] args) {
        Window window = new Window();
        window.createWindow();
        window.loop();
        window.dispose();
    }
}
