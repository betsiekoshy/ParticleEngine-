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
	private int				gameOverCounter = 0;
	private static int				gameCounter = 0;
	private int 			complimentCounter = 0;
	private int 			numGamesPlayed = 0;
	private TextRenderer	renderer;


	private static final String[] compliments = new String[]
	{
		"Nice Job!",
		"Amazing!",
		"Fantastic!",
		"Awesome!",
		"Marvelous!",
		//"Wowzers!",
		//"Mind-Boggling!",
		//"Astounding"
	};


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
		//gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glShadeModel(GL2.GL_SMOOTH);
		//gl.glEnable(GL.GL_TEXTURE_2D);

		gl.glMatrixMode(gl.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(0.0f, 2f, 2f, 0.0f, 0.0f, 1.0f);
		//draw the bounds of the screen
		drawBounds(gl);

		//create the buttons
		buttons = new Buttons(gl, numGamesPlayed);

		//draw the particles
		particleEffect = new ParticleEffect(gl, buttons, bounds, this.getComponent());

		displayNumberCounter();

		displayAchievement();

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

	private void	displayAchievement()
	{
		Random rand = new Random();
		String name = "";
		boolean gameOver = true;
		boolean nextRound = true;
		boolean stillActiveShapes = false;
		renderer = new TextRenderer(new Font("Serif", Font.PLAIN, 50), true, true);

		for(Shape shape: buttons.getShapes())
		{
			if(shape.isActive() && !shape.isMixColor() && !shape.isTwoTone())
			{
				gameOver = false;
			}

			if(shape.isActive()){
				nextRound = false;
			}
		}

		for(Particle particle: particleEffect.getParticles())
		{
			if(bounds.contains(particle.getPosition()))
			{
				gameOver = false;
			}
		}

		for(Shape shape: buttons.getShapes())
		{
			if((shape.isMixColor() && shape.isActive()) || (shape.isTwoTone() && shape.isActive()))
			{
				int colorCountOne = particleEffect.getColorCount(shape.getColorOne());
				int colorCountTwo = particleEffect.getColorCount(shape.getColorTwo());

				if(gameOver)
				{
					if(colorCountOne >= shape.getOneColorCount() || colorCountTwo >= shape.getTwoColorCount())
					{
						gameOver = false;
					}
				}


			}
		}




		// if(nextRound)
		// {
		// 	for(Shape shape: buttons.getShapes())
		// 	{
		// 		if((shape.isMixColor() && shape.isActive()) || (shape.isTwoTone() && shape.isActive()))
		// 		{
		// 			stillActiveShapes = true;
		// 		}
		// 	}
		// }



		if(nextRound)
		{
			gameOverCounter++;

			//Intilalize String to store name
			name = compliments[complimentCounter];

		}

		if(gameOver)
		{
			gameOverCounter++;

			//Intilalize String to store name
			name = "GameOver";
			numGamesPlayed = 0;
		}

		if(gameOverCounter == 200)
		{
			if(complimentCounter == compliments.length - 1)
			{
				complimentCounter = 0;
			}
			else
			{
				complimentCounter++;
			}

			buttons.setInitial(true);
			particleEffect.setInitial(true);
			gameOverCounter = 0;
			numGamesPlayed++;
		}

		//Begin rendering the Text
		renderer.beginRendering(this.getWidth(), this.getHeight());

		//Set the color of the Text
		renderer.setColor(1f,1f,1f,1f);

		//Draw/Dispaly the name
		renderer.draw(name, 270, 368);

		//Finish rendering
		renderer.endRendering();

	}

	private void	displayNumberCounter()
	{
		renderer = new TextRenderer(new Font("Serif", Font.PLAIN, 18), true, true);

		for(Shape shape: buttons.getShapes())
		{
			if(shape.isActive())
			{
				//Intilalize String to store name
				String name = shape.getNumCount() + "";
				//Begin rendering the Text
				renderer.beginRendering(this.getWidth(), this.getHeight());

				//Set the color of the Text
				renderer.setColor(shape.getTextColor());

				//Draw/Dispaly the name
				renderer.draw(name,  (int) shape.getTextPoint().getX(), (int) shape.getTextPoint().getY());

				//Finish rendering
				renderer.endRendering();
			}
		}
	}

}
