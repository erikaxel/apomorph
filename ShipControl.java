/**
 * Kontrol for skipet
 *
 * Lytter etter tastene som styrer skipet
 *
 * @author Jørgen Braseth (jorgebr@stud.ntnu.no)
 */

import java.awt.event.*;

public class ShipControl extends KeyAdapter {
	//keys
	private int APO_UP = KeyEvent.VK_UP;
	private int APO_DOWN = KeyEvent.VK_DOWN;
	private int APO_LEFT = KeyEvent.VK_LEFT;
	private int APO_RIGHT = KeyEvent.VK_RIGHT;
	private int APO_FIRE = KeyEvent.VK_A;
	private int APO_ALTFIRE = KeyEvent.VK_S;
	private int APO_CHANGEWEP = KeyEvent.VK_D;
	private int APO_LEVELUP = KeyEvent.VK_F12;
	private boolean _keysPressed[] = new boolean[ APO_NUM_KEYS ];

	public static final int APO_NUM_KEYS = 7;
	/** Verdi for bevegelse oppover */
	public static final int BOOLEAN_UP = 0;
	/** Verdi for bevegelse ned */
	public static final int BOOLEAN_DOWN = 1;
	/** Verdi for bevegelse venstre */
	public static final int BOOLEAN_LEFT = 2;
	/** Verdi for bevegelse høyre */
	public static final int BOOLEAN_RIGHT = 3;
	/** Verdi for tast som skyter normale skudd */
	public static final int BOOLEAN_FIRE = 4;
	/** Verdi for tast som fyrer av alt-våpen */
	public static final int BOOLEAN_ALTFIRE = 5;
	/** Verdi for tast som skifter våpen */
	public static final int BOOLEAN_CHANGEWEP = 6;


	/**
	* Konstruktor
	*/
	public ShipControl() {
		for( int i=0;i<7;i++ ) {
			_keysPressed[ i ] = false;
		}
	}

    /**
    * Returnerer status til alle taster.
    * true for alle som holdes inne
    */
    public boolean[] getKeys() {
	    return _keysPressed;
    }

  	/**Setter taster
	 *
	 *@param button navn på funksjon - Fire,Alternate,Up,Down,Left,Right,ChangeWeapon
	 *@param newKey koden for den nye tasten. Fra KeyEvent.getKeyCode();
	 */
	public void setKey(String button, int newKey) {

		if ("Fire".equals(button)) {
			APO_FIRE = newKey;
		} else if ("Alternate".equals(button)) {
			APO_ALTFIRE = newKey;
		} else if ("Up".equals(button)) {
			APO_UP = newKey;
		} else if ("Down".equals(button)) {
			APO_DOWN = newKey;
		} else if ("Left".equals(button)) {
			APO_LEFT = newKey;
		} else if ("Right".equals(button)) {
			APO_RIGHT = newKey;
		} else if ("ChangeWeapon".equals(button)) {
			APO_CHANGEWEP = newKey;
		}
	}

	/**
	* Returnerer hvilke knapp som brukes for en gitt knapp.
	* @param button navnet på knappn en ønsker info om
	*/
	public int getKey( String button ) {
		if ("Fire".equals(button)) {
			return APO_FIRE;
		} else if ("Alternate".equals(button)) {
			return APO_ALTFIRE;
		} else if ("Up".equals(button)) {
			return APO_UP;
		} else if ("Down".equals(button)) {
			return APO_DOWN;
		} else if ("Left".equals(button)) {
			return APO_LEFT;
		} else if ("Right".equals(button)) {
			return APO_RIGHT;
		} else { // ("ChangeWeapon".equals(button)) {
			return APO_CHANGEWEP;
		}
	}

	/**
	* Utfører arbeid ut ifra hvilke tast som trykkes
	* @param e tastetrykket
	*/
	public void keyPressed( KeyEvent e ) {
		int tast = e.getKeyCode();

		if (tast==APO_RIGHT) {
			_keysPressed[ BOOLEAN_RIGHT ] = true;
		}else if (tast==APO_LEFT) {
			_keysPressed[ BOOLEAN_LEFT ] = true;
		}else if (tast==APO_UP) {
			_keysPressed[ BOOLEAN_UP ] = true;
		}else if (tast==APO_DOWN) {
			_keysPressed[ BOOLEAN_DOWN ] = true;
		}else if (tast==APO_FIRE) {
			_keysPressed[ BOOLEAN_FIRE ] = true;
		}else if (tast==APO_ALTFIRE) {
			_keysPressed[ BOOLEAN_ALTFIRE ] = true;
		}else if (tast==APO_CHANGEWEP) {
			_keysPressed[ BOOLEAN_CHANGEWEP ] = true;
		}
	}

	/**
	* Utfører arbeid ut ifra hvilke tast som slippes opp
	* @param e tastetrykket
	*/
	public void keyReleased(KeyEvent e) {
		int tast = e.getKeyCode();

		if (tast==APO_RIGHT) {
			_keysPressed[ BOOLEAN_RIGHT ] = false;
		}else if (tast==APO_LEFT) {
			_keysPressed[ BOOLEAN_LEFT ] = false;
		}else if (tast==APO_UP) {
			_keysPressed[ BOOLEAN_UP ] = false;
		}else if (tast==APO_DOWN) {
			_keysPressed[ BOOLEAN_DOWN ] = false;
		}else if (tast==APO_FIRE) {
			_keysPressed[ BOOLEAN_FIRE ] = false;
		}else if (tast==APO_ALTFIRE) {
			_keysPressed[ BOOLEAN_ALTFIRE ] = false;
		}else if (tast==APO_CHANGEWEP) {
			_keysPressed[ BOOLEAN_CHANGEWEP ] = false;
		}
	}

}
