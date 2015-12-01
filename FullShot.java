/**Siktbart skudd
 * Skudd som kan settes med både x og y fart og skade
 *
 * @see GameObject
 * @author J&oslash;rgen Braseth (jorgebr@stud.ntnu.no)
 */

public class FullShot extends EnemyShot {
	public FullShot( GameRenderer gr, int x, int y, float ySpeed,float xSpeed,int dmg) {
		super( gr, "shot4", x, y);
		_damage = dmg;
		_xSpeed = xSpeed;
		_ySpeed = ySpeed;
	}

	public void action() {
    	// NOOP
	}
}
