/** Ringmonster Enemy.
 *
 *	Ringmonster
 *
 * @author Egil Sørensen
 */

class Ring extends EnemyObject  {

	private Integer _option;

	/**
	 * Konstruktor
	 * @see EnemyObject
	 *
	 */

	public Ring( GameRenderer gr, Integer x, Integer y, Integer option ) {
		super( gr, "ring"+(((option.intValue()-1)%4)+1), x, y, option, (((option.intValue()-1)%4)+1), 100*(((option.intValue()-1)%4)+1));
		_option = option;
		_damage = 1;
	}
    /**
     * Går i rundinger
     */
	public void action() {

	    if (_option.intValue() < 5)
	    {
	    _xSpeed = (int) (15 * Math.cos( (double)_age/200 * ( 4*Math.PI ) )) - 4;
	    _ySpeed = (int) (15 * Math.sin( (double)_age/200 * ( 4*Math.PI ) ));
		}

		if (_option.intValue() >= 5)
		{
		_xSpeed = (int) (15 * Math.cos( (double)_age/200 * ( 4*Math.PI ) )) - 4;
		_ySpeed = (int) (-15 * Math.sin( (double)_age/200 * ( 4*Math.PI ) ));
		}


	}


}
