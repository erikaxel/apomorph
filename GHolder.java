/** Glowholder Enemy.
 *
 *	Manetliknende fiende
 *	Gør i sinus-kurve i x-retningen
 *
 * @author Jørgen Braseth (jorgebr@stud.ntnu.no)
 */

class GHolder extends EnemyObject  {
	public GHolder( GameRenderer gr, Integer x, Integer y, Integer option ) {
		super( gr, "glowholder"+option.intValue(), x, y, option, 5*option.intValue(), 600*option.intValue() );
		_damage = 1;
	}
    /**
     * Alt det denne fienden gjør er å bevege seg i sinuskurver i x-retningen over skjermen.
     */
	public void action() {
	    _xSpeed = (int)(Math.sin(((double)_age/15)*Math.PI)*5)-5;
	    if( (_age % 61) == 0)
	    	fire();
	}

    public void fire() {
    	int shotType;
    	switch(_option) {
    		case 1: shotType=2; break;	
    		case 2: shotType=1; break;	
    		case 3: shotType=3; break;
    		case 4: shotType=(_age%2)+1; break;
    		case 5: shotType=(_age%3)+1; break;	
			default: shotType=1; break;
    	}
        _gameRenderer.addObject(new ShotCloud(_gameRenderer,x+width/2,y+5,shotType));
    }

}
