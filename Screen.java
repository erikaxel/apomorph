import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.TreeSet;

/**
 * Denne klassen implementerer rotvinduet i applikasjonen.
 * Setter opp forskjellige oppløsninger, fullskjerm og får tak i Graphic objekter.
 * @author Erik Axel Nielsen (erikaxel@stud.ntnu.no)
 *
 * Bugs:
 * Hvis du setter oppløsning, og så skifter til fullscreen så får vi en feil av en eller annen grunn.
 * Har brukt mye tid til å fikse det, men finner ikke hva som er feil.
 * Hvis noen finner det er det finfint.
 */

public class Screen extends JFrame {
	private GraphicsEnvironment _gEnv;
	private GraphicsDevice _gDevice;
	private DisplayMode _startResolution, _currentResolution;
	private DisplayMode[] _allResolutions;
	private HighResTimer _timer;
	private final static double MIN_ROUND_TIME = 40; // minimum tid på 20ms.
	/** Står vi i fullskjermsmodus? */
    public boolean isFullScreen;

	/**
	 * Konstruktør.
	 */
	public Screen() {
		isFullScreen = false;
		_gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
		_gDevice = _gEnv.getDefaultScreenDevice();
		_startResolution = _gDevice.getDisplayMode();
		_currentResolution = new DisplayMode( 0, 0, 16, 70 );
        _timer = new HighResTimer();

		setIgnoreRepaint( true ); // vil ikke bli forstyrret av os'et
		// Fjern ekstra refresh-rater og kun 16-bit:
		_allResolutions = removeDoubles( _gDevice.getDisplayModes() );
		setBackground( Color.BLACK );
        setIconImage( ImageMap.getInstance().getImage( "icon" )[0] );
		// Fjern musepeker:
		setCursor( Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB ),new Point(0,0),"Hei"));
	}

	/**
	 * Setter oss inn i vindumodus, dvs ikke fullskjerm.
	 */

	public boolean setWindowedMode() {
		if( isFullScreen == true ) {
		   _gDevice.setFullScreenWindow( null );
			isFullScreen = false;
  	    }
        if( !isDisplayable() ) {
            setUndecorated( true );
        }
        setVisible( true );

		int x,y;
		if ( _startResolution == null ) { // Todo: Er dette en god nok sjekk for om vi ikke har en gyldig startmode?
			x = 0;
			y = 0;
		} else {
			x = ( _startResolution.getWidth() - _currentResolution.getWidth() ) / 2;
			y = ( _startResolution.getHeight() - _currentResolution.getHeight() ) / 2;
		}

		setBounds( x, y, _currentResolution.getWidth(), _currentResolution.getHeight() );
		createStrategy();
		return true;
	}


	public boolean setFullScreen() {
		if( _gDevice.isFullScreenSupported() ) {
			if( !isDisplayable() ) {
				setUndecorated( true );
			}
			_gDevice.setFullScreenWindow( this );
			if( _gDevice.isDisplayChangeSupported() ) {
				_gDevice.setDisplayMode( _currentResolution );
			}
            isFullScreen = true;
			createStrategy();
		} else {
			Log.log( "No support for fullscreen" );
			return false;
		}
		return true;
	}

	public boolean setResolution( int width, int height ) {
		DisplayMode m = findResolution( width, height );
		if( m == null ) {
			return false;
		}
		_currentResolution = m;

		if( isFullScreen ) {
			return setFullScreen();
		} else {
			return setWindowedMode();
		}
	}

	/**
	 * Returner om vi kan gå inn i fullskjerm.
	 * @return true hvis vi kan bruke fullskjerm, false ellers
	 */
	public boolean isFullScreenSupported() {
		return _gDevice.isFullScreenSupported();
	}

	/**
	 * Returnerer den nåværende resolusjonen
	 * @return resolusjonen vi er i nå
	 */
	public DisplayMode getCurrentResolution() {
		return _currentResolution;
	}

	/**
	 * Sette skjermen tilbake til der vi var før vi startet spillet.
	 */
	public void restoreScreen() {
		if( isFullScreen ) {
			_gDevice.setFullScreenWindow( null );
		}
	}

	/**
	 * Prøver å sette en resolusjon uten å sjekke om den finnes.
	 * @param width Bredde på resolusjon
	 * @param height Høyde på resolusjon
	 */
	public void forceWindowedResolution( int width, int height ) {
		_currentResolution = new DisplayMode( width, height, 16, 70);
		setWindowedMode();
	}

	/**
	 * Kjøreløkken. Henter et Graphic objekt og gir det til Rendereren. Skriver så til skjerm.
	 * @param re Renderen som skal skrive på Graphic objektet vårt.
	 */
	public void renderLoop( Renderer re ) {
		this.addKeyListener( re );
		renderLoopAux( re );
		this.removeKeyListener( re );
	}

	// Den faktiske løkken. renderLoop brukes til å sette opp og ta ned keyListener
	private void renderLoopAux( Renderer re ) {
		int time;
		re.done = false;
		while( !re.done ) {
			try {
				time = (int) ((_timer.timeElapsed() * 1000) - MIN_ROUND_TIME) ;
                if( time < 0 ) {
					Thread.sleep( -time );
                }
		    } catch( Exception e ) {
				// NOOP
		    }
			Graphics2D g = (Graphics2D)getBufferStrategy().getDrawGraphics();
			re.render( g );
			getBufferStrategy().show(); // will pageflip
			g.dispose();
        }
	}
	/**
	 * Kjører render() en gang bare.
	 */
	public void renderOnce( Renderer re ) {
		Graphics2D g = (Graphics2D)getBufferStrategy().getDrawGraphics();
		re.render( g );
		getBufferStrategy().show(); // will pageflip
		g.dispose();
	}

	public void clearScreen() {
		// Vi clearer 3 ganger for å være sikker på at alle buffrene har blitt clearet
		for( int i=0; i<3; i++ ) {
			Graphics2D g = (Graphics2D)getBufferStrategy().getDrawGraphics();
			g.clearRect( 0, 0, _currentResolution.getWidth(), _currentResolution.getHeight() );
			getBufferStrategy().show();
			g.dispose();
		}
	}
   /**
    * Prøver å finne oppløsningen den gitte oppløsningen.
    * @param width Bredden som skal finnes
    * @param height Høyden som skal finnes
    * @return Referanse til oppløsningen hvis den finnes, eller null hvis den ikke finnes.
    */
	public DisplayMode findResolution( int width, int height ) {
		for( int i=0; i<_allResolutions.length; i++ ) {
			if( _allResolutions[i].getWidth() == width &&
				_allResolutions[i].getHeight() == height ) {
				return _allResolutions[i];
			}
		}
		return null;
	}
	/*
	 * Vil fjerne alle displaymodes som:
	 * 1. Kun har forskjell i refresh-rate.
	 * 2. Ikke har 16 bits
	 */
	private DisplayMode[] removeDoubles( DisplayMode[] mode ) {
		TreeSet intSet = new TreeSet();
		DisplayMode[] tempMode = new DisplayMode[ mode.length ];
		int i;
		int total = 0;
		for( i=0; i< mode.length; i++ ) {
			Integer ii = new Integer( mode[i].getWidth() );
			if( (mode[i].getBitDepth() == 16 ) &&
				!intSet.contains( ii ) ) {

				intSet.add( ii );
				tempMode[ total++ ] = mode[i];
			}
		}
		DisplayMode[] returnMode = new DisplayMode[ total ];
		for( i=0; i<returnMode.length; i++ ) {
			returnMode[i] = tempMode[i];
		}
		return returnMode;
	}
    // Aktiver dobbeltbuffer (fjerner flimmer og slikt på skjermen)
	private void createStrategy() {
		createBufferStrategy( 2 );
	}
}