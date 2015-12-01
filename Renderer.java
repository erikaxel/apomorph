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
	 * Konstrukt�r
	 * @param resolution resolusjonen vi er i.
	 */
	public Renderer( DisplayMode resolution ) {
		_height = resolution.getHeight();
		_width = resolution.getWidth();
		done = false;
	}

	/**
	 * render skal v�re loopen i rendermaskinen.
	 * @param g Graphic � tegne p�.
	 */
 	public abstract void render( Graphics2D g );

	public void setResolution( DisplayMode resolution ) {
		_height = resolution.getHeight();
		_width = resolution.getWidth();
	}


	/**
	 * Returnerer h�yden p� modusen vi er i.
	 * @return H�yden p� oppl�sningen
	 */
	public int getHeight() {
		return _height;
	}

	/**
	 * Returnerer bredden p� modusen vi er i.
	 * @return Bredden p� oppl�sningen.
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
