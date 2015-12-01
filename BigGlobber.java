/** BigGlobber Enemy.
 *
 *	Pattern-monster
 *	Beveger seg fremover for så å fare bakover og etterlate et sport med fallende skudd.
 *  Denne ersjonen går aldri ut av bildet. Levelboss-mulighet
 *
 * @author J&oslash;rgen Braseth (jorgebr@stud.ntnu.no)
 */

public class BigGlobber extends EnemyObject  {
	private int BehaveState; //type oppførsel
	private int startX;
	public BigGlobber( GameRenderer gr, Integer x, Integer y, Integer option) {
		super( gr, "bigglobber"+option.intValue(), x, y, option, 100*(option.intValue()*option.intValue()), 1000*option.intValue() );
		BehaveState=0;
		startX=_gameRenderer.getWidth()-width-20;
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
				_ySpeed=15;
				BehaveState=1;
			}
		} else if (BehaveState == 1) { // sinus
			if((y>_gameRenderer.getHeight()-5-height)&&(_ySpeed>=0))
				_ySpeed = -15;

			if((y<5)&&(_ySpeed<=0))
				_ySpeed = 15;

			_xSpeed=0;

			if (_age%(60/_option)==0)
				fire(2);

			if (_age%90==0)
				BehaveState = 2;
		} else if (BehaveState == 2) { //bullrush
			_ySpeed=0;
			_xSpeed=-30;
			if (x<=10)
				BehaveState=3;
		} else if (BehaveState == 3) { //retreat
			_xSpeed=8;
			_ySpeed=0;
			if (_age%(30-_option*8)==0)
				fire(1);
			if (x>=startX)
				BehaveState=0;
		}

	}

    public void fire(int type) {
        switch(type) {
        	case 1:_gameRenderer.addObject(new BigGlobberShot(_gameRenderer,x,y+25,_option)); break;
        	case 2:_gameRenderer.addObject(new ShotCloud(_gameRenderer,x+width/2,y+height/2,_option)); break;
		}
    }

}
