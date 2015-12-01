
import java.awt.*;

/**
 * @author Erik Axel Nielsen (erikaxel@stud.ntnu.no)
 */
public class MenuOptionControls extends MenuOption {
	private MenuOption controltext;
    private boolean _keyTextSet;
    private static final int FRAMES_PR_BLINK = 10;

	public MenuOptionControls( String desc, String keyText, int id ) {
		super( desc, id );
 		controltext = new MenuOption( keyText, 0);
		_keyTextSet = true;

	}

	public void drawStringSinus( Graphics2D g, int x, int y ) {
		super.drawStringSinus( g, x, y );
		if( !_keyTextSet ) {
			g.setColor( Color.WHITE );
			g.setFont( ImageMap.getInstance().getNormalFont() );
			g.drawString( "__________", x+400, y );
			g.setFont( ImageMap.getInstance().getFont( g ) );
		} else {
			controltext.drawStringSinus( g, x+400, y );
		}
	}

	public void setKeyText( String keyText ) {
		controltext = new MenuOption( keyText, 0 );
	}

	public void setKeyTextSet( boolean value ) {
    	_keyTextSet = value;
	}
	public boolean isKeyTextSet() {
		return _keyTextSet;
	}
}
