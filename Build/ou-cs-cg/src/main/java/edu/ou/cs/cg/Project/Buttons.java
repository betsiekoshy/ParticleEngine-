package edu.ou.cs.cg.Project;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
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
  private static int numGamesPlayed;
  private double mass;

  private static ArrayList<Shape>  shapes = new ArrayList<Shape>();
  private static ArrayList<Color> colorUsed = new ArrayList<Color>();

  private static boolean initial = true;

  private static final Point2D.Double[] centerPoints = new Point2D.Double[]
  {
    new Point2D.Double (1.0,1.0),
    new Point2D.Double (0.5,0.5),
    new Point2D.Double (1.5,1.5),
    new Point2D.Double (0.5,1.5),
    new Point2D.Double (1.5,0.5),
  };

  private static final Point2D.Double[] textPoint = new Point2D.Double[]
  {
    new Point2D.Double (366, 368), //center
    new Point2D.Double (178, 555), //top left
    new Point2D.Double (555, 181), //bottom right
    new Point2D.Double (178, 181), //bottom left
    new Point2D.Double (555, 555), // top right

  };

  private static final Color[] colors = new Color[]
  {
    new Color(255,0,0),
    new Color(0,255,0),
    new Color(0,0,255),
    new Color(255,255,0),
    new Color(0,255,255),
    new Color(255,0,255),
  };

  private static final int[] sides = new int[]
  {
    3,
    12,
    18,
    4,
    6,
    8,
  };

  public int getNumButtons()
  {
    return this.numButtons;
  }

  public void setNumButtons(int b)
  {
    this.numButtons = b;
  }

  public Buttons(GL2 gl, int numGamesPlayed)
  {
    this.numGamesPlayed = numGamesPlayed;

    if(initial)
    {
        shapes = new ArrayList<Shape>();
        generateShapes();
        initial = false;
    }
    drawButtons(gl);
  }

  public void generateShapes()
  {
    Random rand = new Random();

    if(numGamesPlayed > 3)
    {
      for(int i = 0; i < numButtons; i++)
      {
        int k = rand.nextInt(colors.length/2);

        if(!colorUsed.contains(colors[k])){
          colorUsed.add(colors[k]);
        }

        Shape shape = new Shape(sides[k], colors[k], null, centerPoints[i], textPoint[i]);
        shapes.add(shape);
      }
    }
    else
    {
      Shape shape = null;

      Color colorOne = null;
      Color colorTwo = null;

      int colorOneSides = 0;
      int colorTwoSides = 0;

      int numCount = 0;

      int mixColor = (colors.length/2 + (int)(Math.random() * (((colors.length - 1) - colors.length/2) + 1)));

      switch(mixColor){
        case 3:
          colorOne = colors[0];
          colorTwo = colors[1];

          colorOneSides = 0;
          colorTwoSides = 1;
          break;
        case 4:
          colorOne = colors[1];
          colorTwo = colors[2];

          colorOneSides = 1;
          colorTwoSides = 2;
          break;
        case 5:
          colorOne = colors[0];
          colorTwo = colors[2];

          colorOneSides = 0;
          colorTwoSides = 2;
          break;
      }

      colorUsed.add(colorOne);
      colorUsed.add(colorTwo);

      for(int i = 0; i < numButtons; i++)
      {
        switch(i){
          case 0:
            shape = new Shape(sides[colorOneSides], colorOne, null, centerPoints[i], textPoint[i]);
            break;
          case 1:
            shape = new Shape(sides[colorTwoSides], colorTwo, null, centerPoints[i], textPoint[i]);
            break;
          case 2:
            shape = new Shape(sides[mixColor], colors[mixColor], null, centerPoints[i], textPoint[i]);
            shape.setIsMixedColor(true);
            numCount = shape.getNumCount();
            break;
          case 3:
            shape = new Shape(sides[mixColor], colorOne, colorTwo, centerPoints[i], textPoint[i]);
            shape.setNumCount(numCount);
            shape.setTwoTone(true);
            shape.setOneColorCount(numCount/2);
            shape.setTwoColorCount(shape.getNumCount() - shape.getOneColorCount());
            break;
          case 4:
            int randShape = (rand.nextBoolean() ? colorOneSides : colorTwoSides) ;
            shape = new Shape(sides[randShape], colors[randShape], null, centerPoints[i], textPoint[i]);
            break;
        }

        shapes.add(shape);
      }
    }
  }

  private void drawButtons(GL2 gl)
  {
    colorUsed = new ArrayList<Color>();
    //Collections.shuffle(this.shapes);

    for(Shape shape: this.shapes)
    {
      if(shape.isActive())
      {

        if(!colorUsed.contains(shape.getColorOne()) && !shape.isMixColor()){
          colorUsed.add(shape.getColorOne());
        }
        //set the color of the button
        setColor(gl, shape.getColorOne());
        //Initalize array to create path2d.double
        ArrayList<Point2D.Double> shapePoints = new ArrayList<Point2D.Double>();

        // Begin drawing
        //gl.glBegin(GL.GL_TRIANGLE_FAN); //6
        gl.glBegin(shape.getDrawingType()); // 2

        // Loops around in a circle depending on the sides set
        for(int i = 0; i <= shape.getSides(); i++)
        {
          if(shape.isTwoTone() && i == (int) shape.getSides()/2)
          {
              setColor(gl, shape.getColorTwo());
          }
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
    }
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

  public ArrayList<Shape> getShapes()
  {
    return this.shapes;
  }

  public void setShapes(ArrayList<Shape> shapes)
  {
    this.shapes = shapes;
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

  public void setInitial(boolean initial){
    this.initial = initial;
  }

}
