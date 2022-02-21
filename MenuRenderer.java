import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

/**
 * Denne klassen lager og viser menyene.
 * @author Erik Axel Nielsen (erikaxel@stud.ntnu.no)
 */

public class MenuRenderer extends TextRenderer implements KeyListener {
	private Screen _screen;
	private Menu _mainMenu;
	private Menu _graphicMenu;
    private Menu _currentMenu;
	private Menu _controlMenu;
	private ShipControl _ship1Control;
	private final static int NUM_CTRL_OPTIONS = 7;
	private MenuOptionControls[] _menuList = new MenuOptionControls[ NUM_CTRL_OPTIONS ];
	private int _lastKeyEntered;
	private boolean _getLastKey;

	/**
	 * Konstruktør.
	 * @param screen Skjermen vi jobber mot.
   	 */
	public MenuRenderer( Screen screen, ShipControl ship1Control ) {
		super( screen.getCurrentResolution() );
		_screen = screen;
		_ship1Control = ship1Control;
		_mainMenu = new Menu();
		_graphicMenu = new Menu();
		_controlMenu = new Menu();
		generateControlMenu();
		_lastKeyEntered = 0;
		_getLastKey = false;
	}

	/**
	 * Går inn i MainMenu loop.
	 * @return "StartGame" hvis spillet skal startes, "Exit" hvis spillet skal sluttes.
	 */
	public String mainMenu() {
		_currentMenu = _mainMenu;
	    _mainMenu.clear();
		_mainMenu.add( new MenuOption( "Start the game", 0 ) );
	    _mainMenu.add( new MenuOption( "Graphic Options", 1 ) );
	    _mainMenu.add( new MenuOption( "Control Options", 2 ) );
		_mainMenu.add( new MenuOption( "HighScore", 3) );
		_mainMenu.add( new MenuOption( "Exit", 4 ) );
		done = false;
		_screen.renderLoop( this );

		switch( _currentMenu.getSelectedId() ) {
			case 0: // Start game
				return "StartGame";
			case 1: // Graphic menu
				resolutionMenu();
				return mainMenu();
			case 2:
				controlMenu();
				_getLastKey = false;
				return mainMenu();
			case 3:
                highScore();
				return mainMenu();
			default:
				break;
		}
 	    return "Exit";
	}

	private void highScore() {
   		String[] names;
		int[] scores;
		int num;

		_currentMenu.clear();
		try {
			HighScore hs = new HighScore();
            hs = hs.getHighScoreFromFile( Apo.HIGH_SCORE_FILE );
			names = hs.getNames();
			scores = hs.getScores();
			num = hs.getNumScores();

			for( int i=0; i<num; i++ ) {
				_currentMenu.add( new MenuOption( (i + 1) + ". " + scores[i] + " " + names[i] , 1 ) );
			}

			_currentMenu.setPaintPointer( false );
			_screen.renderLoop( this );
			_currentMenu.setPaintPointer( true );

			} catch( IOException e ) {
				e.printStackTrace();
				Log.log( "Couldn't find highscore file" );
			}
	}

	private void generateControlMenu() {
        _controlMenu.clear();

		_menuList[0] = new MenuOptionControls( "Up", KeyEvent.getKeyText( _ship1Control.getKey( "Up" )   ), 0 ) ;
		_menuList[1] = new MenuOptionControls( "Down",  KeyEvent.getKeyText(_ship1Control.getKey( "Down" )  ), 1 ) ;
		_menuList[2] = new MenuOptionControls( "Left", KeyEvent.getKeyText(_ship1Control.getKey( "Left" )  ), 2 ) ;
		_menuList[3] = new MenuOptionControls( "Right", KeyEvent.getKeyText(_ship1Control.getKey( "Right")  ), 3 ) ;
		_menuList[4] = new MenuOptionControls( "Fire",  KeyEvent.getKeyText(_ship1Control.getKey( "Fire")  ), 4 ) ;
		_menuList[5] = new MenuOptionControls( "Alternate", KeyEvent.getKeyText(_ship1Control.getKey( "Alternate")  ), 5 ) ;
		_menuList[6] = new MenuOptionControls( "ChangeWeapon",  KeyEvent.getKeyText(_ship1Control.getKey( "ChangeWeapon")  ), 6 ) ;

		for( int i=0; i< NUM_CTRL_OPTIONS; i++ ) {
			_controlMenu.add( _menuList[i] );
		}
		_controlMenu.add( new MenuOption("Back", 7 ) );
	}

