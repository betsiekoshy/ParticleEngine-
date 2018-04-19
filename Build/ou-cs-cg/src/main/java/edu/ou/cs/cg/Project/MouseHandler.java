package edu.ou.cs.cg.Project;


import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public final class MouseHandler extends MouseAdapter
{
	//**********************************************************************
	// Private Members
	//**********************************************************************

	// State (internal) variables
	private final Driver	driver;

	//**********************************************************************
	// Constructors and Finalizer
	//**********************************************************************

	public MouseHandler(Driver driver)
	{
		this.driver = driver;

		Component	component = driver.getComponent();

		component.addMouseListener(this);
		component.addMouseMotionListener(this);
		component.addMouseWheelListener(this);
	}

	//**********************************************************************
	// Override Methods (MouseListener)
	//**********************************************************************

	public void		mouseClicked(MouseEvent e)
	{

	}

	public void		mouseEntered(MouseEvent e)
	{
	}

	public void		mouseExited(MouseEvent e)
	{
	}

	public void		mousePressed(MouseEvent e)
	{

	}

	public void		mouseReleased(MouseEvent e)
	{
		driver.particleEffect.generateParticles(50);
	}

	//**********************************************************************
	// Override Methods (MouseMotionListener)
	//**********************************************************************

	public void		mouseDragged(MouseEvent e)
	{

	}

	public void		mouseMoved(MouseEvent e)
	{

	}

	//**********************************************************************
	// Override Methods (MouseWheelListener)
	//**********************************************************************

	public void		mouseWheelMoved(MouseWheelEvent e)
	{
	}

	//**********************************************************************
	// Private Methods
	//**********************************************************************

	private Point2D.Double	calcCoordinatesIndriver(int sx, int sy)
	{
		int				w = driver.getWidth();
		int				h = driver.getHeight();
		Point2D.Double	p = new Point2D.Double(0,0);
		double			vx = p.x + (sx * 2.0) / w - 1.0;
		double			vy = p.y - (sy * 2.0) / h + 1.0;

		return new Point2D.Double(vx, vy);
	}
}

//******************************************************************************
