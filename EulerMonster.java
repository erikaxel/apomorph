/** En fiende som går bratt oppover eller nedover og så mot midten
	@author Egil Sørensen (egil@stud.ntnu.no)
  */

class EulerMonster extends EnemyObject  {
    private int mult;
    private boolean collide = false; //Settes til 1 om fienden treffer veggen :)

	/**
	 * Konstruktor
	 * @see EnemyObject
	 */

	public EulerMonster( GameRenderer gr, Integer x, Integer y, Integer option ) {
		super( gr, "manet" + option,x ,y, option, 4, 200 );
	}

    /**
     * Fienden går enten bratt oppover eller nedover først og går så fort mot midten av bildet (er planen =)
     */
	public void action() {
        switch (_option){
            case 1: {
                mult = 1;
                break;
            }
            default: {
                mult = -1;
                break;
            }

        }

	    _ySpeed = (int)Math.exp((_age)/12);

        if( y < 0 ) {
            _xSpeed = -5;
            collide = true;
        }
		if( y > _gameRenderer.getHeight() ) {
            _ySpeed = -_ySpeed;
            collide = true;
        }

        if ( x > 950 && !collide) {
            _xSpeed = -4;
        } else if ( x > 700 && !collide) {
            _xSpeed = -5;
		    _ySpeed = (int) (-Math.exp((_age - 20)/12) * mult);
        } else {
            _xSpeed = -20;
	        _ySpeed = 12 * mult;
        }
	}
}
