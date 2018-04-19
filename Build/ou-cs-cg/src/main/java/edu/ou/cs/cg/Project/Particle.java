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
  private Color color;                   // Color of the particle

  private boolean isAlive;               //Checks to see if the particle is still alive

  private Point2D.Double position;       // Position of the Particle

  private Point2D.Double newPosition;       // Position of the Particle

  private Point2D.Double speed;

  private ArrayList<Point2D.Double> points;

  private Double velocity;               // Velocity of the particle (how fast it is going)

  private Double acceleration;

  private double size;                   // How large the particle will be
  private double mass;

  private Path2D.Double bounds;
  private boolean inside;


  private int age;                       // Duration in seconds when the particle was created
  private int life;                      // How long the particle will 'live' for. When the particle is considered
                                         // “dead” ( 0 lifetime) it will not be rendered until it is re-emitted.

  public Particle()
  {
    this.inside = false;
    this.isAlive = true;
    this.position = null;
    this.newPosition = null;
    this.color = null;
    this.velocity = 0.0;
    this.acceleration = 0.0;
    this.size = 0;
    this.age = 0;
    this.life = 0;
    this.points = new ArrayList<Point2D.Double>();
    this.mass =0;
    this.bounds = new Path2D.Double();
    this.speed = new Point2D.Double( (Math.random() * 3.0) + 2.0, (Math.random() * 3.0)+2.0);
  }



  /******************************************
          Setters for Particle Variables
   ******************************************/
  public void setColor(Color color)
  {
    this.color = color;
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
  public void update(Buttons buttons)
  {
    // Increases the life of the particle
    this.age ++;

    // If the age is greater than the particle's life
    // Mark it as dead
    if(this.age > this.life)
    {
      this.isAlive = false;
    }

    if(newPosition != null)
    {
        checkBoundries(buttons);
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
    private void checkBoundries(Buttons button)
    {
      Random rand = new Random();
      if(bounds.contains(position) || inside){
        if(position.getX() < -1.0 || position.getX() >  1.0)
        {
            setNewPosition(new Point2D.Double(newPosition.getX() * -1, newPosition.getY()));
        }

        if(position.getY() < -1.0 || position.getY() >  1.0)
        {
            setNewPosition(new Point2D.Double(newPosition.getX(), newPosition.getY() * -1));
        }

        inside = true;
      }

        ArrayList<Path2D.Double> buttonPaths = button.getButtonShapes();
         Point2D.Double[] buttonCenters = button.getAllCenterPoints();
        //
        // for(int i = 0; i < buttonCenters.length; i ++)
        // {
        //   if (position.getX() + this.size + button.getSize() > buttonCenters[i].getX()
        //     && position.getX() < buttonCenters[i].getX() + this.size + button.getSize()
        //     && position.getY() + this.size + button.getSize() >  buttonCenters[i].getY()
        //     && position.getY() < buttonCenters[i].getY() + this.size + button.getSize())
        //     {
        //       double x1 = ((position.getX() - buttonCenters[i].getX()) * (position.getX() - buttonCenters[i].getX()));
        //       double y1 = ((position.getY() - buttonCenters[i].getY()) * (position.getY() - buttonCenters[i].getY()));
        //
        //       double distance = Math.sqrt(x1 + y1);
        //
        //       if (distance < this.size + button.getSize())
        //       {
        //
        //         double collisionPointX = ((position.getX() * button.getSize()) + (buttonCenters[i].getX()* this.size)) / (this.size + button.getSize());
        //         double collisionPointY = ((position.getY() * button.getSize()) + (buttonCenters[i].getY()* this.size)) / (this.size+ button.getSize());
        //
        //
        //
        //         double mass1 = this.mass;
        //         double mass2 = button.getMass();
        //
        //         double speedX = this.speed.getX();
        //         double speedY = this.speed.getY();
        //
        //         double val1 = this.mass - button.getMass();
        //         double val2 = this.mass + button.getMass();
        //
        //
        //         double newVelX1 = ((speedX * (mass1 - mass2)) / (mass1 + mass2));
        //         double newVelY1 = ((speedY * (mass2 - mass1)) / (mass1 + mass2));
        //
        //         this.speed = new Point2D.Double(newVelX1,newVelY1);
        //
        //         double x = position.getX() + newVelY1;
        //         double y = position.getY() + newVelX1;
        //
        //         System.out.println(newVelX1 + ", " + newVelY1);
        //         setNewPosition(new Point2D.Double(x,y));
        //
        //
        //       }
        //     }
        // }


        for(int i = 0; i < buttonCenters.length; i ++)
        {
          for(Point2D.Double point: points)
          {
            if(buttonPaths.get(i).contains(point))
            {

              if (buttonCenters[i].getX() + 0.17 > point.getX())
              {
                setNewPosition(new Point2D.Double(newPosition.getX() * - 1, newPosition.getY()));
                //  setHorizontalDirection(Direction.DIRECTION_RIGHT);
              }
              else
              {
                setNewPosition(new Point2D.Double(newPosition.getX() , newPosition.getY()));

                //  setHorizontalDirection(Direction.DIRECTION_LEFT);
              }

              if (buttonCenters[i].getY() + 0.17> point.getY())
              {
                setNewPosition(new Point2D.Double(newPosition.getX(), newPosition.getY() * -1));

                //  setVerticalDirection(Direction.DIRECTION_UP);
              }
              else
              {
                setNewPosition(new Point2D.Double(newPosition.getX(), newPosition.getY()));

                //  setVerticalDirection(Direction.DIRECTION_DOWN);
              }
              break;
              //setNewPosition(new Point2D.Double(newPosition.getX() * (rand.nextBoolean() ? -1:1), newPosition.getY()* (rand.nextBoolean() ? -1:1)));
            }
          }
        }

        // Point2D.Double[] buttonPoints = button.getAllCenterPoints();
        // resolveCollision(buttonPoints[0]);

    }


}
