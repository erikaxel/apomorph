/** Globber Enemy.
 *
 *	Pattern-monster
 *	Beveger seg fremover for så å fare bakover og etterlate et sport med fallende skudd.
 *
 * @author J&oslash;rgen Braseth (jorgebr@stud.ntnu.no)
 */

class Globber extends EnemyObject  {
	private int BehaveState; //type oppførsel
	private int startX;
	public Globber( GameRenderer gr, Integer x, Integer y, Integer option ) {
		super( gr, "globber"+option.intValue(), x, y, option, 5*option.intValue(), 600*option.intValue() );
		BehaveState=0;
		startX=_gameRenderer.getWidth()-width;
		_damage = 1*_option;
	}
    /**
     * Gjør et steg av hva det nå måtte være
     */
	public void action() {

		if (BehaveState == 0) { //inn på bildet
			_xSpeed=-10;
			_ySpeed = (float)(Math.sin(((double)_age/10)*Math.PI)*2);
			if (x<=startX) {
				startX=x-100;
				_ySpeed=15;
				BehaveState=1;
			}
		} else if (BehaveState == 1) { // sinus
			if((y>_gameRenderer.getHeight()-5-height)&&(_ySpeed>=0))
				_ySpeed = -15;

			if((y<5)&&(_ySpeed<=0))
				_ySpeed = 15;

			_xSpeed=0;
			if (_age%90==0)
				BehaveState = 2;
		} else if (BehaveState == 2) { //bullrush
			_ySpeed=0;
			_xSpeed=-20;
			if (x<=0)
				BehaveState=3;
		} else if (BehaveState == 3) { //retreat
			_xSpeed=8;
			_ySpeed=0;
			if (_age%(40-_option*7)==0)
				fire();
			if (x>=startX+100)
				BehaveState=0;
		}

	}

    public void fire() {
        _gameRenderer.addObject(new GlobberShot(_gameRenderer,x,y+25));
    }

}
