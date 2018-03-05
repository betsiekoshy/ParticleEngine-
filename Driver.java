
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
import com.sun.org.apache.xerces.internal.impl.dv.xs.YearMonthDV;


public final class Driver
implements GLEventListener
{


	//private ArrayList<Particle> particles = new ArrayList<Particle>(500);

	public static final GLU		GLU = new GLU();
	public static final GLUT	GLUT = new GLUT();
	public static final Random	RANDOM = new Random();

	

	

	// State (internal) variables
	private int				k = 0;		// Just an animation counter

	private int				w;			// Canvas width
	private int				h;			// Canvas height
	private TextRenderer	renderer;


	public static void main(String[] args)
	{
		GLProfile		profile = GLProfile.getDefault();
		GLCapabilities	capabilities = new GLCapabilities(profile);
		GLCanvas		canvas = new GLCanvas(capabilities);
		JFrame			frame = new JFrame("Particles");

		canvas.setPreferredSize(new Dimension(800, 800));

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

		canvas.addGLEventListener(new Driver());

		FPSAnimator		animator = new FPSAnimator(canvas, 60);

		animator.start();
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

		setProjection(gl);						// Use a coordinate system

		
	}

	// Coordinate System
	
	private void	setProjection(GL2 gl)
	{
		GLU		glu = new GLU();

		gl.glMatrixMode(GL2.GL_PROJECTION); // Prepare for matrix xform
		gl.glLoadIdentity();                // Set to identity matrix

		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();

		gl.glTranslatef(0.0f, 0.0f, -10.0f);
		gl.glScalef(2.0f, 2.0f, 2.0f);
		//glu.gluOrtho2D(-2.0f, 2.0f, -2.0f, 2.0f);	// 2D translate and scale
		

	}

	
}
