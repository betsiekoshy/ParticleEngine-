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
  public static final int NUMBER_OF_PARTICLES_OVERALL = 75;                      // Overall number of particles in the application

  private static ArrayList<Particle> particles = new ArrayList<Particle>(NUMBER_OF_PARTICLES_OVERALL);    // All the particles in the application

  private Particle particle;                // Single particle

  private Color[] RGB;                      // Default colors of the particles (Red, Green, or Blue)

  private static Random rand;               // For Random Number generator

  private static int i;                     // Keeps track of how many particles are in the application so far
  private final int sides = 18;             // Number of sides for the particle
  private final int minSize = 10;           // Minimum size for particles
  private final int maxSize = 20;           // Maximum size for particles

  private double angle;                     // Angle to create the particle circle
  private double x;                         // Final x postion for the random particle
  private double y;                         // Final y postion for the random particle

  private double boundaryX;                 // GL boundary X axis
  private double boundaryY;                 // GL boundary Y axis

  private Buttons buttons;


  public ParticleEffect(GL2 gl, Buttons buttons)
  {
    this.buttons = buttons;
    // Initilize the Color array for RGB
    //this.RGB = new Color[]{new Color(255,0,0), new Color(0,255,0), new Color(0,0,255)};
    this.RGB = buttons.getColors();

    // Initialize boundaries
    //boundaryX =

    // At the beginning, create all the particles
    if(i < NUMBER_OF_PARTICLES_OVERALL)
    {
      // Initialize and add randomized variables for all particles
      for(i = 0; i < NUMBER_OF_PARTICLES_OVERALL; i++)
      {
        // Create a single particle
        particle = new Particle();

        // Gives the particle random initial variables
        RandomizeParticle(particle);

        // Add particles to the ArrayList of particles
        particles.add(particle);

        // Draw the particle
        drawParticle(gl, particle);
      }
    }

    else
    {
      // Loops through the arraylist of particles
      for(Particle particle: particles)
      {
        // Update the particle varaibles (this is where the particle will move, etc)
        particle.update(buttons);

        // Redraw the particle
        drawParticle(gl, particle);
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
    boolean intersects = false;
    double randomX = 0;
    double randomY = 0;
    double newRandomX = 0;
    double newRandomY = 0;

    do{
      randomX = rand.nextInt(100) / 100.0 * (rand.nextBoolean() ? -1 : 1);
      randomY = rand.nextInt(100) / 100.0 * (rand.nextBoolean() ? -1 : 1);

      newRandomX = randomX * (.0055)* (rand.nextBoolean() ? -1 : 1);
      newRandomY = randomY * (.0055)* (rand.nextBoolean() ? -1 : 1);

      ArrayList<Path2D.Double> buttonPaths = buttons.getButtonShapes();

      for(Path2D.Double path: buttonPaths)
      {
        if(!path.contains(new Point2D.Double(randomX, randomY))){
          intersects = true;
        }
      }
    }while(!intersects);

    // Gives the Particle a Random Position between -1 to 1
    particle.setPosition(
                new Point2D.Double(randomX, randomY));

    //Gives the Particle a new Random Position between -1 to 1
    particle.setNewPosition(
                new Point2D.Double(newRandomX, newRandomY));

    // Gives the Particle the Color Red, Green, or Blue
    //particle.setColor(RGB[rand.nextInt(3)]);
    particle.setColor(RGB[rand.nextInt(RGB.length)]);

    // Gives the Particle a Random Size
    particle.setSize((rand.nextInt((this.maxSize - this.minSize) + 1) + this.minSize) / 1000.0);


  }



  /**************************************************
         Creating the actual particle (circles)
   **************************************************/
  private void drawParticle(GL2 gl, Particle particle)
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

}
