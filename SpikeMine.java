/** Mine som skyter 'pigger'.
 * Går i SMÅ sinus-bølger, sakte mot venstre
 * Hastighet kan settes i options
 *
 * @author J&orslash;rgen Braseth (jorgebr@stud.ntnu.no)
 *@see EnemyObject
 */
import java.awt.*;
public class SpikeMine extends EnemyObject  {

	public SpikeMine( GameRenderer gr, Integer x, Integer y, Integer option ) {
		super( gr, "spikemine"+option.intValue(),x, y, option, 2, 100 );
		_health=1;
		_damage=100000;
		_xSpeed = -2;
	}

    /**
     * Beveger seg i små sinuskurver sakte mot venstre.
     * skyter med jevne mellomrom
     */
	public void action() {
		if (_age==35) {
			fire(0);
		} else if(_age==135){
			fire(1);
		} else if((_age==0)||(_age==100)){
			SoundPlayer.getInstance().play("spikemineout.wav");
		}
		_ySpeed = (int) (2 * Math.sin( (double)_age/100 * ( 12*Math.PI ) ));
	}

	/**
	* Tegner objektet
	*/
	public void draw(Graphics2D g) {
		int whichImage = _age%100;
		if ((whichImage>4)&&(whichImage<=35)) {
			whichImage = 4;
		} else if (whichImage>35) {
			whichImage = 0;
		}
		if (_age>=100)
			whichImage+=5;
		g.drawImage( _movie[ whichImage ], x, y, null  );
	}

	/*
	* Skyter fire pigger
	* roterer retning med 45 grader hver gang.
	*/
	public void fire(int direction) {
		SoundPlayer.getInstance().play("spikeshot.wav");
		_gameRenderer.addObject(new ShotSpike(_gameRenderer,x-10+width/2,y+height/2,1+(4*direction),_option));
		_gameRenderer.addObject(new ShotSpike(_gameRenderer,x-10+width/2,y+height/2,2+(4*direction),_option));
		_gameRenderer.addObject(new ShotSpike(_gameRenderer,x-10+width/2,y+height/2,3+(4*direction),_option));
		_gameRenderer.addObject(new ShotSpike(_gameRenderer,x-10+width/2,y+height/2,4+(4*direction),_option));
	}

	/**
	* Skader objektet det treffer
	*/
	public void collide( GameObject otherObject ) {
		otherObject.hit(_damage);
	}

	/**
	* Arvet fra EnemyObject.
	* Gjør ingen ting - Denne fienden er udødelig
	*/
	public void hit(int dmg) {
	//NOOP denne fienden er udødelig
	}
}
