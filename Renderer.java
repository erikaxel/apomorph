import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Abstrakt klasse for alle renderne.
 * @author Erik Axel Nielsen (erikaxel@stud.ntnu.no)
 */

public abstract class Renderer implements KeyListener {
	public boolean done;
	public boolean quit; //trengs kanskje bare i GameRenderer?
	protected int _height, _width;

	/**
	 * Konstruktør
	 * @param resolution resolusjonen vi er i.
	 */
	public Renderer( DisplayMode resolution ) {
		_height = resolution.getHeight();
		_width = resolution.getWidth();
		done = false;
	}

	/**
	 * render skal være loopen i rendermaskinen.
	 * @param g Graphic å tegne på.
	 */
 	public abstract void render( Graphics2D g );

	public void setResolution( DisplayMode resolution ) {
		_height = resolution.getHeight();
		_width = resolution.getWidth();
	}


	/**
	 * Returnerer høyden på modusen vi er i.
	 * @return Høyden på oppløsningen
	 */
	public int getHeight() {
		return _height;
	}

	/**
	 * Returnerer bredden på modusen vi er i.
	 * @return Bredden på oppløsningen.
	 */
	public int getWidth() {
		return _width;
	}

	public void keyReleased( KeyEvent e ) {
		// NOOP
	}
	public void keyTyped( KeyEvent e ) {
		// NOOP
	}
}
