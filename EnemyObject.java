/**
 * @author Erik Axel Nielsen (erikaxel@stud.ntnu.no)
 */

public abstract class EnemyObject extends GameObject {
	protected int _option;
    protected int _score = 0;

	EnemyObject( GameRenderer gr, String movieBaseName, Integer x, Integer y, Integer option, int health, int score ) {
		super( gr, movieBaseName, x.intValue(), y.intValue() );
  		_layer = GameRenderer.LAYER_ENEMY;
		_health = health;
		_score = score;
		_option = option.intValue();
	}

	public void collide( GameObject otherObject ) {
        int layer = otherObject.getLayer();
		if( layer == GameRenderer.LAYER_PLAYER || layer == GameRenderer.LAYER_FRENDLY_SHOT ) {
            _health -= otherObject._damage;
			if ( _health <= 0 ) {
				die();
				Player player;
				if( layer == GameRenderer.LAYER_PLAYER ) {
					player = (Player)otherObject;
				} else {
					player = ((PlayerShot)otherObject).getOwner();
				}
				player.getScoreboard().increaseScore( _score );
			}
        }
	}

	public void die() {
		super.die();
		Explode ex = new Explode( _gameRenderer, x+width/2, y+height/2 );
		_gameRenderer.addObject( ex );
	}

	/**
	 *  Skal kunne forsvinne ut til høyre og venstre, men ikke opp eller ned.
	 */
	public void outOfScreenAction() {
		if( y < 0 ) {
			y = 0;
		} else if ( y + height + _ySpeed > _gameRenderer.getHeight() ) {
			y = _gameRenderer.getHeight() - height;
		}
	}
}
