/** Level2 Skudd
 *
 * @see GameObject
 * @author Jørgen Braseth (jorgebr@stud.ntnu.no)
 */

public class Shot2 extends PlayerShot {

	/**
	* Konstruktor.
	* @see PlayerShot
	*/
	public Shot2( GameRenderer gr, Player owner, int x, int y, int ySpeed ) {
		super( gr, "shot2", owner, x, y, ySpeed );
		_damage = 2;
		_xSpeed = 30;
	}
	/**
	* Konstruktor.
	* @see PlayerShot
	*/
    public Shot2( GameRenderer gr,Player owner, int x, int y ) {
	    super( gr, "shot2", owner,x, y, 0 );
	    _damage = 2;
	    _xSpeed = 30;
    }

	/**
	* Gjør ingen ting
	* @see PlayerShot
	*/
	public void action() {
		// NOOP
	}
}