import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.*;

/**
 * Denne klassen er selve motoren i spillet.
 * Har oversikt over alle objektene i forskjellige lag, og ber dem om å flytte og tegne seg selv.
 * @author Erik Axel Nielsen (erikaxel@stud.ntnu.no), Egil Sørensen (egil@stud.ntnu.no)
 */

public class GameRenderer extends Renderer implements KeyListener {
	/**
	 * Denne vektoren inneholder lagene.
	 * Lag 0, er bakgrunn, kan ikke kollidere med de andre lagene.
	 * Lag 1, er vennlig, dvs Skip + skips-skudd, kan kollidere med lag 2.
	 * Lag 2, er fiende, dvs Fiende, fiende-skudd, kan kollidere med lag 1.
	 * Lag 3, er powerups, kan kollidere med lag 1
	 * Objekt som ligger i høyere lag vil gjemme de under.
	 */
	public LinkedList[] layers;

	/* For å unngå "magiske" variabler, defineres lagbetydningene som statiske variabler */
	/** Bakgrunnslaget */
	final static public int LAYER_BACKGROUND = 0;
    /** Eksplosjonslaget */
    final static public int LAYER_EXPLOSION = 1;
	/** Vennlig lag */
	final static public int LAYER_PLAYER = 5;
	/** Motstanderlag */
	final static public int LAYER_ENEMY = 3;
    /** Powerups */
    final static public int LAYER_POWERUP = 4;
    /** Vennlige skudd */
    final static public int LAYER_FRENDLY_SHOT = 2;
	/** Fiendtlige skudd */
    final static public int LAYER_ENEMY_SHOT = 6;
    private ArrayList removeObjects;
	private Level _level;
    private HighResTimer highResTimer;
	private HighResTimer _fpsTimer;
	private double totalRunTime = 0; // used for debugging
	private int totalRunTicks = 0;
	private boolean _isFullScreen;
	protected boolean _levelDone;
	public boolean pause;
	public boolean noTicks;
	private boolean _showFPS;
	private Font _fpsFont;
	private double[] _lastTenFps;
	private boolean _clearFps;

    private BackgroundFactory bgf = new BackgroundFactory(this);
	//private int tick;
    //private Rectangle screenSize;
	/**
	 * Konstruktor
	 * @param dispMode Displaymodusen vi er i.
	 */
	public GameRenderer( DisplayMode dispMode, boolean isFullScreen ) {
		super( dispMode );
		layers = new LinkedList[7];
        removeObjects = new ArrayList();
        highResTimer = new HighResTimer();
        _isFullScreen = isFullScreen;
        _showFPS = false;
        _fpsTimer = new HighResTimer();
		_fpsFont = ImageMap.getInstance().getNormalFont();
        _lastTenFps = new double[10];

		for( int i=0; i<layers.length; i++ ) {
			layers[i] = new LinkedList();
		}

		for( int i=0; i<10; i++ ) {
        	_lastTenFps[i] = 40;
		}
	}

	public void setLevel( Level level ) {
		_level = level;
		done = false;
		_levelDone = false;
	}

	/**
	 * Overridet fra baseklassen.
	 * Dette er spillets main-loop, kjører action() og draw() på alle objektene.
	 */
    public void render(Graphics2D g) {
		//Log.log( "Time since last check: " + highResTimer.timeElapsed() );
        totalRunTime += highResTimer.timeElapsed();
		totalRunTicks++;



		int last = 0;
		ArrayList newObjects;

		g.setColor( Color.black );

		// Vi må cleare bildet før vi gjør noe som helst annet:
		if( _isFullScreen ) {
			g.clearRect( 0,0, _width, _height + 50);

		} else {
			for( int i=0;i<layers.length; i++ ) {
				ListIterator iter = layers[i].listIterator();

				while( iter.hasNext() ) {
					GameObject o = (GameObject)iter.next();
					o.clearDraw( g );
				}
			}
		}
		// Bli kvitt gamle objekter:
		int s = removeObjects.size();
		for( int i=0; i<s; i++ ) {
			GameObject go = (GameObject)removeObjects.get(i);
			layers[ go.getLayer() ].remove( go );
		}
		removeObjects.clear();

		if( !noTicks ) {
			newObjects = _level.getNextObjects();
			last = newObjects.size();
			for (int i=0; i<last; i++) {
				addObject((GameObject)newObjects.get(i));
			}
			newObjects.clear();
		}

		for( int i=0; i<layers.length; i++ ) {

			ListIterator iter = layers[i].listIterator();
			while( iter.hasNext() ) {
				GameObject o = (GameObject)iter.next();
				o.doAction();  // flytt, skyt osv
				o.draw( g );   // tegn objektet
				// Sjekk om vi er langt utenfor skjermen, isåfall, fjern objektet:
				if (o.x < -200 || o.x > (_width + 200)  || o.y < -200) {
				     removeObject(o);
				}
			}
		}

        bgf.addBackgroundObject();
		testCollide();

		// Skrive fps'en øverst til venstre i skjermen:
		if( _showFPS ) {
			printFPS( 1 / _fpsTimer.timeElapsed(), g, false );
		}
		if( _clearFps ) {
			printFPS( 1 / _fpsTimer.timeElapsed(), g, true );
			_clearFps = false;
		}
 		// Sjekk om levelet er ferdig.
		// Levelet er ferdig når det er tomt for fiender både i levelet og på skjermen.
		if ( _level.empty() && (layers[LAYER_ENEMY].size() == 0 && layers[LAYER_POWERUP].size() == 0)) {
			levelDone();
		}
	}

	public void levelDone() {
		layers[LAYER_ENEMY].clear();
		layers[LAYER_ENEMY_SHOT].clear();
		layers[LAYER_FRENDLY_SHOT].clear();
		layers[LAYER_EXPLOSION].clear();
		_level.clear();
		done = true;
		_levelDone = true;
	}

