package edu.ou.cs.cg.Project;

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
import java.lang.Object;


public class Particle
{
  private Color color;                   // Color of the particle

  private boolean isAlive;               //Checks to see if the particle is still alive

 private Point2D.Double position;       // Position of the Particle
    
 private Point2D.Double newPosition;       // Position of the Particle
    

 private Double velocity;               // Velocity of the particle (how fast it is going)

 private Double acceleration;


    
  private double size;                   // How large the particle will be
  private int age;                       // Duration in seconds when the particle was created
  private int life;                      // How long the particle will 'live' for. When the particle is considered
                                         // “dead” ( 0 lifetime) it will not be rendered until it is re-emitted.

  public Particle()
  {
    this.isAlive = true;
    this.position = null;
    this.newPosition = null;
    this.color = null;
    this.velocity = 0.0;
    this.acceleration = 0.0;
    this.size = 0;
    this.age = 0;
    this.life = 0;
    
    
    
  }



  /******************************************
          Setters for Particle Variables
   ******************************************/
  public void setColor(Color color)
  {
    this.color = color;
  }

  public void setPosition(Point2D.Double position)
  {
    this.position = position;
  }
    
    public void setNewPosition(Point2D.Double newPosition)
    {
        this.newPosition = newPosition;
    }


  public void setSize(double size)
  {
    this.size = size;
  }

  public void setLife(int life)
  {
    this.life = life;
  }




  /*******************************************
          Getters for Particle Variables
   *******************************************/
  public Color getColor()
  {
    return this.color;
  }

  public Point2D.Double getPosition()
  {
    return this.position;
  }

  public double getSize()
  {
    return this.size;
  }

  public int getLife()
  {
    return this.life;
  }




  /*****************************************************
        Method to update the position of the particle
   *****************************************************/
  public void update()
  {
    // Increases the life of the particle
    this.age ++;

    // If the age is greater than the particle's life
    // Mark it as dead
    if(this.age > this.life)
    {
      this.isAlive = false;
    }
    
    
    
/**
    // Change the position of particle
    this.velocity += 1;
      
      double xPos = this.position.getX();
      double yPos = this.position.getY();
      
      xPos += -.001;
      yPos += -.001;
      
    // Change position
      Point2D.Double newP = new Point2D.Double(xPos, yPos);
      this.setPosition(newP);
 **/
      //Change position
      
      if(newPosition != null)
      {
          checkBoundries();
          this.setPosition(add(position,newPosition));
      }

  }
    
    /*****************************************************
     Method add 2 Point2D.Double values
     *****************************************************/
    public Point2D.Double add(Point2D.Double p1, Point2D.Double p2)
    {
        Point2D.Double newLocation = new Point2D.Double();
        newLocation = new Point2D.Double(p1.getX() + p2.getX(), p1.getY() + p2.getY());
        return newLocation;
    }
    
    
    /*****************************************************
     Method for checking width and height constraints
     *****************************************************/
    private void checkBoundries()
    {
        if(position.getX() < -1.0 || position.getX() >  1.0)
        {
            setNewPosition(new Point2D.Double(newPosition.getX() * -1, newPosition.getY()));
        }
        
        if(position.getY() < -1.0 || position.getY() >  1.0)
        {
            setNewPosition(new Point2D.Double(newPosition.getX(), newPosition.getY() * -1));
        }

        
    }
    
   

}
