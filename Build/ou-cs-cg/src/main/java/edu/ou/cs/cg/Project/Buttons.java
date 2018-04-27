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
  private static int numButtons = 5;      //number of buttons to draw
  private static int numGamesPlayed;      //number of games users played so far
  private double mass;                    //mass of the buttons

  private static ArrayList<Shape>  shapes = new ArrayList<Shape>();     //keeps track of all the shapes
  private static ArrayList<Color> colorUsed = new ArrayList<Color>();   //keeps track of colors used for the shape

  private static boolean initial = true;   //check if this is the beinning of a stage

  private static final Point2D.Double[] centerPoints = new Point2D.Double[]     //all center points for the shapes
  {
    new Point2D.Double (1.0,1.0),
    new Point2D.Double (0.5,0.5),
    new Point2D.Double (1.5,1.5),
    new Point2D.Double (0.5,1.5),
    new Point2D.Double (1.5,0.5),
  };

  private static final Point2D.Double[] textPoint = new Point2D.Double[]        //all positions for the number counter assigned to the shape
  {
    new Point2D.Double (366, 368), //center
    new Point2D.Double (178, 555), //top left
    new Point2D.Double (555, 181), //bottom right
    new Point2D.Double (178, 181), //bottom left
    new Point2D.Double (555, 555), //top right

  };

  private static final Color[] colors = new Color[] //all colors used for the shapes
  {
    new Color(255,0,0),
    new Color(0,255,0),
    new Color(0,0,255),
    new Color(255,255,0),
    new Color(0,255,255),
    new Color(255,0,255),
  };

  private static final int[] sides = new int[]    //sides for the shapes
  {
    3,
    12,
    18,
    4,
    6,
    8,
  };


  public Buttons(GL2 gl, int numGamesPlayed)
  {
    //initialize number games played
    this.numGamesPlayed = numGamesPlayed;

    //check if its the beginning of a stage
    if(initial)
    {
      //initalize the shapes
      shapes = new ArrayList<Shape>();

      //generate some shapes classes
      generateShapes();

      //set intial to false
      initial = false;
    }

    //draw the all the shapes
    drawButtons(gl);
  }

  public void generateShapes()
  {
    //itnialize random
    Random rand = new Random();

    //check if the number of games is under 3
    //the stage will only consist of single color shapes
    if(numGamesPlayed < 3)
    {
      //create 5 buttons
      for(int i = 0; i < numButtons; i++)
      {
        //selects a random RGB color
        int k = rand.nextInt(colors.length/2);

        //check to see if the color is in colorused (for particles)
        if(!colorUsed.contains(colors[k]))
        {
          //add the color to colorused
          colorUsed.add(colors[k]);
        }

        //Create a new shape with the random generated color
        Shape shape = new Shape(sides[k], colors[k], null, centerPoints[i], textPoint[i]);

        //add the shape to shapes
        shapes.add(shape);
      }
    }

    //Create shapes that include special colors and two toned shapes
    else
    {
      //intialize variables
      Shape shape = null;
      Color colorOne = null;
      Color colorTwo = null;

      //color number counter for two toned
      int colorOneSides = 0;
      int colorTwoSides = 0;

      //number counter for shapes
      int numCount = 0;

      //get a random special color
      int mixColor = (colors.length/2 + (int)(Math.random() * (((colors.length - 1) - colors.length/2) + 1)));

      //check that color that was generated
      switch(mixColor)
      {
        //if the color is yellow
        case 3:
          //first color is red
          colorOne = colors[0];
          //side assoiated with red
          colorOneSides = 0;

          //second color is green
          colorTwo = colors[1];
          //side associated with green
          colorTwoSides = 1;
          break;

        //if the color is cyan
        case 4:
          //first color is green
          colorOne = colors[1];
          //side associated with green
          colorTwoSides = 1;

          //second color is blue
          colorTwo = colors[2];
          //side associated with blue
          colorTwoSides = 2;
          break;

        //if the color is magenta
        case 5:
          //first color is red
          colorOne = colors[0];
          //side assoiated with red
          colorOneSides = 0;

          //second color is blue
          colorTwo = colors[2];
          //side associated with blue
          colorTwoSides = 2;
          break;
      }

      //add the color to the colorused
      colorUsed.add(colorOne);
      colorUsed.add(colorTwo);

      //loop through all the buttons
      for(int i = 0; i < numButtons; i++)
      {
        //for each button
        switch(i)
        {
          //create a shape with the first color
          case 0:
            shape = new Shape(sides[colorOneSides], colorOne, null, centerPoints[i], textPoint[i]);
            break;

          //create a shape with the second color
          case 1:
            shape = new Shape(sides[colorTwoSides], colorTwo, null, centerPoints[i], textPoint[i]);
            break;

          //create a shape with the special color
          case 2:
            shape = new Shape(sides[mixColor], colors[mixColor], null, centerPoints[i], textPoint[i]);

            //set true for special mixed color
            shape.setIsMixedColor(true);

            //get the num count for the mixed color shape
            numCount = shape.getNumCount();
            break;

          //create the two toned shape
          case 3:
            shape = new Shape(sides[mixColor], colorOne, colorTwo, centerPoints[i], textPoint[i]);

            //set the num count the same as the mixed shape
            shape.setNumCount(numCount);

            //set two toned as true
            shape.setTwoTone(true);

            //set the num coutn for the first color to half of the total num count
            shape.setOneColorCount(numCount/2);

            //set the num coutn for the second color to half of the total num count
            shape.setTwoColorCount(shape.getNumCount() - shape.getOneColorCount());
            break;

          //create a random single color shape
          case 4:
            int randShape = (rand.nextBoolean() ? colorOneSides : colorTwoSides) ;
            shape = new Shape(sides[randShape], colors[randShape], null, centerPoints[i], textPoint[i]);
            break;
        }

        //add the shape to shapes
        shapes.add(shape);
      }
    }
  }

  private void drawButtons(GL2 gl)
  {
    //intialize color used array
    colorUsed = new ArrayList<Color>();

    //loop through all shaes
    for(Shape shape: this.shapes)
    {
      //check if shape is active to draw
      if(shape.isActive())
      {
        //make sure the color added to colorused is not already added
        if(!colorUsed.contains(shape.getColorOne()) && !shape.isMixColor())
        {
          //add the color
          colorUsed.add(shape.getColorOne());
        }

        //set the color of the button
        setColor(gl, shape.getColorOne());

        //Initalize array to create path2d.double
        ArrayList<Point2D.Double> shapePoints = new ArrayList<Point2D.Double>();

        // Begin drawing
        //get the drawing type of the shape
        gl.glBegin(shape.getDrawingType());

        // Loops around in a circle depending on the shape sides
        for(int i = 0; i <= shape.getSides(); i++)
        {
          //check if the shape is two toned
          if(shape.isTwoTone() && i == (int) shape.getSides()/2)
          {
              //if shape is two tone, after half drawing the shape change colors
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

        //add the bounds to getShapeBounds
        shape.setShapeBounds(shapeBounds);
      }
    }
  }

  //creates a path of the shape
  private Path2D.Double createShape(ArrayList<Point2D.Double> shapePoints)
  {
    //store the resulting bounds of the button
    Path2D.Double result = new Path2D.Double();

    //move to the first point
    result.moveTo(shapePoints.get(0).getX(), shapePoints.get(0).getY());

    //move around to all the other points
    for(int i = 1; i < shapePoints.size(); i++)
    {
      result.lineTo(shapePoints.get(i).getX(), shapePoints.get(i).getY());
    }

    //move back to the first point
    result.lineTo(shapePoints.get(0).getX(), shapePoints.get(0).getY());
    result.closePath();

    //return shape path
    return result;
  }


  //**********************************************************************
	// Getters for Button class
	//**********************************************************************

  public static ArrayList<Color> getColors()
  {
    return colorUsed;
  }

  public ArrayList<Shape> getShapes()
  {
    return this.shapes;
  }
  public int getNumButtons()
  {
    return this.numButtons;
  }


  //**********************************************************************
	// Setters for Button class
	//**********************************************************************

  public void setShapes(ArrayList<Shape> shapes)
  {
    this.shapes = shapes;
  }

  public void setNumButtons(int b)
  {
    this.numButtons = b;
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
    gl.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, alpha / 255.0f);
  }

  private void	setColor(GL2 gl, Color color)
  {
    setColor(gl, color, 255);
  }


}
