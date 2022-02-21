/**
 * ForceField
 *
 * @see GameObject
 * @author Jørgen Braseth <jorgebr@stud.ntnu.no>
 */

import java.awt.*;
import java.awt.image.*;

public class ForceField extends PlayerShot {
	/** Viser om forcefield er på */
	public boolean fieldOn;

	/**
	* Konstruktor
	* @param gr Gamerenderer den tegnes i
	* @param player eieren av forecfielden
	*/
	public ForceField( GameRenderer gr,Player player) {
		super( gr, "forcefield", player, 0, 0, 0 );
		_damage=15;
		fieldOn = false;
	}

	public void hit(int dmg) {
		//NOOP
	}

	/**
	 *Utfører steget.
	 *Flytter med skipet om på, fjernes fra render-loopen om av
	 */
	public void action() {
		if ((_owner.forceAmmo<=0)||(fieldOn==false)) {
			die();
		} else if (fieldOn) {
			x = _owner.x-50;
			y = _owner.y-8;
			addAmount(-1);
		}
	}

	/**
	 *Skrur forcefield av eller på
	 */
	public void toggle() {
		if (fieldOn) {
			fieldOn = false;
			die();
		} else if (_owner.forceAmmo>0){
			fieldOn = true;
			_gameRenderer.addObject( this );
		}
	}

	/**
	* Legger til ammo.
	* @param a Mengden som legges til
	*/
	public void addAmount(int a) {
		_owner.forceAmmo += a;
		if (_owner.forceAmmo>100)
			_owner.forceAmmo=100;
		Log.log("Increased forcefield amount by "+a);
		_owner.getScoreboard().setForce(_owner.forceAmmo);
	}
}