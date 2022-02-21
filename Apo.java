import java.io.IOException;

/**
  *  Apo er starter-klassen. Den har main-metoden, åpner en fullscreen og starter spillet
  *  Du kan gå ut av spillet ved å trykke på Esc knappen
  *  @author Erik Axel Nielsen (erikaxel@stud.ntnu.no)
 */

public class Apo {
	private Screen screen;
	private String os;
	private String initialLevel;
	private int initialPlayerLevel;
    private ShipControl player1Control;
	public static final String HIGH_SCORE_FILE = "highscore.apo";

	/* Her starter programmet! */
	public static void main( final String[] args ) {
		String initialLevel = "level1.lev";
		int initialPlayerLevel = 1;

		if( args.length >= 1 ) {
			initialLevel = args[0];
		}
		if( args.length >= 2 ) {
			initialPlayerLevel = Integer.parseInt(args[1]);
		}

		Apo apo = new Apo(initialLevel, initialPlayerLevel);
	}

	/**
	 * Konstruktoren, starter spillet
	 * @param initialLevel levelet som man skal starte på.
	 * @param initialPlayerLevel Hvor bra skipet skal være når man starter.
	 */
	public Apo( String initialLevel, int initialPlayerLevel) {
		this.initialLevel = initialLevel;
		this.initialPlayerLevel = initialPlayerLevel;

		Log.setLogLevel( 4 );
		Log.setErrorLevel( 5 );

		//disable the properties subsystem
		System.setProperty("java.util.prefs.PreferencesFactory", "DisabledPreferencesFactory");
		checkSystemProperties();

        ImageMap.getInstance().getInstance().setImageDir( "img/" );
		SoundPlayer.getInstance().setSoundDir( "sound/" );
		LevelLoader.setLevelDir( "levels/" );

		ImageMap.getInstance().setFont( "Jambotan.ttf" );

		// slå av musikk
		SoundPlayer.getInstance().musicEnabled = true;
		player1Control = new ShipControl();

		// Forsøk å finne skjerm-modus og lage et vindu.
        screen = new Screen();
		screen.setTitle("Apomorph");

        if( !screen.setResolution( 1024, 768) && !screen.setResolution( 800, 600 ) ) {
            Log.log( "Couldn't find a suitable resolution, probably Linux" );
	        screen.forceWindowedResolution( 1024, 768 );
        }

		try {
			MenuRenderer menus = new MenuRenderer( screen, player1Control );
			while( menus.mainMenu() == "StartGame" ) {
				startGame();
			}

		} catch ( Exception e ) {
			// Prøver å rydde opp etter oss:
			screen.restoreScreen();
			screen.hide();
			screen.dispose();
			e.printStackTrace();
		}
        System.exit( 0 );
	}

	private void checkSystemProperties() {
		if( System.getProperty( "os.name").matches( ".indows.*" ) ) {
            os = "windows";
		} else {
			os = "unix";
		}
		Log.log( "Operating system found: " + os );
		if( System.getProperty( "java.version" ).matches( "..[0-3].*") ) {
			System.out.println( "Sorry, you must have at least Java JRE 1.4.0");
			System.exit( -1 );
		}
	}

	/**
	 * Dette starter selve spillet.
	 */
	private void startGame() {
		String levelName = initialLevel;
		LoadingRenderer lr = new LoadingRenderer( screen );
		GameRenderer gr = new GameRenderer( screen.getCurrentResolution(), screen.isFullScreen );
        LevelLoader ll;

		Player ship = new Player( gr, initialPlayerLevel );
		gr.addObject(ship);
		ship.setControls( player1Control );


		do {
	        ll = new LevelLoader( gr, levelName, lr );
			lr.setLoadingText( "Loading level " + ll.levelNumber() + "..." );
	        ll.start();
			while( !ll.loadingDone() ) {
				try { Thread.sleep( 50 ); } catch( Exception e ) {}
				screen.renderOnce( lr );
			}
			screen.addKeyListener( player1Control );
	        gr.setLevel( ll.getLevel() );

			if( screen.isFullScreen ) {
				ship.getScoreboard().setDrawEachFrame( true );
			}

	        lr.setSubText( "Press any key to start level." );
	        screen.renderLoop( lr );
			Runtime.getRuntime().gc();

	        do {
		        screen.clearScreen();
		        gr.drawScoreboards();
		        screen.renderLoop( gr );
		        // Vis pausetekst
		        if( gr.pause ) {
                    lr.setLoadingText( "Game Paused" );
			        lr.setSubText( "Press any key to continue ");
			        lr.done = false;
			        screen.renderLoop( lr );
			        gr.pause = false;
			        gr.done = false;
		        }
	        } while( ! (gr.quit || gr.getLevelDone()) );

            screen.removeKeyListener( player1Control );
			ship.setAllKeys( false );
			levelName = ll.nextLevel();

			SoundPlayer.getInstance().stopLoop();
        } while( ! (gr.quit || ll.lastLevel()) );

		lr.setLoadingText("Game Over");
		lr.setSubText( ship.getScoreboard().getScore() +  " points" );
		lr.done = false;
		screen.renderLoop( lr );

		try {
			HighScore hs = new HighScore();
			hs = hs.getHighScoreFromFile( HIGH_SCORE_FILE );

			if( hs.isHighScore( ship.getScoreboard().getScore() ) ) {
                HighScoreRenderer hsr = new HighScoreRenderer( screen.getCurrentResolution() );
				screen.renderLoop( hsr );
				hs.addHighScore( hsr.getInput(), ship.getScoreboard().getScore() );
				hs.saveToFile( HIGH_SCORE_FILE );
            }

  		} catch( IOException e) {
			Log.log( "Tull og tøys på gang i highscore filen" );
		} catch( NullPointerException e ) {
			Log.log( "Kunne ikke finne highscore filen." );
		}

		//cleanups
		ll.resetLevelNumber();
	}
}

