package edu.ou.cs.cg.Project;

import edu.ou.cs.cg.Project.Particle;
import edu.ou.cs.cg.Project.Buttons;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.Random;
import java.util.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.awt.*;
import javax.media.opengl.glu.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.gl2.GLUT;
import java.lang.Object;


public class ParticleEffect
{
  public static final int NUMBER_OF_PARTICLES_OVERALL = 200;                      // Overall number of particles in the application

  private static ArrayList<Particle> particles = new ArrayList<Particle>();    // All the particles in the application

  private ArrayList<Color> colors;                      // Default colors of the particles (Red, Green, or Blue)

  private static Random rand;               // For Random Number generator

  private static int i;                     // Keeps track of how many particles are in the application so far
  private final int sides = 18;             // Number of sides for the particle
  private final int minSize = 10;           // Minimum size for particles
  private final int maxSize = 20;           // Maximum size for particles

  private double angle;                     // Angle to create the particle circle
  private double x;                         // Final x postion for the random particle
  private double y;                         // Final y postion for the random particle

  private Buttons buttons;
  private GL2 gl;

  private Path2D.Double bounds;


  public ParticleEffect(GL2 gl, Buttons buttons, Path2D.Double bounds)
  {
    //initalize variables
    this.gl = gl;
    this.buttons = buttons;
    this.bounds = bounds;

    // Initilize the Color array for RGB
    //this.RGB = new Color[]{new Color(255,0,0), new Color(0,255,0), new Color(0,0,255)};
    this.colors = buttons.getColors();

    // Initialize boundaries
    // At the beginning, create all the particles
    if(i < NUMBER_OF_PARTICLES_OVERALL )
    {
      // Initialize and add randomized variables for all particles
      for(i = 0; i < NUMBER_OF_PARTICLES_OVERALL ; i++)
      {
        // Create a single particle
        Particle particle = new Particle();

        //store the bounds of the scrren
        particle.setBounds(bounds);

        // Gives the particle random initial variables
        RandomizeParticle(particle);

        // Add particles to the ArrayList of particles
        particles.add(particle);

        // Draw the particle
        drawParticle(particle);
      }
    }
    else
    {
      // Loops through the arraylist of particles
      for(Particle particle: particles)
      {
        // Update the particle varaibles (this is where the particle will move, etc)
        update(particle);

        // Redraw the particle

        drawParticle(particle);

      }
    }

  }

  public void generateParticles(int numParticles)
  {
    for(int i = 0; i < numParticles; i++)
    {
      // Create a single particle
      Particle particle = new Particle();

      // Set the bounds of the screen
      particle.setBounds(bounds);

      // Gives the particle random initial variables
      RandomizeParticle(particle);

      // Add particles to the ArrayList of particles
      particles.add(particle);

      // Draw the particle
      drawParticle(particle);
    }
  }


  /************************************************************************
                 Randomly distribute all of the particles
    Give all particles a random position, Color, Size, life and velocity
   ************************************************************************/
  public void RandomizeParticle(Particle particle)
  {
    // Initlaize the Random varaible
    rand = new Random();

    //Check to see if any point is inside the button
    boolean intersects = false;

    //New points for the particle
    Point2D.Double center;
    double newRandomX = 0;
    double newRandomY = 0;

    // Gives the Particle a Random Size
    //particle.setSize((rand.nextInt((this.maxSize - this.minSize) + 1) + this.minSize) / 1000.0);
    particle.setSize(20 / 1000.0);

    // Gives the Particle the Color Red, Green, or Blue
    //particle.setColor(RGB[rand.nextInt(3)]);
    particle.setColor(colors.get(rand.nextInt(colors.size())));

    // Gives a particle a mass
    particle.setMass(((float)(4 / 3 * Math.PI * Math.pow(particle.getSize(), 3.0))));

    //Calculates the starting center for the particle
    //Random point on the top and bottom of the screen
    double randomX = rand.nextInt(200) / 100.0 ;
    double randomY =  (200 + (int)(Math.random() * ((210 - 200) + 1))) / 100.0 ;
    Point2D.Double randomYaxis = new Point2D.Double(randomX, randomY);

    //Random point on the left and right of the screen
    double randomX1 = (200 + (int)(Math.random() * ((210 - 200) + 1))) / 100.0 ;
    double randomY1 = rand.nextInt(200) / 100.0 ;
    Point2D.Double randomXaxis = new Point2D.Double(randomX1, randomY1);

    //Randomly initalize new center
    center = (rand.nextBoolean() ? randomXaxis : randomYaxis);

    //if point spawns from the left side, moves the particle right
    //if point spawns from the right side, moves the particle left
    if(center.getX() <= 0 || center.getX() >= 2)
    {
        newRandomX = center.getX() * (.0055) * -1;
        newRandomY = center.getY() * (.0055) * (rand.nextBoolean() ? -1 : 1);
    }

    //if point spawns from the top side, moves the particle bottom
    //if point spawns from the right bottom, moves the particle top
    if(center.getY() <= 0 || center.getY() >= 2)
    {
        newRandomX = center.getX() * (.0055) * (rand.nextBoolean() ? -1 : 1);
        newRandomY = center.getY() * (.0055) * -1;
    }

    // Gives the Particle a Random Position between -1 to 1
    particle.setPosition(center);

    //Gives the Particle a new Random Position between -1 to 1
    particle.setNewPosition(new Point2D.Double(newRandomX, newRandomY));
  }



