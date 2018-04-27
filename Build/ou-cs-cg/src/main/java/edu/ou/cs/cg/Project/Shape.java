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


public final class Shape
{
  private int sides;                    //number of sides the shape has
  private int numCount;                 //number of particles shape needs to consume
  private int drawingType;              //drawying type of the shape (outline or filled)
  private int oneColorCount;            //number of particles of this color (first color) the shape needs to consume
  private int twoColorCount;            //number of particles of this color (second color) the shape needs to consume

  private double size = 0.14;;          //size of the shape
  private double mass;                  // mass of the shape

  private Color colorOne;               //color of the shape
  private Color colorTwo;               //second color of the shape (if the shape is two toned)
  private Color textColor;              //number text color

  private Point2D.Double centerPoint;   //center point of the shape
  private Point2D.Double textPoint;     //position of the number
  private Path2D.Double shapeBounds;    //boundaries of the shape

  private boolean isActive;             //checks if the shape is active
  private boolean twoTone;              //check if the shape is two toned
  private boolean isMixColor;           //check if the shape is a special color


  public Shape(int sides, Color colorOne, Color colorTwo, Point2D.Double centerPoint, Point2D.Double textPoint)
  {
    //Intialize all variables
    Random rand = new Random();
    this.sides = sides;
    this.colorOne = colorOne;
    this.colorTwo = colorTwo;
    this.centerPoint = centerPoint;
    this.textPoint = textPoint;
    this.mass = ((float)(4 / 3 * Math.PI * Math.pow(this.size, 3.0)));
    this.isActive = true;
    this.twoTone = false;
    this.isMixColor = false;
    this.drawingType = 2;
    this.oneColorCount = 0;
    this.twoColorCount = 0;
    this.numCount = (10 + (int)(Math.random() * ((30- 10) + 1)));
    this.textColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);
  }


  //**********************************************************************
	// Setter for shape class
	//**********************************************************************

  public void setShapeBounds(Path2D.Double shapeBounds)
  {
    this.shapeBounds = shapeBounds;
  }

  public void setTextColor(Color textColor){
    this.textColor = textColor;
  }

  public void setNumCount(int numCount){
    this.numCount = numCount;
  }

  public void setOneColorCount(int oneColorCount){
    this.oneColorCount = oneColorCount;
  }

  public void setTwoColorCount(int twoColorCount){
    this.twoColorCount = twoColorCount;
  }

  public void setIsActive(boolean isActive){
    this.isActive = isActive;
  }

  public void setTwoTone(boolean twoTone)
  {
    this.twoTone = twoTone;
  }

  public void setIsMixedColor(boolean isMixColor){
    this.isMixColor = isMixColor;
  }

  public void setDrawingType(int drawingType)
  {
    this.drawingType = drawingType;
  }


  //**********************************************************************
	// Getters for shape class
	//**********************************************************************
  
  public int getNumCount()
  {
    return numCount;
  }

  public int getSides()
  {
    return sides;
  }

  public double getSize()
  {
    return this.size;
  }

  public int getDrawingType()
  {
    return this.drawingType;
  }

  public int getOneColorCount()
  {
    return this.oneColorCount;
  }

  public int getTwoColorCount()
  {
    return this.twoColorCount;
  }

  public double getMass()
  {
    return this.mass;
  }

  public Color getColorOne()
  {
    return colorOne;
  }

  public Color getColorTwo()
  {
    return colorTwo;
  }

  public Color getTextColor()
  {
    return textColor;
  }

  public Point2D.Double getCenter()
  {
    return centerPoint;
  }

  public Point2D.Double getTextPoint()
  {
    return textPoint;
  }

  public Path2D.Double getShapeBounds()
  {
    return this.shapeBounds;
  }

  public boolean isActive()
  {
    return isActive;
  }

  public boolean isTwoTone()
  {
    return twoTone;
  }

  public boolean isMixColor()
  {
    return isMixColor;
  }

}
