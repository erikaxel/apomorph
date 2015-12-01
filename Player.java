/**
 * Spilleren.
 *
 * Spillerens skip
 *
 * @author Jørgen Braseth (jorgebr@stud.ntnu.no)
 * @see GameObject
 */

import java.awt.*;
import java.applet.AudioClip;

public class Player extends GameObject {

	/** Mengen ammo for forcefiled og uberbeam */
	public int forceAmmo;
	/** Alt-weapon som er valgt */
	public int currentWeapon;

	private int _baseSpeed,_level;
	private ForceField field;
	private UberBeam _uberBeam;
	private boolean _canShoot,_canShootMissile,_shooting;
    private static int _maxLevel = 9;
	private ScoreBoard _scoreBoard;
	private int _lives,_maxHealth;
	private int _maxAltWeapons;
    private boolean isDrawn,died;
	private int oldX, oldY;
    private ShipControl _controls;
	private static final String SOUND_SHOT1 = "gun.wav";
	private static final String SOUND_SHOT2 = "shot2.wav";
    private static final String SOUND_PLAYERHIT = "playerhit.wav";
	private static final int WEAPON_HEATSEEK = 1;
	private static final int WEAPON_FIELD = 2;
	private static final int WEAPON_BEAM = 3;
	private AudioClip _gunFireSound;

	/**
	 * Konstruktor.
	 * @param gameRenderer GameRenderer som tegner skipet
	 * @param playerLevel int definerer skipets level
	 */
	public Player(GameRenderer gameRenderer, int playerLevel) {
		super(gameRenderer,"player-lvl"+playerLevel, 0, (gameRenderer.getHeight() / 2) );

		died=false;
		isDrawn=true;
		_maxHealth=4;
		currentWeapon = 0;
		_level = playerLevel-1;
		setSpeed(15);
		_layer = gameRenderer.LAYER_PLAYER;
		_lives = 8;
		_maxAltWeapons=0;

		for(int i=1;i==playerLevel;i++) {
			levelUp();
		}
	}

	/**
	 *  Alt som laster grafikk legges her!
 	 */
	public void loadGraphics() {
		_movie = ImageMap.getInstance().getImage( "player-lvl" + _level );
		field = new ForceField(_gameRenderer,this);
		_uberBeam = new UberBeam(_gameRenderer,this);
		_scoreBoard = new ScoreBoard( _gameRenderer );
		newShip();
		_gunFireSound = SoundPlayer.getInstance().getSound(SOUND_SHOT1);
	}

	/**
	* Setter skipets kontrol
	* @param controls ShipControl objektet som styrer skipet
	*/
	public void setControls( ShipControl controls ) {
		_controls = controls;
	}

	/**
	* Starter nytt skip
	*
	* Flytter skipet midstilt langs ventre kant av skjermen.
	* Setter full helse.
	* Starter Udødelighet.
	*/
	public void newShip() {
		_health = _maxHealth;
		_canShoot = true;
		_canShootMissile = true;
		x=0;
		y=(_gameRenderer.getHeight()/2);
		_xSpeed = 0;
		_ySpeed = 0;
		_health = 4;
		_shooting = false;
        _scoreBoard.setLives( _lives );
		_scoreBoard.setHealth(_health,_maxHealth);
		_age=-50;
	}

	/**
	* Skifter til neste alt-våpen
	* Cycler alternate-våpen
	*/
	public void cycleWeapon() {
		if (currentWeapon>_maxAltWeapons-1) {
			currentWeapon=0;
			if(_maxAltWeapons==0) currentWeapon--;
		}
		currentWeapon++;
		_scoreBoard.setAltWeapon(currentWeapon);
	}

	/**
	* Setter skipets hastighet
	*
	* Setter skipets grunnhastighet
	* @param s ny grunnhastighet
	*/
	public void setSpeed(int s) {
		_baseSpeed = s;
	}

	/**
	 * Setter alle keysene til value
	 * @param value true eller false
	 */
	public void setAllKeys( boolean value ) {
		boolean[] keys = _controls.getKeys();

		for( int i=0;i<ShipControl.APO_NUM_KEYS; i++ ) {
			keys[ i ]  = value;
		}
		setControls();
	}

	/** Fyrer av alternate fire */
	public void altFire(boolean state) {
		if(state) {
			switch(currentWeapon) {
				case WEAPON_HEATSEEK:fireHeatSeek(); break;
				case WEAPON_FIELD:toggleForceField(true); break;
				case WEAPON_BEAM:toggleBeam(true); break;
				default: break;
			}
		}else {
			toggleForceField(false);
			toggleBeam(false);
		}
	}

	/** Øker forcefield "ammo".
	 *@param a mengde ammo
	 */
	public void powerUpForceField(int a) {
		forceAmmo+=a;
		if (forceAmmo<0) {
			forceAmmo=0;
		} else if (forceAmmo>100) {
			forceAmmo=100;
		}
		_scoreBoard.setForce(forceAmmo);
	}

