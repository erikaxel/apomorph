/**
 * Level1 Skudd
 *
 * @see GameObject
 * @author Jørgen Braseth (jorgebr@stud.ntnu.no)
 */

public class Shot1 extends PlayerShot {

	/**
	* Konstruktor.
	* @see PlayerShot
	*/
	public Shot1( GameRenderer gr, Player owner, int x, int y, int ySpeed ) {
		super( gr, "shot1", owner, x, y, ySpeed );
		_damage = 1;
		_xSpeed = 30;
	}

	/**
	* Konstruktor.
	* @see PlayerShot
	*/
    public Shot1( GameRenderer gr,Player owner, int x, int y ) {
	    super( gr, "shot1", owner, x, y, 0 );
		_damage = 1;
	    _xSpeed = 30;
    }

	/**
	* Arvet fra PlayerShot.
	* Gjør ingen ting.
	* @see PlayerShot
	*/
	public void action() {
		// NOOP
	}
}
