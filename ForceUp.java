/**
 * Ship Forcefield-ammo
 *
 * @author J�rgen Braseth (jorgebr@stud.ntnu.no)
 * @see GameObject
 */


public class ForceUp extends GameObject  {
	
	/** 
	 *Konstruktor.
	 *
	 *oppretter objectet
	 *@param gr Gamerenderer objektet skal tegnes i
	 *@param x x-posisjonen ved start
	 *@param y y-posisjonen ved start
	 */
	public ForceUp( GameRenderer gr,Integer x,Integer y, Integer option ) {
		super( gr, "forceup",x.intValue(), y.intValue() );
		_layer = GameRenderer.LAYER_POWERUP;
		_xSpeed = -2;
	}
	
	/** Flytter seg */
	public void action() {
		// NOOP
	}
	
	/**
	 *Utf�rer kollisjon.
	 *Om kollisjon med skip:
	 *gir skipet ammo og kj�rer die()
	 *
	 *@param otherObject Objektet dette objektet kolliderer med
	 */
	public void collide( GameObject otherObject ) {
		
		try {
			Player ship = (Player)otherObject;
			ship.powerUpForceField(50);
			die();
		} catch (ClassCastException e) {
			//Log.log("Powerup: Skudd traff powerup");
		}
	}
	public void outOfScreenAction() {
		// NOOP
	}
}
