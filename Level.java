
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Erik Axel Nielsen (erikaxel@stud.ntnu.no)
 */
public class Level {
	private int _tick;
    private HashMap _objectMap;
	private HashMap _musicMap;

	public Level( HashMap objectMap, HashMap musicMap ) {
    	_tick = 0;
		_objectMap = objectMap;
		_musicMap = musicMap;
	}

	/**Tømmer brettet
	 */
	public void clear() {
		_objectMap.clear();
		_musicMap.clear();	
	}
	
	public ArrayList getNextObjects() {
		ArrayList enemiesThisTick;
		Integer tickObject = new Integer( ++_tick );
		String song;

		if ( _musicMap.containsKey(tickObject) && SoundPlayer.getInstance().musicEnabled ){
			song = (String) _musicMap.remove( tickObject );
			SoundPlayer.getInstance().startLoop(song);
			//Log.log(tick + ": Playing " + song);
		}

		enemiesThisTick = (ArrayList) _objectMap.remove(tickObject);
		if (enemiesThisTick == null) {
			enemiesThisTick = new ArrayList();
		}
		return enemiesThisTick;
	}

	/**
	 * Sjekker om levelet er tomt
	 * @return true hvis levelet er tomt, false ellers.
	 */
	public boolean empty() {
		return _objectMap.isEmpty();
	}

	public void printElements() {
		int size = _objectMap.size();
		/*
		for(int i=0; i<size; i++){
			Log.log("Fiende " + i + ": " + _objectMap.get(i).toString());
		}
		*/
		Log.log("Totalt " + size + " fiender igjen i brettet.");
	}
}
