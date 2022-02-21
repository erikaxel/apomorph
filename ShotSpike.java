/** Skudd fra spikemines
 *
 * @see GameObject
 * @author Jørgen Braseth <jorgebr@stud.ntnu.no>
 */
import java.awt.*;
public class ShotSpike extends EnemyShot {
	private GameObject _target;
	private int _yFactor,_xFactor,_direction;

	/**
	* Konstruktor
	* @param option setter skade fra skuddet
	* @param direction setter retningen på skuddet
	* @see EnemyShot
	*/
	public ShotSpike( GameRenderer gr, int x, int y,int direction, int option) {
		super( gr, "shotspike"+option, x, y, 0 );
		_damage = option;
		_direction = direction;
		switch(direction) {
		case 1:_yFactor=0; _xFactor=1; break;
		case 2:_yFactor=0; _xFactor=-1; break;
		case 3:_yFactor=1; _xFactor=0; break;
		case 4:_yFactor=-1; _xFactor=0; break;
		case 5:_yFactor=-1; _xFactor=1; break;
		case 6:_yFactor=-1; _xFactor=-1; break;
		case 7:_yFactor=1; _xFactor=-1; break;
		case 8:_yFactor=1; _xFactor=1; break;
		}

		_ySpeed = 10*_yFactor;
		_xSpeed = 10*_xFactor-2;
	}

	public void action() {
	//NOOP
	}

	public void draw(Graphics2D g) {
		g.drawImage( _movie[ _direction-1 ], x, y, null  );
	}
}