/** Rask skip som går rett frem
 *
 * @author J&oslash;rgen Braseth (jorgebr@stud.ntnu.no)
 */

public class Spinner extends EnemyObject  {
	private GameObject _target;
	private int _targetLayer;

	public Spinner( GameRenderer gr, Integer x, Integer y, Integer option ) {
		super( gr, "spinner"+option.intValue(),x, y, option, option.intValue(), 400*option.intValue() );
		_xSpeed = -10-(2*_option);
		_health = _option;
	}

    /**
     * Skyter om tiden er inne for det.
     * Høyere level Spinner skyter oftere
     */
	public void action() {
		if(_age%(50/_option)==0)
			fire();
	}

	/**
	* Skitter et skudd siktet etter en tilfeldig spiller.
	*/
	public void fire() {
		//sjekker om noen spillere er i live
		int numOfPlayersLive = _gameRenderer.layers[_targetLayer].size();
		if (numOfPlayersLive>0)
			_target = (GameObject)(_gameRenderer.getRandomObject(_gameRenderer.LAYER_PLAYER));



		if (_target==null) {
			die();
		}else {

			// Hvor er offeret?
			int xdist = (_target.x - x);
			int ydist = (_target.y - y);

			// Beregn enhetsvektor (retning man bør bevege seg i)
			float xa = (float)(xdist / (double)Math.abs(xdist));
			float ya = (float)(ydist / (double)Math.abs(ydist));
			float shotSpeedX = (int)(xa * 5*_option);
			float shotSpeedY = (int)(ya * 5*_option);



			_gameRenderer.addObject(new FullShot(_gameRenderer,x+width/2,y+height/2,shotSpeedY,shotSpeedX,_option));
		}
	}
}
