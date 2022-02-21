import java.io.*;
import java.util.*;
import java.lang.reflect.Constructor;

/**
 * Denne klassen laster inn et _levelName fra en
 * tekstfil og lager de aktuelle objektene.
 *
 * @author Knut Auvor Grythe
 */
public class LevelLoader extends Thread {
	private GameRenderer _gr;		// gamerendereren
	private String _levelName;			// filnavnet til levelet
	private static String _levelDir; // diren som levlene ligger i.
	private HashMap _objectMap; // levelet vi holder på med nå.
	private HashMap _musicMap;
    private String _nextLevel;
	private boolean _lastLevel;
	private boolean _LevelLoadingDone;
	private LoadingRenderer _loadingRenderer;
	private static int levelNumber = 1;

	/*
	 * Genererer et _levelName-objekt fra spesifiserte fil
	 */
	LevelLoader( GameRenderer gr, String initalLevel, LoadingRenderer loadingRenderer ){
		_gr = gr;
		_loadingRenderer = loadingRenderer;
		setLevel( initalLevel );
		_LevelLoadingDone = false;
	}

	/**
	 * Bytt _levelName
	 * @param levelfile Filnavnet til det nye levelet
	 */
	private void setLevel( String levelfile ){
		Log.log("LevelLoader: Setting level to '" + levelfile + "'");
		_levelName = _levelDir + levelfile;
	}
	
	/**
	 * Skifter til neste level
	 */
	public String nextLevel() {
		setLevel( _nextLevel );
		levelNumber++;
		return _nextLevel;
	}

	/**
	 * Setter levelnummer til 1
	 */
	public void resetLevelNumber(){
		levelNumber = 1;
	}

	/**
	 * Sjekker om dette er det siste levelet
	 * @return true om dette levelet er sist, false ellers
	 */
	public boolean lastLevel(){
		return _lastLevel;
	}

	/**
	 * Etter at loadLevel() er ferdig, kan man hente levelet her.
	 * @return levelet som ble laget
	 */
	public Level getLevel() {
		return new Level( _objectMap, _musicMap );
	}

	/**
	 * Returns the number of the level currently loading.
	 * @return the number of the level
	 */
	public int levelNumber() {
		return levelNumber;
	}

	/**
	 * Returnerer om levelet er ferdig med å laste
	 * @return true om lastingen er fullført, false ellers
	 */
	public boolean loadingDone() {
		return _LevelLoadingDone;
	}

	/**
	 * Gjør i utgangspunktet ingenting
	 */
	public void run() {
		_objectMap = new HashMap();
		_musicMap = new HashMap();

		if( levelNumber == 1 ) {
			loadFirst();
	    }

		try {
			readLevel();
		} catch (IOException e) {
			Log.error("Level.java: IOException ved lesing av " + _levelName + ": " + e.getMessage() );
			// Avslutt
			_gr.done = true;
			_gr.quit = true;
		}
		_LevelLoadingDone = true;
	}

	private void loadFirst() {
		LinkedList players  = _gr.getAllObjects( GameRenderer.LAYER_PLAYER );

		ListIterator iter = players.listIterator( 0 );

		_loadingRenderer.setSubText( "Loading player graphics" );
        while( iter.hasNext() ) {
	        Player player = (Player)iter.next();
	        player.loadGraphics();
        }
  	}

