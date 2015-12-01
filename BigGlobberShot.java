/**
 * Skuddet til fienden Globber
 *
 * @see GameObject
 * @see Globber
 * @author J&oslash;rgen Braseth (jorgebr@stud.ntnu.no)
 */

public class BigGlobberShot extends EnemyShot {
	public BigGlobberShot( GameRenderer gr, int x, int y,int option ) {
		super( gr, "bigglobbershot"+option, x, y);
		_damage = option;
		_xSpeed = 0;
		_ySpeed = 0;
		SoundPlayer.getInstance().play("globbershot.wav");
	}

	public void action() {
    	_ySpeed+=0.3;
	}
}
