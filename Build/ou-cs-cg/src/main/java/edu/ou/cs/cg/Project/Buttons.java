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


public final class Buttons
{
  private static int numButtons = 5;
  private double mass;

  private ArrayList<Path2D.Double> buttonShapes;
  private static ArrayList<Shape>  shapes = new ArrayList<Shape>();
  private static ArrayList<Color> colorUsed = new ArrayList<Color>();

  private static boolean initial = true;

  private static final Point2D.Double[] centerPoints = new Point2D.Double[]
  {
    new Point2D.Double (0,0),
    new Point2D.Double (0.5,0.5),
    new Point2D.Double (-0.5,0.5),
    new Point2D.Double (0.5,-0.5),
    new Point2D.Double (-0.5,-0.5),
  };

  private static final boolean[] centerIsActive = new boolean[]
  {
    true,
    true,
    true,
    true,
    true,
  };

  private static final Color[] colors = new Color[]
  {
    new Color(255,0,0),
    new Color(0,255,0),
    new Color(0,0,255),
  };

  private static final int[] sides = new int[]
  {
    8,
    18,
    4,
  };



  public Buttons(GL2 gl)
  {
    //generate mass for the button
    //Shape shape = new Shape();

    buttonShapes = new ArrayList<Path2D.Double>();

    if(initial)
    {
        generateShapes();
        initial = false;

    }


    drawButtons(gl);


  }

  public void generateShapes()
  {
    Random rand = new Random();

    for(int i = 0; i < numButtons; i++){
      int k = rand.nextInt(colors.length);

      if(!colorUsed.contains(colors[k])){
        colorUsed.add(colors[k]);
      }

      Shape shape = new Shape(sides[k], colors[k], centerPoints[i]);
      shapes.add(shape);
    }
  }

  private void drawButtons(GL2 gl)
  {
    for(Shape shape: this.shapes)
    {

      //set the color of the button
      setColor(gl, shape.getColor());
      //Initalize array to create path2d.double
      ArrayList<Point2D.Double> shapePoints = new ArrayList<Point2D.Double>();

      // Begin drawing
      //gl.glBegin(GL.GL_TRIANGLE_FAN);
      gl.glBegin(GL.GL_LINE_LOOP);


      // Loops around in a circle depending on the sides set
      for(int i = 0; i <= shape.getSides(); i++)
      {
        // Figure out the angle of the particle
        Double angle = 2 * Math.PI * i / shape.getSides();

        // Calculates the final x and y position of the particle
        Double x = shape.getCenter().getX() + shape.getSize() * Math.cos(angle);
        Double y = shape.getCenter().getY() + shape.getSize() * Math.sin(angle);

        // Draws the section
        gl.glVertex2d(x,y);

        //add coordiate points to shapePoints
        shapePoints.add(new Point2D.Double(x,y));
      }
      // Completed in drawing the particle
      gl.glEnd();

      //create the path2d.double with the shapes
      Path2D.Double shapeBounds = createShape(shapePoints);

      shape.setShapeBounds(shapeBounds);

    }
    // for(int j = 0; j < centerPoints.length; j++)
    // {
    //   //set the color of the button
    //   setColor(gl, colors[j]);
    //
    //
    //   //Initalize array to create path2d.double
    //   ArrayList<Point2D.Double> shapePoints = new ArrayList<Point2D.Double>();
    //
    //   // Begin drawing
    //   //gl.glBegin(GL.GL_TRIANGLE_FAN);
    //   gl.glBegin(GL.GL_LINE_LOOP);
    //
    //   // Loops around in a circle depending on the sides set
    //   for(int i = 0; i <= this.sides; i++)
    //   {
    //     // Figure out the angle of the particle
    //     Double angle = 2 * Math.PI * i / this.sides;
    //
    //     // Calculates the final x and y position of the particle
    //     Double x = centerPoints[j].getX() + this.size * Math.cos(angle);
    //     Double y = centerPoints[j].getY() + this.size * Math.sin(angle);
    //
    //     // Draws the section
    //     gl.glVertex2d(x,y);
    //
    //     //add coordiate points to shapePoints
    //     shapePoints.add(new Point2D.Double(x,y));
    //   }
    //   // Completed in drawing the particle
    //   gl.glEnd();
    //
    //   //create the path2d.double with the shapes
    //   Path2D.Double shape = createShape(shapePoints);
    //
    //   buttonShapes.add(shape);
    // }
  }


  private Path2D.Double createShape(ArrayList<Point2D.Double> shapePoints)
  {
    //store the bounds of the button
    Path2D.Double result = new Path2D.Double();

    result.moveTo(shapePoints.get(0).getX(), shapePoints.get(0).getY());

    for(int i = 1; i < shapePoints.size(); i++){
      result.lineTo(shapePoints.get(i).getX(), shapePoints.get(i).getY());
    }

    result.lineTo(shapePoints.get(0).getX(), shapePoints.get(0).getY());
    result.closePath();

    return result;
  }


  //**********************************************************************
	// Public Class Methods (Event Handling)
	//**********************************************************************

  public static ArrayList<Color> getColors()
  {
    return colorUsed;
  }

  public ArrayList<Path2D.Double> getButtonShapes()
  {
    return buttonShapes;
  }

  public ArrayList<Shape> getShapes()
  {
    return this.shapes;
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