	/**
	 * Leser inn en fil og kaller <CODE>addObjects</CODE> for å generere fiender
	 * @throws IOException
	 */
	private void readLevel() throws IOException{
		int i = 0;
		int line = 0;
		int[] params = new int[6];
		boolean nextLevelExists = false;
		String type = null;
		String musicFile;
		InputStreamReader in = null;
		BufferedReader b = null;
		StreamTokenizer t = null;
		
		try {
			in = new InputStreamReader( getClass().getResourceAsStream( _levelName ) );
			b = new BufferedReader(in);
			t = new StreamTokenizer(b);
		} catch (NullPointerException e){
			throw new IOException("No such file");
		}
		// Tweak the tokenizer
		t.parseNumbers();
		t.eolIsSignificant(true);
		t.wordChars(041,126);

		/*
		 * Dette ble litt drøyt med indentering.
		 * Kunne blitt mindre om jeg kastet exceptions for hver syntax error
		 * i stedet for å bare printe feilmelding og gå videre, men da ville
		 * man ikke få se alle syntax errorene på en gang.
		 * Kjappere å debugge levels slik det er nå.
		 */
		do {
			t.nextToken();
			line = t.lineno();
			if ((t.ttype != t.TT_WORD) && (t.ttype != t.TT_EOL)
					&& (t.ttype != t.TT_EOF)) {
				Log.error(_levelName + ": Syntax error on line " + line + ": Does not begin with a keyword");
			} else {
				if (t.ttype == t.TT_WORD) {
					if ("//".equals(t.sval) || "#".equals(t.sval)
							|| ";".equals(t.sval)) {
						//Just a comment. Ignore.
					} else if ("enemy".equalsIgnoreCase(t.sval)
							|| "powerup".equalsIgnoreCase(t.sval)) {
						t.nextToken();
						if (t.ttype != t.TT_WORD) {
							Log.error(_levelName + ": Syntax error on line " + line + ": Should be a name "
							        + "after 'enemy' or 'powerup'");
						} else {
							type = t.sval;
							i = 0;
							while ((i < 6) && (t.nextToken() == t.TT_NUMBER)) {
								params[i] = (int) t.nval;
								i++;
							}
							if (i < 5) {
								Log.error(_levelName + ": Syntax error on line " + line +": Only " + i +" numbers, "
										+ "should be at least 5");
							} else if ((params[3] > 100) || (params[4] > 100)) {
								Log.error(_levelName + ": Syntax error on line " + line  +": posX and posY can't exceed 100.");
							} else {
								if (i < 6) {
									params[5] = 0;
								}
								addObjects(type, params, line );
							}
						}
					} else if ("music".equalsIgnoreCase(t.sval)) {
						t.nextToken();
						if (t.ttype != t.TT_WORD) {
							Log.error(_levelName + ": Syntax error on line " + line + ": Should be a filename "
							        + "after 'music'");
						} else {
							musicFile = t.sval;
							t.nextToken();
							if (t.ttype != t.TT_NUMBER) {
								Log.error(_levelName + ": Syntax error on line " + line +
								        ": Should be a start-time after the filename");
							} else {
								//only cache if music is enabled
								if (SoundPlayer.getInstance().musicEnabled) {
									addMidi( musicFile, (int)t.nval );
								}
							}
						}
					} else if ("nextlevel".equalsIgnoreCase(t.sval)){
						t.nextToken();
						if (t.ttype != t.TT_WORD) {
							Log.error(_levelName + ": Syntax error on line " + line +
							        ": Should be a filename after 'nextlevel'");
						} else {
							_nextLevel = t.sval;
							nextLevelExists = true;
						}
					//new keywords can be added here
					} else {
						Log.error(_levelName + ": Syntax error on line " + line + ": Unknown keyword '"+t.sval+"'");
					}
				}
			}

			// skip to end of line if not already there
			while ((t.ttype != t.TT_EOL) && (t.ttype != t.TT_EOF)) {
				t.nextToken();
			}
		} while (t.ttype != t.TT_EOF);
		b.close();
		
		if (nextLevelExists) {
			_lastLevel = false;
		} else {
			_lastLevel = true;
			_nextLevel = "";
		}
	}

