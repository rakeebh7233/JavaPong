/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaprojectclient;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

/**
 *
 * @author hossa
 */
public class Ball extends Rectangle {

    Random random;
    int xVelocity=1;
    int yVelocity=1;
    int initialSpeed = 2;

    Ball(int x, int y, int width, int height){
        super(x,y,width,height);
    }
        
    public void setXDirection(int randomXDirection) {
            xVelocity = randomXDirection;
    }
    public void setYDirection(int randomYDirection) {
            yVelocity = randomYDirection;
    }
    public void move() {
            x += xVelocity;
            y += yVelocity;
    }
    public void draw(Graphics g) {
            g.setColor(Color.black);
            g.fillOval(x, y, height, width);
    }
}