/**
 * Skuddet til fienden Globber
 *
 * @see GameObject
 * @see Globber
 * @author J&oslash;rgen Braseth (jorgebr@stud.ntnu.no)
 */

public class GlobberShot extends EnemyShot {
	public GlobberShot( GameRenderer gr, int x, int y ) {
		super( gr, "globbershot", x, y);
		_damage = 1;
		_xSpeed = 0;
		_ySpeed = 0;
		SoundPlayer.getInstance().play("globbershot.wav");		
	}

	public void action() {
    	_ySpeed+=0.2;
	}
}
