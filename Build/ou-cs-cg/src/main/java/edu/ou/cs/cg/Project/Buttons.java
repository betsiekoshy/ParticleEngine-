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
  private int sides = 18;

  private double size  = 0.14;

  private ArrayList<Path2D.Double> buttonShapes;

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
    new Color(0,255,0),
    new Color(0,0,255),
  };

  //**********************************************************************
	// Public Class Methods (Event Handling)
	//**********************************************************************
  public static Point2D.Double[] getAllCenterPoints()
  {
    return centerPoints;
  }

  public static boolean[] getAllcenterIsActive()
  {
    return centerIsActive;
  }

  public static Color[] getColors()
  {
    return colors;
  }

  public ArrayList<Path2D.Double> getButtonShapes()
  {
    return buttonShapes;
  }

  public Buttons(GL2 gl)
  {
    buttonShapes = new ArrayList<Path2D.Double>();
    drawButtons(gl);
  }

  private void drawButtons(GL2 gl)
  {
    // Set the color of the particle


    for(int j = 0; j < centerPoints.length; j++)
    {
      setColor(gl, colors[j]);
      //Initalize array to create path2d.double
      ArrayList<Point2D.Double> shapePoints = new ArrayList<Point2D.Double>();

      // Begin drawing
      //gl.glBegin(GL.GL_TRIANGLE_FAN);
      gl.glBegin(GL.GL_LINE_LOOP);

      // Loops around in a circle depending on the sides set
      for(int i = 0; i <= this.sides; i++)
      {
        // Figure out the angle of the particle
        Double angle = 2 * Math.PI * i / this.sides;

        // Calculates the final x and y position of the particle
        Double x = centerPoints[j].getX() + this.size * Math.cos(angle);
        Double y = centerPoints[j].getY() + this.size * Math.sin(angle);

        // Draws the section
        gl.glVertex2d(x,y);

        //add coordiate points to shapePoints
        shapePoints.add(new Point2D.Double(x,y));
      }
      // Completed in drawing the particle
      gl.glEnd();

      //create the path2d.double with the shapes
      Path2D.Double shape = createShape(shapePoints);

      buttonShapes.add(shape);
    }
  }

  private Path2D.Double createShape(ArrayList<Point2D.Double> shapePoints)
  {
    Path2D.Double result = new Path2D.Double();

    result.moveTo(shapePoints.get(0).getX(), shapePoints.get(0).getY());

    for(int i = 1; i < shapePoints.size(); i++){
      result.lineTo(shapePoints.get(i).getX(), shapePoints.get(i).getY());
    }

    result.lineTo(shapePoints.get(0).getX(), shapePoints.get(0).getY());
    result.closePath();

    return result;
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
