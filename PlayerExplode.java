/**
 * Explosion sprite
 *
 * @author Jørgen Braseth
 */


public class PlayerExplode extends GameObject  {
	PlayerExplode( GameRenderer gr, int x, int y) {
		super( gr, "explode",x, y );
		_layer = gr.LAYER_EXPLOSION;
		_xSpeed = -1;
		SoundPlayer.getInstance().play("playerexplode.wav");
	}

	public void action() {
		if( _age >= 7 ) {
        	die();
		}
	}

	public void collide( GameObject otherObject ) {
		// NOOP
	}
    public void outOfScreenAction() {
	    die();
    }
}