	/** Setter forcefield av eller på
	 *@param state boolean
	 */
	public void toggleForceField(boolean state) {
		if ((state != field.fieldOn)&&(_level>=6)) {
			field.toggle();
		}
	}
	/** Setter uberbeam av eller på
	 *@param state boolean
	 */
	public void toggleBeam(boolean state) {
		if (state != _uberBeam.beamOn)
			_uberBeam.toggle();
	}

	/** Øker skipets level med én.
	*/

	public void levelUp() {
		if (_level < _maxLevel ) {
			_level++;
			switch(_level) {
				case 4:	_movie = ImageMap.getInstance().getImage( "player-lvl2" ); break;
				case 5:	_movie = ImageMap.getInstance().getImage( "player-lvl3" ); break;
				case 6:	_movie = ImageMap.getInstance().getImage( "player-lvl4" ); break;
				case 7:	_movie = ImageMap.getInstance().getImage( "player-lvl5" ); break;
				case 8:	_movie = ImageMap.getInstance().getImage( "player-lvl6" ); break;
				case 9:	_movie = ImageMap.getInstance().getImage( "player-lvl7" ); break;
			}

			//legger til alt-weapon
			switch (_level) {
				case 5: _maxAltWeapons++; cycleWeapon(); break;
				case 7: _maxAltWeapons++; break;
				case 9: _maxAltWeapons++; break;
				default: break;
			}
		}
	}

	/**Setter om skipet skal skyte*/
	/*
	public void setShooting(boolean state) {
		_shooting = state;
	}
    */

	/** Skyter Heatseeking missile */
	public void fireHeatSeek() {
		if (_canShootMissile) {
			_gameRenderer.addObject( new ShotHeatseek1( _gameRenderer, this, x+width/2, y+height/2 ) );
			_canShootMissile = false;
		}

	}

	/** Skyter skudd i henhold til skipets level */
	public void fire() {

		if (_canShoot) {
			switch(_level) {
				case 1:
					_gameRenderer.addObject(new Shot1(_gameRenderer,this,(x+width/2),(y+height/2)));
					break;
				case 2:
					_gameRenderer.addObject(new Shot1(_gameRenderer,this,(x+width/2),(y+height/2-5)));
					_gameRenderer.addObject(new Shot1(_gameRenderer,this,(x+width/2),(y+height/2+5)));
					break;
				case 3:
					_gameRenderer.addObject(new Shot1(_gameRenderer,this,(x+width/2),(y+height/2-5)));
					_gameRenderer.addObject(new Shot1(_gameRenderer,this,(x+width/2),(y+height/2+5)));
					_gameRenderer.addObject(new Shot1(_gameRenderer,this,(x-40+width/2),(y-6+height/2),-7));
					_gameRenderer.addObject(new Shot1(_gameRenderer,this,(x-40+width/2),(y+6+height/2),7));
					break;
				case 4:
				case 5:
					_gameRenderer.addObject(new Shot2(_gameRenderer,this,(x+width/2),(y+height/2-1)));
					_gameRenderer.addObject(new Shot1(_gameRenderer,this,(x+width/2),(y+height/2-9)));
					_gameRenderer.addObject(new Shot1(_gameRenderer,this,(x+width/2),(y+height/2+9)));
					_gameRenderer.addObject(new Shot1(_gameRenderer,this,(x-40+width/2),(y-6+height/2),-7));
					_gameRenderer.addObject(new Shot1(_gameRenderer,this,(x-40+width/2),(y+6+height/2),7));
					break;
				case 6:
				case 7:
					_gameRenderer.addObject(new Shot2(_gameRenderer,this,(x+width/2),(y+height/2-2)));
					_gameRenderer.addObject(new Shot1(_gameRenderer,this,(x+width/2),(y+height/2-9)));
					_gameRenderer.addObject(new Shot1(_gameRenderer,this,(x+width/2),(y+height/2+9)));
					_gameRenderer.addObject(new Shot1(_gameRenderer,this,(x-40+width/2),(y-6+height/2),-7));
					_gameRenderer.addObject(new Shot1(_gameRenderer,this,(x-40+width/2),(y+6+height/2),7));
					_gameRenderer.addObject(new Shot1(_gameRenderer,this,(x-40+width/2),(y-6+height/2),-20));
					_gameRenderer.addObject(new Shot1(_gameRenderer,this,(x-40+width/2),(y+6+height/2),20));
					break;
				case 8:
				default:
					_gameRenderer.addObject(new Shot2(_gameRenderer,this,(x+width/2),(y+height/2-2)));
					_gameRenderer.addObject(new Shot2(_gameRenderer,this,(x+width/2),(y+height/2-13)));
					_gameRenderer.addObject(new Shot2(_gameRenderer,this,(x+width/2),(y+height/2+11)));
					_gameRenderer.addObject(new Shot1(_gameRenderer,this,(x-40+width/2),(y-6+height/2),-7));
					_gameRenderer.addObject(new Shot1(_gameRenderer,this,(x-40+width/2),(y+6+height/2),7));
					_gameRenderer.addObject(new Shot1(_gameRenderer,this,(x-40+width/2),(y-6+height/2),-20));
					_gameRenderer.addObject(new Shot1(_gameRenderer,this,(x-40+width/2),(y+6+height/2),20));
					break;
			}

			_canShoot = false;
		}

		if (currentWeapon==WEAPON_HEATSEEK)
			fireHeatSeek();
	}

