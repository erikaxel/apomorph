import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.StreamTokenizer;
import java.io.IOException;

/**
 * @author Erik Axel Nielsen (erikaxel@stud.ntnu.no)
 */
public class HighScoreRenderer extends TextRenderer {
    private String[] _names;
	private int[] _scores;
	private int _num;
    private String _input;
	private MenuOption _header1 = new MenuOption( "Congratulations, you got a highscore!",0 );
	private MenuOption _header2 = new MenuOption( "Please enter your name:",0 );

	public HighScoreRenderer( DisplayMode dispMode ) throws IOException {
		super( dispMode );
        _input = "";
	}

	public String getInput() {
		return _input;
	}

	public void renderText(Graphics2D g) {
		int x = (int)(_width * 0.1 );
		int y = _logo.getHeight( null ) + 20;

 		_header1.drawStringSinus( g, x, y );
		y+=100;
        _header2.drawStringSinus( g, x, y );
		y += 100;

       g.drawString( _input, x, y );
	}

	public void keyPressed(KeyEvent e) {
		switch( e.getKeyCode() ) {
			case KeyEvent.VK_ENTER:
				done = true;
		    	break;
			case KeyEvent.VK_SHIFT:
				break;
			case KeyEvent.VK_BACK_SPACE:
				_input = _input.substring(0, _input.length() - 1 );
				break;
			default:
				_input += e.getKeyChar();
				break;
		}
	}
}
