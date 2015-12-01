import java.awt.*;
import java.awt.Image;
import java.awt.image.*;

/**
 * Scoreboard til toppen av vinduet.
 * Viser liv, score, forcefield osv
 * @author Jørgen Braseth (jorgebr@stud.ntnu.no), Erik Axel Nielsen (erikaxel@stud.ntnu.no)
 */

public class ScoreBoard extends Container {
	private int _lives,_forceAmmo,_health,_maxHealth;
	private int _score,_altWeapon;
	private int _x, _y;
	private Image[] _movie,_lifeIcon,_forceBar,_altWeaponIcon,_healthBar;
	private GameRenderer _gr;
	private boolean _hasChanged;
	private boolean _drawEachFrame;

	/**
	* Konstruktor
	* Cacher grafikk og initialiserer variabler for liv, score osv
	*
	* @param gr GameRendereren som scoreboarden skal tegnes i
	*/
	public ScoreBoard ( GameRenderer gr ) {
		_movie = ImageMap.getInstance().getImage( "scoreboard" );
		_lifeIcon = ImageMap.getInstance().getImage( "lifeicon" );
		_altWeaponIcon= ImageMap.getInstance().getImage( "weaponlogo" );
		_gr = gr;
		_lives = 0;
		setHealth(4,4);
		_score = 0;
		_forceAmmo = 0;
		_altWeapon = 0;
		_x = 0;
		_y = _gr.getHeight() - _movie[0].getHeight( null );
        _hasChanged = true;
		 Image tempForceBar = ImageMap.getInstance().getImage( "forcebar" )[0];
		 Image tempHealthBar = ImageMap.getInstance().getImage( "healthbar" )[0];

		_forceBar = new Image[101];
		for( int i=0; i<101; i++ ) {
			_forceBar[i] = tempForceBar.getScaledInstance( (i*8/10)+1, 27, Image.SCALE_FAST );
			_forceBar[i].getHeight( null ); // tvinger bildet til å bli laget... (ellers flimrer det)
		}
		_healthBar = new Image[101];
		for( int i=0; i<101; i++ ) {
             ImageProducer source = tempHealthBar.getSource();
             ImageFilter filter = new CropImageFilter( 0,0, (i*8/10)+1, 27 );
             ImageProducer producer = new FilteredImageSource( source, filter );
             _healthBar[i] = createImage( producer );
			_healthBar[i].getHeight( null ); // tvinger bildet til å bli laget... (ellers flimrer det)
		}
		// Fiks slik at vi ikke kan fly over scoreboarden
		gr.setPlayingSize( gr.getWidth(), gr.getHeight() - (_movie[0].getHeight( null ) + 10 ) );
	}

	/**
	* Setter variablen for skipets helse.
	* @param health mengde helse igjen
	* @param maxHealth maximal helse for skipet. Brukes for tegning av health-bar
	*/
	public void setHealth(int health,int maxHealth) {
		_health = health;
		if (_health<0) _health=0;
		_maxHealth = maxHealth;
		_hasChanged=true;
	}

	/**
	* Setter variablen for skipets ammo til forcefield og uberbeam.
	* @param f Mengde ammo igjen
	*/
	public void setForce(int f) {
		_forceAmmo = f;
		_hasChanged = true;
	}

	/**
	* Setter antall liv igjen.
	* @param lives antall liv igjen
	*/
	public void setLives( int lives ) {
		_lives = lives;
		_hasChanged = true;
	}

	/**
	* Setter valgt alternate-våpen.
	* @param w nummer som betegner våpenet
	*/
	public void setAltWeapon(int w) {
		_altWeapon = w;
		_hasChanged = true;
	}

	/**
	* Setter ny score
	* @param score Ny Score
	*/
	public void setScore( int score ) {
    	_score = score;
		_hasChanged = true;
	}

	/**
	* Øker score.
	* @param score mengden score økes med
	*/
	public void increaseScore( int score ) {
		_score += score;
		_hasChanged = true;
	}

	/**
	* Returnerer score vist på scoreboard
	*/
	public int getScore(){
		return _score;
	}


	/**
	* Tegner scoreboard og dets elimenter.
	* Tegner liv-symbolene, score, våpen osv.
	* Kjøres hver gang scoreboard endres.
	*/
	public void draw( Graphics2D g ) {
		if( _hasChanged || _drawEachFrame ) {
			g.clearRect( _x, _y, _movie[0].getHeight( null ), _movie[0].getWidth( null ));
			g.drawImage( _movie[ 0 ], _x, _y, null );
			g.drawImage( _healthBar[ (int)100*_health/_maxHealth ], _x + 667, _y + 39, null );
			g.drawImage( _forceBar[ _forceAmmo ], _x + 829, _y + 39, null );
			g.drawImage( _altWeaponIcon[ _altWeapon ], _x + 565, _y + 25, null );
			for(int i = 0;i<_lives;i++) {
				g.drawImage( _lifeIcon[0], _x + 45+((i%4)*50), _y + ((i/4))*15+41, null );
			}
     		Font font = ImageMap.getInstance().getFont( g );
			g.setFont( font  );
			g.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
			g.setColor( new Color(0x000000));

			double charWidth = font.getStringBounds( "0", g.getFontRenderContext() ).getWidth(); //
			String scoreString = Integer.toString( _score );
			g.drawString( scoreString , _x + 500 - (int)( charWidth * scoreString.length()), _y + 70 );
		}
		_hasChanged = false;
	}

	/**
	* Velger om scoreboard skal tegnes ved hver oppdatering.
	* @param value scoreboard tegnes hver oppdatering om denne er true. krever mye ressurser!
	*/
    public void setDrawEachFrame( boolean value ) {
	   _drawEachFrame = value;
    }

	/**
	 * Tegn scoreboarden ved neste oppdatering av bildet.
	 */
	public void drawOnce() {
		_hasChanged = true;
	}
}
