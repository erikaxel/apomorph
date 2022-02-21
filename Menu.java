
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * Denne klassen representerer en meny.
 * @author Erik Axel Nielsen (erikaxel@stud.ntnu.no)
 */

public class Menu {
	private ArrayList _optionList;
    private int _selected;
	private MenuPointer _menuPointer;
   	private boolean _paintPointer;

	/**
	 * Konstruktør
	 */
	public Menu() {
		_optionList = new ArrayList();
		_menuPointer = new MenuPointer();
		_paintPointer = true;
	}

	/**
	 * Angir om pekeren på menyskjermen skal vises (det lille skipet)
	 * Verdien er default på true
	 * @param value false hvis du ikke vil vise, true ellers.
	 */
	public void setPaintPointer( boolean value ) {
		_paintPointer = value;
	}

	/**
	 * Legge til et menyvalg
	 * @param menuOption Valget som skal legges til.
	 */
	public void add( MenuOption menuOption ) {
		_optionList.add( menuOption );
	}

	/**
	 * Fjerner alle valgene fra menyen.
	 */
	public void clear() {
		_optionList.clear();
		_selected = 0;
	}

	/**
	 * Går til neste menyvalg.
	 */
    public void next() {
	    if( _selected == ( _optionList.size() -1 ) ) {
		    _selected = 0;
	    } else {
    	    _selected++;
	    }
    }

	/**
	 * Går til forrige menyvalg.
	 */
	public void previous() {
		if( _selected == 0 ) {
   		    _selected = _optionList.size() -1;
		} else {
			_selected--;
	    }
	}

	/**
	 * Når render har returnert, kan man sjekke retur-verdien med denne funksjonen
	 * @return Valg verdi.
	 */
	public int getSelectedId() {
		MenuOption mo = (MenuOption)_optionList.get( _selected );
		return mo.getId();
	}

	/**
	 * Tegner menyen på skjermen.
	 * @param g Et graphics objekt å tegne på.
	 * @param x x startsted for menyen
	 * @param y y startsted for menyen
	 * @param font Fonten å tegne menyen med
	 */
	public void draw( Graphics2D g, int x, int y, Font font ) {
		for( int i=0; i<_optionList.size(); i++ ) {
			MenuOption opt = (MenuOption)_optionList.get( i );
			Rectangle2D bounds = font.getStringBounds( "H", g.getFontRenderContext() ); //
			y += bounds.getHeight() + 20;

			opt.drawStringSinus( g, x, y );
			if( i == _selected && _paintPointer ) {
				_menuPointer.draw( g, x, y);
			}
		}
	}
}


