
import java.awt.*;

/**
 * @author Erik Axel Nielsen (erikaxel@stud.ntnu.no)
 */
public class MenuPointer {
	private Image[] _pointer;
	private int _pointerAlder;
	private int _pointerWidth;
    private int _pointerHeight;

	MenuPointer() {
		_pointer = ImageMap.getInstance().getImage( "menu_arrow" );
		_pointerAlder = 0;
		_pointerWidth = _pointer[0].getWidth( null );
		_pointerHeight = _pointer[0].getHeight( null );
	}

	public void draw( Graphics2D g, int x, int y ) {
		int whichPointer = _pointerAlder++ % _pointer.length;
		int pointerx = x - (_pointerWidth + 10 );
		int pointery = y - (_pointerHeight);
		g.drawImage( _pointer[ whichPointer ], pointerx , pointery, null );
	}
}
