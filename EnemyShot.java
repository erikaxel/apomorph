/** Overklasse for fienders skudd
 * @author Egil Sørensen (egil@stud.ntnu.no), J&oslash;rgen Braseth (jorgebr@stud.ntnu.no)
 */
abstract public class EnemyShot extends GameObject {
	protected int option;

	public EnemyShot( GameRenderer gr, String name, int x, int y, float ySpeed ) {
        super( gr, name, x, y );
		_damage = 1;
		_ySpeed = ySpeed;
		_layer = GameRenderer.LAYER_ENEMY_SHOT;
	}

	public EnemyShot( GameRenderer gr, String name, int x, int y ) {
        super( gr, name, x, y );
		_damage = 1;
		_ySpeed = 0;
		_layer = GameRenderer.LAYER_ENEMY_SHOT;
	}

	public void collide(GameObject otherObject) {
		die();
	}
}