  /**************************************************
         Creating the actual particle (circles)
   **************************************************/
  private void drawParticle(Particle particle)
  {
    // Set the color of the particle
    setColor(gl, particle.getColor());

    ArrayList<Point2D.Double> points = new ArrayList<Point2D.Double>();
    // Begin drawing the circ;e
    gl.glBegin(GL.GL_TRIANGLE_FAN);

    // Loops around in a circle depending on the sides set
    for(int i = 0; i <= this.sides; i++)
    {
      // Figure out the angle of the particle
      angle = 2 * Math.PI * i / this.sides;

      // Calculates the final x and y position of the particle
      x = particle.getPosition().getX() + particle.getSize() * Math.cos(angle);
      y = particle.getPosition().getY() + particle.getSize() * Math.sin(angle);

      // Draws the section
      gl.glVertex2d(x,y);
      points.add(new Point2D.Double(x,y));
    }

    // Completed in drawing the particle
    gl.glEnd();
    particle.setPoints(points);
  }

  /*****************************************************
        Method to update the position of the particle
   *****************************************************/
  public void update(Particle particle)
  {
    if(!particle.isMovingToShape() && particle.isAlive())
    {
      checkBoundries(particle);
    }

    particle.setPosition(add(particle.getPosition(), particle.getNewPosition()));
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

  private void checkBoundries(Particle particle)
  {
    //Initialize random variable
    Random rand = new Random();

    //Check if particles is inside the screen
    if(bounds.contains(particle.getPosition()) || particle.isInside())
    {
      // Check to see if the particle collides with the left or right side of the screen
      if(particle.getPosition().getX() <= 0 || particle.getPosition().getX() >=  2.0)
      {
        // Reverse the direction of the x
        particle.setNewPosition(new Point2D.Double(particle.getNewPosition().getX() * -1, particle.getNewPosition().getY()));
      }

      // Check to see if the particle collides with the top or bottom side of the screen
      if(particle.getPosition().getY() <= 0 || particle.getPosition().getY() >=  2.0)
      {
        // Reverse the direction of the y
        particle.setNewPosition(new Point2D.Double(particle.getNewPosition().getX(), particle.getNewPosition().getY() * -1));
      }

      // Particle is inside the screen
      particle.setInside(true);
    }

    //Intialize lists of button centers and bounds
    ArrayList<Shape> shapes = buttons.getShapes();

    for(Shape shape: shapes)
    {
      //Get all all points that create the bounds of the particle
      for(Point2D.Double point: particle.getPoints())
      {
        //Check if any point collids with the button bounds
        if(shape.getShapeBounds().contains(point) && shape.isActive())
        {
          //Set new position if particles collid with buttons
          if (shape.getCenter().getX() + shape.getSize() >= point.getX())
          {
            particle.setNewPosition(new Point2D.Double(particle.getNewPosition().getX() * - 1, particle.getNewPosition().getY()));
          }
          else
          {
            particle.setNewPosition(new Point2D.Double(particle.getNewPosition().getX() , particle.getNewPosition().getY()));
          }

          if (shape.getCenter().getY() + shape.getSize()>= point.getY())
          {
            particle.setNewPosition(new Point2D.Double(particle.getNewPosition().getX(), particle.getNewPosition().getY() * -1));
          }
          else
          {
            particle.setNewPosition(new Point2D.Double(particle.getNewPosition().getX(), particle.getNewPosition().getY()));
          }
          break;
        }
      }
    }

  }

  public void moveTowardButton(Point2D.Double initialClick){


    		ArrayList<Particle> particles = this.getParticles();


    		for(Shape shape: buttons.getShapes()){
    			if(shape.getShapeBounds().contains(initialClick) && shape.isActive())
    			{
    				shape.setDrawingType(6);
    				shape.setTextColor(new Color(0f,0f,0f,1f));

    				for(Particle particle: this.getParticles() )
    				{

    					if(particle.getColor() == shape.getColor() && particle.isAlive() && shape.getNumCount() != 0)
    					{
    						particle.setIsMovingToShape(true);

    						double deltaX = shape.getCenter().getX() - particle.getPosition().getX();
    						double deltaY = shape.getCenter().getY() - particle.getPosition().getY();

    						double angle = Math.atan2( deltaY, deltaX ) ;

    						double currentX = particle.getPosition().getX() * (0.05) * Math.cos( angle );
    						double currentY = particle.getPosition().getY() * (0.05) * Math.sin( angle );

    						particle.setNewPosition(new Point2D.Double(currentX, currentY));
    					}

    					if(shape.getShapeBounds().contains(particle.getPosition()))
    					{
    						particle.setPosition(shape.getCenter());
    						particle.setNewPosition(new Point2D.Double(0,0));
    						particle.setIsAlive(false);
    						if(shape.getNumCount() != 0){
    							shape.setNumCount(shape.getNumCount() - 1);
    						}else{
    							shape.setNumCount(0);
    						}

    					}

    					if(shape.getNumCount() == 0){
    						particle.setIsMovingToShape(false);
    						shape.setIsActive(false);
    					}

    				}
    			}
    		}

    		ArrayList<Particle> aliveParticles = new ArrayList<Particle>();
    		for(Particle particle: this.getParticles() )
    		{
    			if(particle.isAlive()){
    				aliveParticles.add(particle);
    			}
    		}

    		this.setParticles(aliveParticles);
  }

  /**************************************************
           Creating the colors of the particle
      Either some are transparent or in full color
   **************************************************/
  private void	setColor(GL2 gl, Color color, int alpha)
  {
    gl.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, alpha / 255.0f);
  }

  private void	setColor(GL2 gl, Color color)
  {
    setColor(gl, color, 255);
  }

  public ArrayList<Particle>  getParticles()
  {
    return this.particles;
  }

  public void setParticles(ArrayList<Particle> particles)
  {
    this.particles = particles;
  }

}
