import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.Random;
import javax.swing.*;
import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.gl2.GLUT;
import com.sun.org.apache.xerces.internal.impl.dv.xs.YearMonthDV;


public class Particle {

   
    //this represents two-dimensional vectors
    
   private int x; 
   private int y; 
   private int speedX;
   private int speedY; 
   private int size; 
   private int life; 


    public Particle(int x, int y, int speedX, int speedY, int size, int life){
        this.x = x; //x coordinate 
        this.y = y; //y coordinate 
        this.speedX = speedX; 
        this.speedY = speedY; 
        this.size = size; 
        this.life= life; 
        
    }

    public boolean update(){

       x += speedX; 
       y += speedY; 
       life --; 
       if(life <= 0){
           return true; //it is dead 
    }
       return false; 

    
    }

    public void render(){

    }
}