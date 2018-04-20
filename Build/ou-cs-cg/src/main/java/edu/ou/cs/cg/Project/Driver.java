package edu.ou.cs.cg.Project;

import edu.ou.cs.cg.Project.ParticleEffect;
import edu.ou.cs.cg.Project.Buttons;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.Random;
import javax.swing.*;
import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.gl2.GLUT;


import java.text.DecimalFormat;
import java.util.*;

import javax.media.opengl.awt.*;



public final class Driver implements GLEventListener
{

	public static final GLU		GLU = new GLU();
	public static final GLUT	GLUT = new GLUT();
	public static final Random	RANDOM = new Random();

	public Buttons buttons;
	public ParticleEffect particleEffect;
	public Path2D.Double bounds;

	private final GLJPanel			canvas;
	private final MouseHandler		mouseHandler;
	private final FPSAnimator		animator;
	private static final int				DEFAULT_FRAMES_PER_SECOND = 60;

	// State (internal) variables
	private int				k = 0;		// Just an animation counter
	private int				w;			// Canvas width
	private int				h;			// Canvas height
	private TextRenderer	renderer;


	public static void main(String[] args)
	{
		GLProfile		profile = GLProfile.getDefault();
		GLCapabilities	capabilities = new GLCapabilities(profile);
		GLJPanel		canvas = new GLJPanel(capabilities);
		JFrame			frame = new JFrame("Particles");

		canvas.setPreferredSize(new Dimension(750, 750));

		frame.setBounds(50, 50, 600, 600);
		frame.getContentPane().add(canvas);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		frame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			});

		//canvas.addGLEventListener(new Driver());


		Driver driver = new Driver(canvas);
	}

	public Driver(GLJPanel canvas)
	{
		this.canvas = canvas;

		// Initialize rendering
		this.canvas.addGLEventListener(this);

		animator = new FPSAnimator(canvas, DEFAULT_FRAMES_PER_SECOND);
		animator.start();


		mouseHandler = new MouseHandler(this);
	}

	public Component	getComponent()
	{
		return (Component)canvas;
	}

	public int	getWidth()
	{
		return this.w;
	}

	public int	getHeight()
	{
		return this.h;
	}

	public Path2D.Double getBounds(){
		return this.bounds;
	}


	// Override Methods (GLEventListener)


    public void		init(GLAutoDrawable drawable)
	{
		w = drawable.getWidth();
		h = drawable.getHeight();

		renderer = new TextRenderer(new Font("Serif", Font.PLAIN, 18),
									true, true);
	}

	public void		dispose(GLAutoDrawable drawable)
	{
		renderer = null;
	}

	public void		display(GLAutoDrawable drawable)
	{
		update();
		render(drawable);
	}

	public void		reshape(GLAutoDrawable drawable, int x, int y, int w, int h)
	{
		this.w = w;
		this.h = h;
	}

	// Private Methods (Rendering)
	private void	update()
	{
		k++;
	}

	private void	render(GLAutoDrawable drawable)
	{
		GL2		gl = drawable.getGL().getGL2();

		gl.glClear(GL.GL_COLOR_BUFFER_BIT);		// Clear the buffer

		gl.glMatrixMode(gl.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(0.0f, 2f, 2f, 0.0f, 0.0f, 1.0f);
		//draw the bounds of the screen
		drawBounds(gl);

		//create the buttons
		buttons = new Buttons(gl);

		//draw the particles
		particleEffect = new ParticleEffect(gl, buttons, bounds);

	}

	private void	drawBounds(GL2 gl)
	{
		bounds = new Path2D.Double();

		bounds.moveTo(0, 0);
		bounds.lineTo(0, 2);
		bounds.lineTo(2, 2);
		bounds.lineTo(2, 0);
		bounds.lineTo(0, 0);
	}

}
