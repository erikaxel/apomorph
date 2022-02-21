/** 
 * Überbeam weapon
 *
 * @author Jørgen Braseth <jorgebr@stud.ntnu.no>
 * @see GameObject
 */

public class UberBeam extends PlayerShot {

	/** Bestemmer om strålen er på.*/
	public boolean beamOn;
	private java.applet.AudioClip sound;	
	/** Konstruktor 
	 *
	 *@param gr GameRenderer som objektet tegnes til
	 *@param owner Spillern som styrer våpenet
	 */
	public UberBeam( GameRenderer gr,Player owner) {
		super( gr, "uberbeam", owner, 0, 0, 0 );
		sound = SoundPlayer.getInstance().getSound("uberbeam.wav");
		_damage=4;
		_health=1;
		beamOn = false;
	}
	
	/**
	 *Utfører steget.
	 *Flytter med skipet om på, fjernes fra render-loopen om av
	 */
	public void action() {
		
		if ((_owner.forceAmmo<=0)||(beamOn==false)) {
			die();
		} else if (beamOn) {
			x = _owner.x+23;
			y = _owner.y+45;
			_owner.powerUpForceField(-2);
		}
		
	}
	
	/**
	 *Skrur våpenet av eller på
	 */
	public void toggle() {
		Log.log("beam toggled");
		if (beamOn) {
			sound.stop();
			beamOn = false;
			die();
		} else if (_owner.forceAmmo>0) {
			sound.loop(); 
			beamOn = true;
			_gameRenderer.addObject( this );
		}
	}

	public void collide( GameObject otherObject ) {
		// NOOP
	}
	public void outOfScreenAction() {
		// NOOP
	}
	public void hit(int dmg) {
		// NOOP	
	}
}
