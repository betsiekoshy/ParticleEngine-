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
  private int sides;
  private int numCount;

  private double size;
  private double mass;

  private Color color;
  private Color textColor;

  private Point2D.Double centerPoint;
  private Point2D.Double textPoint;
  private Path2D.Double shapeBounds;

  private boolean isActive;

  private int drawingType;


  public Shape(int sides, Color color, Point2D.Double centerPoint, Point2D.Double textPoint)
  {
    Random rand = new Random();
    this.size = 0.14;
    this.sides = sides;
    this.color = color;
    this.centerPoint = centerPoint;
    this.textPoint = textPoint;
    this.mass = ((float)(4 / 3 * Math.PI * Math.pow(this.size, 3.0)));
    this.isActive = true;
    this.drawingType = 2;
    this.numCount = (10 + (int)(Math.random() * ((40 - 10) + 1)));
    //this.numCount = (1 + (int)(Math.random() * ((9 - 1) + 1)));
    this.textColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);
  }

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

  public void setIsActive(boolean isActive){
    this.isActive = isActive;
  }


  //**********************************************************************
	// Public Class Methods (Event Handling)
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

  public void setDrawingType(int drawingType)
  {
    this.drawingType = drawingType;
  }

  public int getDrawingType()
  {
    return this.drawingType;
  }

  public double getMass()
  {
    return this.mass;
  }

  public Color getColor()
  {
    return color;
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

  public boolean isActive()
  {
    return isActive;
  }

  public Path2D.Double getShapeBounds()
  {
    return this.shapeBounds;
  }

}