	/**Tegne skipet.
	 *
	 *Velger riktig grafikk i forhold til retningen
	 *og tegner skipet
	 */
	public void draw(Graphics2D g) {
		if((_age>=0)||((Math.abs(_age)%6<=2))) {
			int whichImage = 2;
			if (_ySpeed<0) {
				whichImage = 0+Math.abs(_age)%2;
			}else if (_ySpeed==0) {
				whichImage = 2+Math.abs(_age)%2;
			}else if (_ySpeed>0) {
				whichImage = 4+Math.abs(_age)%2;
			}
			g.drawImage( _movie[ whichImage ], x, y, null  );
			_scoreBoard.draw( g );
		}
	}

	/**
	* Skader det skipet treffer
	*/
	public void collide(GameObject objectHit) {
		if( objectHit.getLayer() == GameRenderer.LAYER_ENEMY || objectHit.getLayer() == GameRenderer.LAYER_ENEMY_SHOT)
			hit(objectHit._damage);
	}

	/**
	* Reduserer antall liv med en, og kjører newShip();
	* Avslutter spilet omt om for liv.
	*/
	public void die() {
		_lives--;
		if ( _lives<0 ) {
			_gameRenderer.done = true;
            _gameRenderer.quit = true;
		}
		oldX = x;
		oldY = y;
		died = true;
		PlayerExplode ex = new PlayerExplode( _gameRenderer, x, y);
		newShip();
		_gameRenderer.addObject( ex );
	}

	/**
	 * Gjør et steg.
	 * Flytter skipet på skjermen
	 */
	public void action() {
		_ySpeed = 0;
		_xSpeed = 0;

		setControls();

		if (_age%5==0) {
			_canShoot = true;
		}
		if (_age%20==0) {
			_canShootMissile = true;
		}
		if(_shooting) {
			fire();
		}
	}

	/**Setter kontroller
	*/
	public void setControls() {
		boolean[] keys = _controls.getKeys();

		// Lyd er gøy
		if(!_shooting && keys[ ShipControl.BOOLEAN_FIRE ]) {
			_gunFireSound.loop();
		} else if( _shooting && !keys[ ShipControl.BOOLEAN_FIRE ] ) {
			_gunFireSound.stop();
		}

        if( keys[ ShipControl.BOOLEAN_UP ] ) {
	        _ySpeed -= _baseSpeed;
        }
		if( keys[ ShipControl.BOOLEAN_DOWN ] ) {
			_ySpeed += _baseSpeed;
		}
		if( keys[ ShipControl.BOOLEAN_LEFT ] ) {
			_xSpeed -= _baseSpeed;
		}
		if( keys[ ShipControl.BOOLEAN_RIGHT ] ) {
			_xSpeed += _baseSpeed;
		}

		_shooting =  keys[ ShipControl.BOOLEAN_FIRE ];

		altFire( keys[ ShipControl.BOOLEAN_ALTFIRE ] );

		if( keys[ ShipControl.BOOLEAN_CHANGEWEP ] ) {
            cycleWeapon();
			keys[ ShipControl.BOOLEAN_CHANGEWEP ] = false;
		}
	}

	/** Reduserer skipets liv.
	*@param dmg mengden skade skipet tar
	*/
	public void hit(int dmg) {
		if((_age>=0)&&(!field.fieldOn)) {
			SoundPlayer.getInstance().play( SOUND_PLAYERHIT );
			_health-=dmg;
			_scoreBoard.setHealth(_health,_maxHealth);
			if (_health<=0)
				die();
		}
	}

	/**
	* Returnerer spillerens scoreboard-object
	* @see ScoreBoard
	*/
	public ScoreBoard getScoreboard() {
		return _scoreBoard;
	}

	/**
	* Clearer objectet fra skjermen
	* Kjøres mellom hver gang objektet tegnes.
	*/
	public void clearDraw( Graphics2D g ) {
		if( died ) {
			g.clearRect( oldX, oldY, width, height );
			died = false;
		}
		super.clearDraw( g );
	}

	/**
	* Sørger for at skipet ikke forsvinner ut fra skjermen
	*/
	public void outOfScreenAction() {
		if( y < 0 ) y = 0;
		else if( y + height > _gameRenderer.getHeight() ) y = _gameRenderer.getHeight() - height;
		if( x < 0 ) x = 0;
		else if( x + width > _gameRenderer.getWidth() ) x = _gameRenderer.getWidth() - width;
	}
}
