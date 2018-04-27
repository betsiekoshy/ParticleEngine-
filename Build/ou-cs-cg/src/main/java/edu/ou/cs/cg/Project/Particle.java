package edu.ou.cs.cg.Project;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.Random;
import java.util.ArrayList;
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
  private Color color;                    // Color of the particle

  private boolean isAlive;                //Checks to see if the particle is still alive
  private boolean inside;                 //Checks to see if particle is inside the boundaries
  private boolean movingToShape;          //Checks to see if the particle is moving towards a shape

  private Point2D.Double position;       // Position of the Particle
  private Point2D.Double newPosition;    // New Position of the Particle
  private Point2D.Double speed;          //speed of the particle

  private ArrayList<Point2D.Double> points;   //All points that makes up the particle

  private double size;                   // How large the particle will be
  private double mass;                   // mass of the particle

  private Path2D.Double bounds;         // window bounds

  public Particle()
  {
    //Intialize all varaibles
    this.color = null;

    this.isAlive = true;
    this.inside = false;
    this.movingToShape = false;

    this.position = null;
    this.newPosition = null;
    this.speed = new Point2D.Double( (Math.random() * 3.0) + 2.0, (Math.random() * 3.0)+2.0);

    this.points = new ArrayList<Point2D.Double>();

    this.size = 0;
    this.mass =0;

    this.bounds = new Path2D.Double();
  }



  /******************************************
          Setters for Particle Variables
   ******************************************/
  public void setColor(Color color)
  {
    this.color = color;
  }

  public void setInside(boolean inside)
  {
    this.inside = inside;
  }

  public void setIsMovingToShape(boolean movingToShape)
  {
    this.movingToShape = movingToShape;
  }

  public void setBounds(Path2D.Double bounds)
  {
    this.bounds = bounds;
  }

  public void setPosition(Point2D.Double position)
  {
    this.position = position;
  }

  public void setNewPosition(Point2D.Double newPosition)
  {
      this.newPosition = newPosition;
  }

  public void setPoints(ArrayList<Point2D.Double> points)
  {
    this.points = points;
  }

  public void setMass(double mass)
  {
    this.mass = mass;
  }

  public void setSize(double size)
  {
    this.size = size;
  }

  public void setIsAlive(boolean isAlive){
    this.isAlive = isAlive;
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

  public Point2D.Double getNewPosition()
  {
    return this.newPosition;
  }

  public double getSize()
  {
    return this.size;
  }

  public ArrayList<Point2D.Double>  getPoints()
  {
    return this.points;
  }

  public boolean isInside()
  {
    return this.inside;
  }

  public boolean isMovingToShape()
  {
    return this.movingToShape;
  }

  public boolean isAlive(){
    return this.isAlive;
  }



}
