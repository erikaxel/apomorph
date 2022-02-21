
import java.awt.*;

/**
 * Denne klassen representerer et valg i en meny.
 * @author Erik Axel Nielsen (erikaxel@stud.ntnu.no)
 */

public class MenuOption {
	private String _text;
	private int _id;
	private int _age;
	/**
	 * Konstruktør.
	 * @param text Teksten som menyen skal ha.
	 * @param id Et tall som unikt identifiserer valget.
	 */
	public MenuOption( String text, int id ) {
		_text = text;
		_id = id;
		_age = 0;
	}

	/**
	 * Returnerer teksten i menyvalget
	 * @return En streng med teksten til menyvalget
	 */
	public String getText() {
		return _text;
	}
	/**
	 * Returnerer id'en til menyvalget.
	 * @return En streng med id'en til valget
	 */
	public int getId() {
		return _id;
	}

	public void drawString( Graphics2D g, int x, int y ) {
		g.drawString( _text, x, y );
	}

	public void drawStringSinus( Graphics2D g, int x, int y ) {
		int textLength = _text.length();
		for( int i=0; i< textLength; i++ ) {
			double yAdd = Math.sin( (i+_age)/Math.PI ) * 5;
			char c = _text.charAt( i );
			g.drawString( String.valueOf( c ),x, (int)(y+yAdd) );
			x += g.getFontMetrics().charWidth( c );
		}
	_age++;
	if( _age == 2147483647 ) _age = 0;
	}
}