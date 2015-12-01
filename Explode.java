/**
 * Explosion sprite
 *
 * @author Jørgen Braseth
 */


public class Explode extends GameObject  {

	Explode( GameRenderer gr, int x, int y ) {
		super( gr, "explode",x-30, y-30 );

		_layer = gr.LAYER_EXPLOSION;
		_xSpeed = -1;
		SoundPlayer.getInstance().play("explosion.wav");
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
