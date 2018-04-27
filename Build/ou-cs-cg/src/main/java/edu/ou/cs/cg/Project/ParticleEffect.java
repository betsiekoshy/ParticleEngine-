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
  public static int NUMBER_OF_PARTICLES_OVERALL;                      // Overall number of particles in the application

  private static ArrayList<Particle> particles = new ArrayList<Particle>();    // All the particles in the application

  private ArrayList<Color> colors;          // Default colors of the particles (Red, Green, or Blue)

  private static Random rand;               // For Random Number generator

  private static int i;                     // Keeps track of how many particles are in the application so far
  private final int sides = 18;             // Number of sides for the particle
  private final int size = 45;              // Maximum size for particles

  private double angle;                     // Angle to create the particle circle
  private double x;                         // Final x postion for the random particle
  private double y;                         // Final y postion for the random particle

  private Buttons buttons;                  //Button class
  private GL2 gl;                           //GL2
  private Component component;              //canvas component

  private Path2D.Double bounds;             //window boundaries

  private static boolean initial = true;    //beginning of state

  public ParticleEffect(GL2 gl, Buttons buttons, Path2D.Double bounds, Component c)
  {
    //initalize variables
    this.gl = gl;
    this.buttons = buttons;
    this.component = c;
    this.bounds = bounds;
    NUMBER_OF_PARTICLES_OVERALL = 0;

    // Initilize the Color array for RGB
    this.colors = buttons.getColors();


    // Initialize boundaries
    // At the beginning, create all the particles
    if(initial)
    {
      particles = new ArrayList<Particle>();
      // Initialize and add randomized variables for all particles
      for(Shape shape: buttons.getShapes())
      {
        if(!shape.isMixColor() && !shape.isTwoTone()){
          for(int i = 0; i < shape.getNumCount()/2; i++)
          {
            // Create a single particle
            Particle particle = new Particle();

            //store the bounds of the scrren
            particle.setBounds(bounds);

            // Gives the particle random initial variables
            RandomizeParticle(particle);

            particle.setColor(shape.getColorOne());

            // Add particles to the ArrayList of particles
            particles.add(particle);

            // Draw the particle
            drawParticle(particle);

          }
        }
      }
      //initialize is false
      initial = false;
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

  public void generateParticles()
  {
    //loop through all the shape
    for(Shape shape: buttons.getShapes())
    {
      //check to see what single shape is still active
      if(shape.isActive() && !shape.isMixColor() && !shape.isTwoTone())
      {
        //create number of particles depending on shape
        for(int i = 0; i < shape.getNumCount(); i++)
        {
          // Create a single particle
          Particle particle = new Particle();

          //store the bounds of the scrren
          particle.setBounds(bounds);

          // Gives the particle random initial variables
          RandomizeParticle(particle);

          particle.setColor(shape.getColorOne());

          // Add particles to the ArrayList of particles
          particles.add(particle);

          // Draw the particle
          drawParticle(particle);
        }
      }
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
    particle.setSize(this.size / 1000.0);

    // Gives a particle a mass
    particle.setMass(((float)(4 / 3 * Math.PI * Math.pow(particle.getSize(), 3.0))));

    //Calculates the starting center for the particle
    //Random point on the bottom of the screen
    double randomX = (5 + (int)(Math.random() * ((195 - 5) + 1))) / 100.0 ;
    double randomY =  (200 + (int)(Math.random() * ((215 - 200) + 1))) / 100.0 ;
    Point2D.Double randomBottom = new Point2D.Double(randomX, randomY);

    //Random point on the right of the screen
    randomX = (200 + (int)(Math.random() * ((215 - 200) + 1))) / 100.0 ;
    randomY =  (5 + (int)(Math.random() * ((195 - 5) + 1)))  / 100.0 ;
    Point2D.Double randomRight = new Point2D.Double(randomX, randomY);

    //Random point on the top of the screen
    randomX = (5 + (int)(Math.random() * ((195 - 5) + 1))) / 100.0 ;
    randomY =  (1 + (int)(Math.random() * ((10 - 1) + 1))) / -100.0 ;
    Point2D.Double randomTop = new Point2D.Double(randomX, randomY);

    //Random point on the left of the screen
    randomX =  (1 + (int)(Math.random() * ((10 - 1) + 1))) / -100.0 ;
    randomY =  (5 + (int)(Math.random() * ((195 - 5) + 1))) / 100.0 ;
    Point2D.Double randomLeft = new Point2D.Double(randomX, randomY);

    //Randomly initalize new center from either top, bottom, left or right
    center = (rand.nextBoolean() ? (rand.nextBoolean() ? randomRight : randomBottom) : (rand.nextBoolean() ? randomLeft : randomTop));

    //if point spawns from the right side, moves the particle left
    if(center == randomRight)
    {
        newRandomX = center.getX() * (.0055) * -1;
        newRandomY = center.getY() * (.0055) * (rand.nextBoolean() ? -1 : 1);
    }

    //if point spawns from the right bottom, moves the particle top
    if(center == randomBottom)
    {
        newRandomX = center.getX() * (.0055) * (rand.nextBoolean() ? -1 : 1);
        newRandomY = center.getY() * (.0055) * -1;
    }

    //if point spawns from the top side, moves the particle bottom
    if(center == randomTop)
    {
        newRandomX = center.getX() * (.0055) * (rand.nextBoolean() ? -1 : 1) ;
        newRandomY = center.getY() * (.1) * -1;
    }

    //if point spawns from the left side, moves the particle right
    if(center == randomLeft)
    {
        newRandomX = center.getX() * (.1) * -1;
        newRandomY = center.getY() * (.0055) * (rand.nextBoolean() ? -1 : 1);
    }

    // Gives the Particle a Random Position
    particle.setPosition(center);

    //Gives the Particle a new Random Position
    particle.setNewPosition(new Point2D.Double(newRandomX, newRandomY));
  }



  /**************************************************
         Creating the actual particle (circles)
   **************************************************/
  private void drawParticle(Particle particle)
  {
    //get the x and y positions of the particle
    float x0 = (float)particle.getPosition().getX();
    float y0 = (float)particle.getPosition().getY();

    //all the points that creates the particle
    ArrayList<Point2D.Double> points = new ArrayList<Point2D.Double>();

    // Set the color of the particle
    setColor(gl, particle.getColor());

    //begin drawing the particle
    gl.glBegin(GL.GL_TRIANGLE_STRIP);

    //create the particles depending on howmany sides it has
    for(int i = 0; i <= this.sides; i++)
    {
      //if the point is in the center
      if(i % 2 == 0)
      {
        //set the color of the particle
        setColor(gl, particle.getColor());

        //draw the vertext of the particle
        gl.glVertex2d(particle.getPosition().getX(), particle.getPosition().getY());

        //add the point to the points
        points.add(new Point2D.Double(x,y));
      }

      //if the point is the outside of the particleEffect
      //set the color of it black and transparent
      setColor(gl, new Color(0, 0, 0, 0), 0);

      //find the angle
      angle = 2 * Math.PI * i / this.sides;

      //get the x and y position
      this.x = x0 + particle.getSize() * Math.cos(angle);
      this.y = y0 + particle.getSize() * Math.sin(angle);

      //draw the vertext
      gl.glVertex2d(x,y);

      //add the point to points
      points.add(new Point2D.Double(x,y));
    }

    //end drawing shape
    gl.glEnd();

    //add the points to particle
    particle.setPoints(points);

  }

  /*****************************************************
        Method to update the position of the particle
   *****************************************************/
  public void update(Particle particle)
  {
    //check the boundaries of the particle
    checkBoundries(particle);

    //get the new position of the particle
    particle.setPosition(add(particle.getPosition(), particle.getNewPosition()));
  }

  /*****************************************************
  Method add 2 Point2D.Double values
  *****************************************************/
  public Point2D.Double add(Point2D.Double p1, Point2D.Double p2)
  {
    //initalize the new location
    Point2D.Double newLocation = new Point2D.Double();

    //add point one and point two to get the new location
    newLocation = new Point2D.Double(p1.getX() + p2.getX(), p1.getY() + p2.getY());

    //return new location
    return newLocation;
  }


  private void checkBoundries(Particle particle)
  {

    //Check if particles is inside the screen
    if(bounds.contains(particle.getPosition()) || particle.isInside())
    {
      // Check to see if the particle collides with the left or right side of the screen
      if(particle.getPosition().getX() < 0 || particle.getPosition().getX() >  2.0)
      {
        // Reverse the direction of the x
        particle.setNewPosition(new Point2D.Double(particle.getNewPosition().getX() * -1, particle.getNewPosition().getY()));
      }

      // Check to see if the particle collides with the top or bottom side of the screen
      if(particle.getPosition().getY() < 0 || particle.getPosition().getY() >  2.0)
      {
        // Reverse the direction of the y
        particle.setNewPosition(new Point2D.Double(particle.getNewPosition().getX(), particle.getNewPosition().getY() * -1));
      }

      // Particle is inside the screen
      particle.setInside(true);
    }

    //check particles that are not moving to a shape and is alive
    if(!particle.isMovingToShape() && particle.isAlive())
    {
        //Intialize lists of button centers and bounds
        ArrayList<Shape> shapes = buttons.getShapes();

        //loop thorugh all the shapes
        for(Shape shape: shapes)
        {
          //Get all all points that create the bounds of the particle
          for(Point2D.Double point: particle.getPoints())
          {
            //Check if any point collids with the button bounds
            if(shape.getShapeBounds().contains(point) && shape.isActive())
            {
              //Set new position if particles collid to the left of the shape
              if (shape.getCenter().getX() + shape.getSize() >= point.getX())
              {
                particle.setNewPosition(new Point2D.Double(particle.getNewPosition().getX() * - 1, particle.getNewPosition().getY()));
              }
              //Set new position if particles collid to the right of the shape
              else
              {
                particle.setNewPosition(new Point2D.Double(particle.getNewPosition().getX() , particle.getNewPosition().getY()));
              }

              //Set new position if particles collid to the bottom of the shape
              if (shape.getCenter().getY() + shape.getSize() >= point.getY())
              {
                particle.setNewPosition(new Point2D.Double(particle.getNewPosition().getX(), particle.getNewPosition().getY() * -1));
              }
              //Set new position if particles collid to the top of the shape
              else
              {
                particle.setNewPosition(new Point2D.Double(particle.getNewPosition().getX(), particle.getNewPosition().getY()));
              }
              break;
            }
          }
        }
    }
  }

  public void moveTowardButton(Point2D.Double initialClick)
  {
    //loop through all the shsapes
  	for(Shape shape: buttons.getShapes())
    {
      //check if the user clicked inside the shape and if it is active
  		if(shape.getShapeBounds().contains(initialClick) && shape.isActive())
  		{
        //set drawing type of the shape, filled
  			shape.setDrawingType(6);

        //set number counter text color to black
  			shape.setTextColor(new Color(0f,0f,0f,1f));

        //loop through all the particles
  			for(Particle particle: this.getParticles())
  			{
          //check if the particle is the same color as the shapes (single or two colored)
          //checks if particle is alive and shape is active
  				if((((particle.getColor() == shape.getColorOne() && (shape.getOneColorCount() != 0 || !shape.isTwoTone())) || (particle.getColor() == shape.getColorTwo()) && shape.getTwoColorCount() !=0)) && particle.isAlive() && shape.getNumCount() != 0)
  				{
            //set particle is moving towards a shape
  					particle.setIsMovingToShape(true);

            //get vector positions from particle to shape
  					double deltaX = shape.getCenter().getX() - particle.getPosition().getX();
  					double deltaY = shape.getCenter().getY() - particle.getPosition().getY();

            //find the angle direction the shape is from particle
  					double angle = Math.atan2( deltaY, deltaX ) ;

            //add the angle to the particle to get the particle to move towards the shape
  					double currentX = particle.getPosition().getX() * (0.075) * Math.cos( angle );
  					double currentY = particle.getPosition().getY() * (0.075) * Math.sin( angle );

            //set the new position of the particle
  					particle.setNewPosition(new Point2D.Double(currentX, currentY));
  				}

          //check if a particle is inside the shape and checks if the particle is the same color as the shape
  				if(shape.getShapeBounds().contains(particle.getPosition()) && (particle.getColor() == shape.getColorOne() || particle.getColor() == shape.getColorTwo()) )
  				{
            //if the shape number counter is not zero (dead)
  					if(shape.getNumCount() != 0)
            {
              //check if the shape is two toned
              if(shape.isTwoTone())
              {
                //if the particle is the same color as the first color of the shape
                //and the number count for the first color is not 0
                if(particle.getColor() == shape.getColorOne() && shape.getOneColorCount() != 0)
                {
                  //decrease the first color count
                  shape.setOneColorCount(shape.getOneColorCount() - 1);

                  //set the new position of the particle to the center of the shape
                  particle.setPosition(shape.getCenter());

                  //particle is inside shape so it is not dead
                  particle.setIsAlive(false);
                }

                //if the particle is the same color as the second color of the shape
                //and the number count for the second color is not 0
                if(particle.getColor() == shape.getColorTwo() && shape.getTwoColorCount() != 0)
                {
                  //decrease the second color count
                  shape.setTwoColorCount(shape.getTwoColorCount() - 1);

                  //set the new position of the particle to the center of the shape
                  particle.setPosition(shape.getCenter());

                  //particle is inside shape so it is not dead
                  particle.setIsAlive(false);
                }

                //set the new total number count of the two toned shape
                shape.setNumCount(shape.getOneColorCount()  + shape.getTwoColorCount());
              }
              //if it is a regular single color shape
              else
              {
                //decrease the color count
                shape.setNumCount(shape.getNumCount() - 1);

                //set the new position of the particle to the center of the shape
                particle.setPosition(shape.getCenter());

                //particle is inside shape so it is not dead
                particle.setIsAlive(false);
              }
          }
          else
          {
            //set shape count to zero
            shape.setNumCount(0);
          }
        }

        //if shape count is zero
        if(shape.getNumCount() == 0)
        {
          //set particle moving to it false
          particle.setIsMovingToShape(false);

          //set shape active false
          shape.setIsActive(false);
        }

      }

        //check if shape is twotone and is not active
        //this is where when the two tone shape disappears
        //it will spawn the new special color
        if(shape.isTwoTone() && !shape.isActive())
        {
          //loop through all the shape
          for(Shape mixShape: buttons.getShapes())
          {
            //find the mix color shape
            if(mixShape.isMixColor())
            {
              //create new color particles of the mix color
              for(int i = 0; i < mixShape.getNumCount()/2; i++)
              {
                //intialize particle
                Particle newParticle = new Particle();

                //set color of the particle
                newParticle.setColor(mixShape.getColorOne());

                //set beginning of the particle position to center of the shape
                newParticle.setPosition(shape.getCenter());

                //find a good random angle the particle will burst out towads
                double angle = 2 * Math.PI * rand.nextInt(mixShape.getNumCount()) / mixShape.getNumCount();

                //set the new position of the particle
                newParticle.setNewPosition(new Point2D.Double(newParticle.getPosition().getX() * (0.0055) * Math.cos(angle) * (rand.nextBoolean() ? -1 : 1),
                                                           newParticle.getPosition().getY() * (0.0055)  * Math.sin(angle) * (rand.nextBoolean() ? -1 : 1)));

                //set the size of the particle
                newParticle.setSize(this.size / 1000.0);

                //set the mass of the particle
                newParticle.setMass(((float)(4 / 3 * Math.PI * Math.pow(newParticle.getSize(), 3.0))));

                //add the new particle to particles
                particles.add(newParticle);

                //draw the particles
                drawParticle(newParticle);

              }
              //set mix shape to false so the random particle waves will now spawn the new color
              mixShape.setIsMixedColor(false);
            }
          }
        }
  		}
  	}

    //initlize array of for alive particles
  	ArrayList<Particle> aliveParticles = new ArrayList<Particle>();

    //loop through all the particles
  	for(Particle particle: this.getParticles() )
  	{
      //check if the particle is alive
  		if(particle.isAlive())
      {
        //add the alive particle to the alive array
  			aliveParticles.add(particle);
  		}
  	}

    //set thea live partticle array to the intial particle array
  	this.setParticles(aliveParticles);
  }

  public void updateOnRelease()
  {
    //intialize variables
    boolean inside = false;
    Random rand = new Random();

    //loop through all the shape
    for(Shape shape: buttons.getShapes())
    {
      //set the drawing type to outline
      shape.setDrawingType(2);

      //set the shape number counter text color white
      shape.setTextColor(new Color(1f,1f,1f,1f));

      //looop through all the particles
      for(Particle particle: this.getParticles())
      {
        //check if the particle is still inside the screen
        if(shape.getShapeBounds().contains(particle.getPosition()))
        {
          //set the particle life as false
          particle.setIsAlive(false);
        }
      }
    }

    //this generates a new wave of particles
    generateParticles();

    //loop through all particles
    for(Particle particle: particles)
    {
      //check if particle is moving towareds a shape
      if(particle.isMovingToShape())
      {
        //set the new speed of the particle
        particle.setNewPosition(new Point2D.Double(particle.getPosition().getX() * (0.0055) * (rand.nextBoolean() ? -1 : 1),
                                                   particle.getPosition().getY() * (0.0055) * (rand.nextBoolean() ? -1 : 1)));
      }
      //set moving to shape as false
      particle.setIsMovingToShape(false);
    }
  }

  /**************************************************
           Getters for particle effect class
   **************************************************/

  public int getColorCount(Color color)
  {
    int count = 0;

    for(Particle particle: particles){
      if(particle.getColor() == color){
        count++;
      }
    }
    return count;
  }

  public ArrayList<Particle>  getParticles()
  {
    return this.particles;
  }

  /**************************************************
           Setters for particle effect class
   **************************************************/

  public void setParticles(ArrayList<Particle> particles)
  {
    this.particles = particles;
  }

  public void setInitial(boolean initial){
    this.initial = initial;
  }
  /**************************************************
           Creating the colors of the particle
      Either some are transparent or in full color
   **************************************************/
  private void	setColor(GL2 gl, Color color, int alpha)
  {
    gl.glEnable(gl.GL_BLEND);
    gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA);
    gl.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, alpha / 255.0f);

  }

  private void	setColor(GL2 gl, Color color)
  {
    setColor(gl, color, 255);
  }


}
