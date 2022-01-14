package com.example.taa;

import javafx.geometry.*;
import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;

public class Sprite {
    private Image image;
    private double positionX;
    private double positionY;
    private double velocityY;
    private double velocityX;
    private double width;
    private double height;


    public Sprite(){
        positionX = 0;
        positionY = 0;
        velocityX = 0;
        velocityY = 0;
    }

    public void setImage(Image i){
        image = i;
        width = i.getWidth();
        height = i.getHeight();
    }

    public void setImage(String filename){
        Image i = new Image(filename);
        setImage(i);
    }

    public Image getImage(){
        return image;
    }

    public void setPosition(double x, double y){
        positionX = x;
        positionY = y;
    }

    public void setVelocity(double x, double y){
        velocityX = x;
        velocityY = y;
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public void addVelocity(double x, double y){
        velocityX += x;
        velocityY += y;
    }

    public void update(double width, double height, double time){
        double addx = velocityX + time;
        double addy = velocityY + time;
        if (positionX + addx > 10 && positionX + addx < width - 10)
            positionX += velocityX * time;
        if (positionY + addy > 10 && positionY + addy < height - 10)
            positionY += velocityY * time;
    }
    public Rectangle2D getBoundary(){
        return new Rectangle2D(positionX, positionY,width,height);
    }
    public boolean intersects(Sprite s){
        return s.getBoundary().intersects(this.getBoundary());
    }
    public String toString(){
        return "position : ["+ positionX + " , " + positionY + "]" + "velocity: [" + velocityX + " , " + velocityY + "]";
    }
}
