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



public class Particle
{
  private Color color;                   // Color of the particle

  private boolean isAlive;               //Checks to see if the particle is still alive

  private Point2D.Double position;       // Position of the Particle

  private Double velocity;               // Velocity of the particle (how fast it is going)

  private double size;                   // How large the particle will be
  private int age;                       // Duration in seconds when the particle was created
  private int life;                      // How long the particle will 'live' for. When the particle is considered
                                         // “dead” ( 0 lifetime) it will not be rendered until it is re-emitted.

  public Particle()
  {
    this.isAlive = true;
    this.position = null;
    this.color = null;
    this.velocity = 0.0;
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
        Method to uplade the position of the particle
   *****************************************************/
  public void update()
  {
    // Increases the life of the particle
    this.age ++;

    // If the age is greater than the particle's life
    // Make it as dead
    if(this.age > this.life)
    {
      this.isAlive = false;
    }

    // Change the position of particleEffec
    //TODO

  }

}
