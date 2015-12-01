/** Level2 Skudd.
 *
 * @see GameObject
 * @author Jørgen Braseth <jorgebr@stud.ntnu.no>
 */

import java.awt.*;

public class ShotHeatseek1 extends PlayerShot {
	private GameObject _target;

	/**
	* Konstruktor
	* @see PlayerShot
	*/
	public ShotHeatseek1( GameRenderer gr, Player owner, int x, int y) {
		super( gr, "ShotHeatseek1", owner, x, y, 0 );
		_damage = 3;

		if (gr.layers[gr.LAYER_ENEMY].size()>0) {
				_target = (GameObject)(_gameRenderer.layers[gr.LAYER_ENEMY].get((int)(Math.random()*_gameRenderer.layers[gr.LAYER_ENEMY].size())));
		}
		SoundPlayer.getInstance().play("heatseek.wav");
		_xSpeed = 3;
		_target = null;
	}

	/**
	* Beveger seg mot målet.
	* Beveger seg mot målet den har satt seg.
	* Finner et nytt mål om forrige mål er dødt.
	*/
	public void action() {
		int targetX = 2000;
		int targetY = 200;
		boolean isEnemyAwailable = _gameRenderer.layers[ _gameRenderer.LAYER_ENEMY ].size() > 0;

		if( _target == null && isEnemyAwailable ) {
			int whichTarget = (int) (Math.random()*_gameRenderer.layers[ _gameRenderer.LAYER_ENEMY ].size());
			_target = (GameObject)(_gameRenderer.layers[ _gameRenderer.LAYER_ENEMY ].get( whichTarget ));
		}
		// har vi et mål så retter vi oss etter det:
        if( _target != null ) {
			targetX = _target.x+(_target.width/2);
			targetY = _target.y+(_target.height/2);
        }

		if ( targetX > x ) {
			if ( _xSpeed < 0 ) {
				_xSpeed += 2;
			}else {
				_xSpeed += 1;
			}
		} else if ( targetX < x ) {
			if ( _xSpeed < 0 ) {
				_xSpeed -= 1;
			}else {
				_xSpeed -= 2;
			}
		}

		if ( targetY > y ) {
			if ( _ySpeed<0 ) {
				_ySpeed += 2;
			}else {
				_ySpeed += 1;
			}
		} else if ( targetY < y ) {
			if ( _ySpeed<0 ) {
				_ySpeed -= 1;
			}else {
				_ySpeed -= 2;
			}
		}

		if (_age>100)
			die();
	}

	/**
	* Tegner raketten.
	* @param g tegneflaten
	*/
	public void draw(Graphics2D g) {
		int whichImage = 9;
		int adx = Math.abs((int)_xSpeed);
		int ady = Math.abs((int)_ySpeed);

			if((_xSpeed<0)&&(adx>2*ady)) {
			//W
			whichImage = 0+_age%2;
			} else if((_ySpeed<0)&&(ady>2*adx)) {
			//N
			whichImage = 4+_age%2;
			} else if((_xSpeed>0)&&(adx>2*ady)) {
			//E
			whichImage = 8+_age%2;
			} else if((_ySpeed>0)&&(ady>2*adx)) {
			//S
			whichImage = 12+_age%2;
			} else if((_xSpeed<0)&&(_ySpeed<0)) {
			//NW
			whichImage = 2+_age%2;
			} else if((_ySpeed<0)&&(_xSpeed>0)) {
			//NE
			whichImage = 6+_age%2;
			} else if((_ySpeed>0)&&(_xSpeed>0)) {
			//SE
			whichImage = 10+_age%2;} else if((_xSpeed<0)&&(_ySpeed>0)) {
			//SW
			whichImage = 14+_age%2;
			}
		g.drawImage( _movie[ whichImage ], x, y, null  );
	}

}