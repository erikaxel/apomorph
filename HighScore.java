
import java.io.*;

/**
 * @author Erik Axel Nielsen (erikaxel@stud.ntnu.no)
 */
public class HighScore implements Serializable {
	private String[] _names;
	private int[] _scores;
    private int _numScores;

	public HighScore() {
		_names = new String[10];
		_scores = new int[10];
		_numScores = 0;
	}

	public String[] getNames() {
		return _names;
	}

	public int[] getScores() {
		return _scores;
	}

	public int getNumScores() {
		return _numScores;
	}


	public boolean isHighScore( int score ) {
		for( int i=0; i<_numScores; i++ ) {
			if( _scores[i] < score ) {
				return true;
			}
		}
		return false;
	}

	public void addHighScore( String name, int score ) {
		if( !isHighScore( score ) ) {
			return;
		}
		for( int i=0; i<_numScores; i++ ) {
			if( _scores[i] < score ) {
            	shiftRight( i );
				_scores[i] = score;
				_names[i] = name;
				if( _numScores != 9 ) _numScores++;
				return;
			}
		}
		// Vi kom på sisteplass:
		//shiftRight( _numScores );
		//_scores[ _numScores ] = score;
		//_names[ _numScores ] = name;
	}

	private void shiftRight( int fromIndex ) {
		for( int i=_numScores; i>=fromIndex; i-- ) {
        	if( i != 9 ) { // hvis den er helt på slutten, ignorer
	            _names[i+1] = _names[i];
		        _scores[i+1] = _scores[i];
	        }
		}
	}

	public void printHighScore() {
		for( int i=0; i<_numScores; i++ ) {
			Log.log( "Number " + (i + 1) + " is " + _names[i] + " with " + _scores[i] );
		}
	}

	HighScore getHighScoreFromFile( String fileName ) throws IOException {
		try {
    	    File fil = new File( fileName );
			if( !fil.exists() ) {
				generateDefault();
				saveToFile( fileName );
			}
			ObjectInputStream ois = new ObjectInputStream( new FileInputStream( new File( fileName ) ) );
			return (HighScore)ois.readObject();
		} catch( InvalidClassException e ) {
            generateDefault();
			saveToFile( fileName );
			return getHighScoreFromFile( fileName);
		} catch( NullPointerException e ) {
			e.printStackTrace();
			throw new IOException();
		} catch( ClassNotFoundException e ) {
			e.printStackTrace();
			throw new IOException();
		}
	}

	/**
	 * Denne funksjonen lager en standard high-score fil.
	 */
	public void generateDefault() {
		_numScores = 7;
		_names[0] = new String( "Catbert" );
        _scores[0] = 100000;
		_names[1] = new String( "Zaphod Beeblebrox" );
        _scores[1] = 50000;
		_names[2] = new String( "Captan Kirk" );
        _scores[2] = 20000;
		_names[3] = new String( "Roger Wilco" );
        _scores[3] = 10000;
		_names[4] = new String( "Marvin the martian" );
        _scores[4] = 5000;
		_names[5] = new String( "CmdrTaco" );
        _scores[5] = 4000;
		_names[6] = new String( "Kjell Magne B" );
        _scores[6] = 3000;
		_names[7] = new String( "Spock" );
        _scores[7] = 2000;
		/*
		_names[8] = new String( "Yoda" );
        _scores[8] = 1000;
		_names[9] = new String( "Danner" );
        _scores[9] = 500;
          */
	}

	public void saveToFile( String fileName ) throws IOException {

        File file = new File( fileName );

		try {
            FileOutputStream fos = new FileOutputStream( file );
            ObjectOutputStream oos = new ObjectOutputStream( fos );

           oos.writeObject( this );

		} catch ( FileNotFoundException e ) {
			throw new IOException( "Feil i saving av higscore fil." );
		}
	}
}
