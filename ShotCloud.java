/** Skudd fra glowholders
 * Varmes&oslash;kende 'skyer'
 *
 * @see GameObject
 * @author Jørgen Braseth <jorgebr@stud.ntnu.no>
 */

public class ShotCloud extends EnemyShot {
	private GameObject _target;
	private int _targetLayer;

	/**
	* Konstruktor
	* @see EnemyShot
	*/
	public ShotCloud( GameRenderer gr, int x, int y, int option) {
		super( gr, "shotcloud"+option, x, y, 0 );
		_targetLayer = gr.LAYER_PLAYER;
		_damage = option;

		//sjekker om noen spillere er i live
		int numOfPlayersLive = _gameRenderer.layers[_targetLayer].size();
		if (numOfPlayersLive>0)
			_target = (GameObject)(_gameRenderer.layers[_targetLayer].get((int)(Math.random()*_gameRenderer.layers[_targetLayer].size())));
		SoundPlayer.getInstance().play("shotcloud.wav");
		_xSpeed = 3;
	}

	/**
	* Beveger seg mot målet.
	* Finner et nytt mål om det forrige er dødt.
	*/
	public void action() {
		if (_target==null) {
			die();
		} else {

			int tx = _target.x+(_target.width/2);
			int ty = _target.y+(_target.height/2);

			if (tx>x) {
				if (_xSpeed<0) {
					_xSpeed += 1;
				}else {
					_xSpeed += 0.5;
				}
			} else if (tx<x) {
				if (_xSpeed<0) {
					_xSpeed -= 0.5;
				}else {
					_xSpeed -= 1;
				}
			}

			if (ty>y) {
				if (_ySpeed<0) {
					_ySpeed += 1;
				}else {
					_ySpeed += 0.5;
				}
			} else if (ty<y) {
				if (_ySpeed<0) {
					_ySpeed -= 0.5;
				}else {
					_ySpeed -= 1;
				}
			}
			if (_age>75)
				die();
		}
	}
}