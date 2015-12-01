/**
 * En fiende som ser ut som en klassisk flyvende tallerken.
 * G�r i sinusb�lger og slikt.
 * @author Erik Axel Nielsen (erikaxel@stud.ntnu.no)
 */

class Ufo extends EnemyObject  {

	public Ufo( GameRenderer gr, Integer x, Integer y, Integer option ) {
		super( gr, "ufo"+option.intValue(),x, y, option, 2*option.intValue(), 100*option.intValue() );
		_xSpeed = -5;
		_health = _option;
	}

    /**
     * Alt det denne fienden gj�r er � bevege seg i sinuskurver over skjermen.
     */
	public void action() {
		_ySpeed = (int) (5 * Math.sin( (double)_age/200 * ( 4*Math.PI ) ));
	}
}