    /** Legg til et objekt
     * 	@param gameObject Objektet som skal legges til
	 */
	public void addObject( GameObject gameObject ) {
		layers[ gameObject.getLayer() ].add( gameObject );
	}

	/**
	 * Fjerner et objekt.
	 * @param gameObject Objektet som skal fjernes.
	 */
	public void removeObject( GameObject gameObject ) {
        removeObjects.add( gameObject );
	}

	/**
	 * Tester om objekter fra layer 2 (fiender), "krasjer" med skudd eller skipet
	 * Kjører i såfall collide() metoden på begge to
	 */
	private void testCollide() {
		checkOneCollide( LAYER_PLAYER );
		checkOneCollide( LAYER_FRENDLY_SHOT );
	}

	private void checkOneCollide( int layer ) {
		GameObject oneObject;
		int layerOneSize = layers[ layer ].size();

		for (int i = 0; i < layerOneSize; i++) {
			oneObject = (GameObject) layers[ layer ].get(i);

			checkTwoCollide( LAYER_ENEMY, oneObject );

			//sjekkes kun mot spillere
			if(layer==LAYER_PLAYER) {
				checkTwoCollide( LAYER_ENEMY_SHOT, oneObject);
				checkTwoCollide( LAYER_POWERUP, oneObject );
			}
		}

	}

	private void checkTwoCollide( int layer, GameObject oneObject ) {
		Rectangle shape;
		GameObject otherObject;
		shape = new Rectangle( oneObject.x, oneObject.y, oneObject.width, oneObject.height);

		ListIterator iter = layers[ layer ].listIterator();

		while( iter.hasNext() ) {
			otherObject = (GameObject)iter.next();
			if (shape.intersects(otherObject.x, otherObject.y, otherObject.width, otherObject.height)) {
				oneObject.collide(otherObject);
				otherObject.collide(oneObject);
			}
		}
	}

	public void setPlayingSize( int width, int height ) {
		_width = width;
		_height = height;
	}


	public GameObject getRandomObject( int layer ) {
		int layerSize = layers[ layer ].size();
		if( layerSize == 0 ) {
			return null;
		} else {
			return (GameObject)layers[ layer ].get( (int)(Math.random()*layerSize) );
		}
	}

	public int getNumObjects( int layer ) {
		return layers[ layer ].size();
	}

	public LinkedList getAllObjects( int layer ) {
		return layers[ layer ];
	}

	public boolean getLevelDone() {
		return _levelDone;
	}

	public void drawScoreboards() {
	    int size = getNumObjects( LAYER_PLAYER );
		for( int i=0; i<size; i++ ) {
			Log.log("Fetching player " + i);
			Player pl = (Player)layers[ LAYER_PLAYER ].get( i );
			Log.log("Drawing scoreboard for player " + i);
			pl.getScoreboard().drawOnce();
		}
	}


	private void printFPS( double fps, Graphics2D g, boolean clear ) {
		g.setColor( Color.BLACK );
		g.clearRect( 0,0, 80, 40 );
		if( clear ) return;

     	_lastTenFps[ totalRunTicks % 10 ] = fps;
		g.setFont( _fpsFont );

		int totFps = 0;
		for( int i=0; i<10;i++ ) {
			totFps += _lastTenFps[i];
		}

		String text = "FPS: " + Math.floor( totFps/10 );
		g.setColor( Color.WHITE );
		g.drawString( text , 10, 20 );
		g.drawString( "Ticks: " + totalRunTicks,  10, 40 );
	}

    public void printTotalTimes() {
		double avgTime = totalRunTime/totalRunTicks;
		Log.log( "Average time on 1 loop: " + avgTime + " (fps :" + 1/avgTime + ")" );

    }

	private void allShipsUp() {
		for( int i=0; i<layers[ LAYER_PLAYER ].size(); i++ ) {
			Player player = (Player)layers[ LAYER_PLAYER ].get( i );
			player.levelUp();
			player.powerUpForceField( 100 );
		}
	}

	/**
	 * Sjekke hvilke objekter vi har igjen på skjermen. Kun for debug.
	 */
	public void printObjects() {
		HashMap objects = new HashMap();

        Log.log( "Printing all objects");

		for( int i=0; i<layers.length; i++ ) {
			int s = layers[i].size();
			Log.log( "Layer " + i + " ("  + s + " has object(s))" );
			for( int u=0; u<s; u++ ) {
				GameObject o = (GameObject)layers[i].get( u );
				if( objects.containsKey( o.getName() ) ) {
					Integer numObjects  = (Integer)objects.get( o.getName() );
					objects.put( o.getName(), new Integer( numObjects.intValue() + 1 ));
				} else {
					objects.put( o.getName(), new Integer( 1 ) );
				}
			}
		}

        Set s = objects.keySet();
        String n;
		for( Iterator i  = s.iterator(); i.hasNext(); ) {
			n=(String)i.next();
            Log.log( n + " occurs " + objects.get( n ).toString() );
        }
	}

	public void keyPressed(KeyEvent e) {
		switch( e.getKeyCode() ) {
			case KeyEvent.VK_ESCAPE:
			    done = true;
				quit = true;
				break;
			case KeyEvent.VK_PAUSE:
				done = true;
				pause = true; // pause;
				break;
			case KeyEvent.VK_F1:
				_showFPS = !_showFPS;
				if( !_showFPS ) _clearFps = true;
				break;
			case KeyEvent.VK_F2:
				_level.printElements();
				break;
			case KeyEvent.VK_F3:
				printObjects();
				break;
			case KeyEvent.VK_F12:
				allShipsUp();
				break;
		}
	}
}