	/**
	 * Add a Midi song to <CODE>_musicMap</CODE>
	 * @param file The name of the song
	 * @param ticks When the song should begin to play
	 */
	private void addMidi(String file, int ticks){
		Integer ticksObject = new Integer(ticks);

		_loadingRenderer.setSubText( "Loading sound: " + file );
		
		//cache the sound
		SoundPlayer.getInstance().addMusic(file);

		if (_musicMap.containsKey(ticksObject)){
			String oldFile = (String) _musicMap.remove(ticksObject);
			Log.error(_levelName + ": Syntax error: " + oldFile + "'s appearance at "
					+ "tick " + ticks + " is overridden by " + file);
		}
		_musicMap.put(ticksObject, file);
	}

	public static void setLevelDir( String levelDir ) {
		_levelDir = levelDir;
	}

	/**
	 * Legger til et objekt i <CODE>_objectMap</CODE>
	 * @param ticks Når objektet skal dukke opp i spillet
	 * @param object Selve objektet
	 */
	private void addObject(int ticks, GameObject object ){
		Integer tickObject = new Integer(ticks);
		ArrayList objectsThisTick;

		if ( _objectMap.containsKey(tickObject)) {
			objectsThisTick = (ArrayList) _objectMap.remove(tickObject);
		} else {
			objectsThisTick = new ArrayList();
		}
		objectsThisTick.add(object);
		_objectMap.put(tickObject, objectsThisTick);
	}

	/**
	 * Legger til objekter i <CODE>_objectMap</CODE> ved hjelp av
	 * {@link #addObject(int, GameObject)}
	 * @param name Navnet på objektet
	 * @param params Inneholder 5 elementer. Antall, Når den første dukker opp,
	 *               hvor lenge det blir mellom hver, x-posisjon og y-posisjon.
	 * @param line Linjenummer. Brukes i feilmeldinger.
	 */
	private void addObjects(String name, int[] params, int line ){
		int amount = 0;
		int ticks = 0;
		int interval = 0;
		int posX = 0;
		int posY = 0;
		int options = 0;

		//paranoia: there shouldn't be any negative values. just making sure.
		for (int i=0; i<6; i++) {
			if (params[i] < 0) {
				Log.log(_levelName + ": Syntax error on line " + line + ": "
						+ "param " + i + " is negative!");
			}
		}

		//give arguments readable names
		amount = params[0];
		ticks = params[1];
		interval = params[2];
		posX = params[3] * _gr.getWidth() / 100;
		posY = params[4] * _gr.getHeight() / 100;
		options = params[5];
		
		//create valid enemies
		if (amount == 0) {
			Log.log(_levelName + ": Syntax error on line " + line + ": "
					+ name + " specified with 0 amount");
		} else {
			GameObject o = createObject(name, posX, posY, options);
			addObject(ticks, o);
			for (int i = 1; i < amount; i++) {
				ticks += interval;
				o = createObject(name, posX, posY, options);
				if (o != null) {
					addObject(ticks, o);
				} else {
					Log.log(_levelName + ": Syntax error on line " + line + ": "
							+ "No such object " + name);
				}
			}
		}
	}

	/**
	 * Lager et objekt som tilsvarer navnet i <code>name</code>
	 * @param name Navnet på objektet som skal lages
	 * @param posX X-koordinaten til det nye objektet
	 * @param posY Y-koordinaten til det nye objektet
	 * @param options Options til fienden
	 */
	private GameObject createObject(String name, int posX, int posY, int options)  {
		_loadingRenderer.setSubText( "Loading " + name );
		
		if(options == 0) {
			options = 1;
		}
		
		Object[] params = new Object[4];
		params[0] = _gr;
		params[1] = new Integer(posX);
		params[2] = new Integer(posY);
		params[3] = new Integer(options);
		
		try {
			Class classobj = ClassLoader.getSystemClassLoader().loadClass(name);
			Class gamerenderer = _gr.getClass();
			Class integer = params[1].getClass();
			Class[] paramTypes = { gamerenderer, integer, integer, integer };
			Constructor constructor = classobj.getConstructor(paramTypes);
			return (GameObject) constructor.newInstance(params);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error("LevelLoader: Kunne ikke lage " + name);
			return null;
		}
	}
}