	private void controlMenu() {
    	_currentMenu = _controlMenu;

		done = false;
		_screen.renderLoop( this );
        int id = _currentMenu.getSelectedId();
        if( id == 7 ) {
	        return;
        }

		MenuOptionControls currMenu = _menuList[ id ];

		// Hvis vi holder på å sette keyen:
		if( currMenu.isKeyTextSet() ) {
			currMenu.setKeyTextSet( false );
			_getLastKey = true;
		} else {
			_ship1Control.setKey( currMenu.getText(), _lastKeyEntered );
			generateControlMenu();
            currMenu.setKeyTextSet( true );
			_getLastKey = false;
		}
		controlMenu();
	}

	// Lager og viser oppløsnings-meny
    private void resolutionMenu() {
	    _currentMenu = _graphicMenu;
		_graphicMenu.clear();

		/*
	    if( _screen.findResolution ( 640, 480 ) != null ) {
		    _graphicMenu.add( new MenuOption( "640x480", 0 ) );
	    }
	    if( _screen.findResolution( 800, 600 ) != null ) {
		    _graphicMenu.add( new MenuOption( "800x600", 1 ) );
	    }
	    if( _screen.findResolution( 1024, 768 ) != null ) {
		    _graphicMenu.add( new MenuOption( "1024x768", 2 ) );
	    }
		*/
	    if( _screen.isFullScreen ) {
		    _graphicMenu.add( new MenuOption( "Set windowed", 3 ) );
		} else if ( _screen.isFullScreenSupported() ) {
		    _graphicMenu.add( new MenuOption( "Set fullscreen", 4 ) );
		}
		_graphicMenu.add( new MenuOption( "Back", 5 ) );

		done = false;
		_screen.renderLoop( this );

		switch( _currentMenu.getSelectedId() ) {
			case 0: // Sette 640x480
				_screen.setResolution( 640, 480 );
				break;
			case 1: // Sette 800x600
				_screen.setResolution( 800, 600 );
				break;
			case 2: // Sette 1024x768
				_screen.setResolution( 1024, 768 );
				break;
            case 3: // Sette windowed modus
                _screen.setWindowedMode();
                break;
            case 4: // Prøve å gå inn i fullscreen
                _screen.setFullScreen();
                break;
			default:  // Back
				return;
		}
		this.setResolution( _screen.getCurrentResolution() );
		resolutionMenu();
	}

	/**
	 * @see TextRenderer
	 * @param g
	 */
    public void renderText( Graphics2D g ) {
	    // Tegne menyen:
	    int x = (int)(_width * 0.25);
	    int y = _logo.getHeight( null ) + 20;

		_currentMenu.draw( g, x, y, ImageMap.getInstance().getFont( g ) );
    }
	public void keyTyped(KeyEvent e) {
	}
    public void keyReleased( KeyEvent e) {
    }
	/* Keylistener... */
	public void keyPressed( KeyEvent e ) {
		_lastKeyEntered = e.getKeyCode();

		if ( _getLastKey ) {
			done = true;
		} else {
			switch( _lastKeyEntered ) {
				case KeyEvent.VK_UP:
					_currentMenu.previous();
					break;
				case KeyEvent.VK_DOWN:
					_currentMenu.next();
					break;
				case KeyEvent.VK_ENTER:
					done = true;
					break;
			}
		}
	}
}