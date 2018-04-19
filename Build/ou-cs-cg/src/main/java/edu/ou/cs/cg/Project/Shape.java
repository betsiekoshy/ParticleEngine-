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

  private double size;
  private double mass;

  private Color color;

  private Point2D.Double centerPoint;

  private boolean isActive;


  public Shape(int sides, Color color, Point2D.Double centerPoint)
  {
    this.size = 0.14;
    this.sides = sides;
    this.color = color;
    this.centerPoint = centerPoint;
    this.mass = ((float)(4 / 3 * Math.PI * Math.pow(this.size, 3.0)));
    this.isActive = true;

  }
  //**********************************************************************
	// Public Class Methods (Event Handling)
	//**********************************************************************
  public int getSides()
  {
    return sides;
  }

  public double getSize()
  {
    return this.size;
  }

  public double getMass()
  {
    return this.mass;
  }

  public Color getColors()
  {
    return color;
  }

  public Point2D.Double getCenter()
  {
    return centerPoint;
  }

  public boolean getIsActive()
  {
    return isActive;
  }

}
