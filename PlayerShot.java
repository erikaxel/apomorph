
/**
 * @author Erik Axel Nielsen (erikaxel@stud.ntnu.no)
 */
abstract public class PlayerShot extends GameObject {
	protected Player _owner;

	public PlayerShot( GameRenderer gr, String name, Player owner, int x, int y, int ySpeed ) {
        super( gr, name,x, y );
		_ySpeed = ySpeed;
		_layer = GameRenderer.LAYER_FRENDLY_SHOT;
		_owner = owner;
	}

	public void collide(GameObject otherObject) {
		if( otherObject.getLayer() == GameRenderer.LAYER_ENEMY ) {
			hit(otherObject._damage);
		}
	}

	public Player getOwner() {
		return _owner;
	}
}