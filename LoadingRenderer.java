import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Denne klassen er en renderer for loading screenen.
 * Vil quitte på any key.
 * @author Erik Axel Nielsen (erikaxel@stud.ntnu.no)
 */

public class LoadingRenderer extends TextRenderer {
	private MenuOption loadingText;
    private MenuOption subText;

	/**
	 * Konstruktørern.
	 * @param screen Skjermen som loadingen skal tegnes på.
	 */
	public LoadingRenderer( Screen screen ) {
		super( screen.getCurrentResolution() );
		loadingText = new MenuOption( "Loading...", 0 );
 	    subText = new MenuOption("", 0);
	}

	/**
	 * Setter en ganske stor tekst midt på skjermen.
	 * Blir tegnet i sinuskurver.
	 * @param text Teksten som skal stå på skjermen
	 */
	public void setLoadingText (String text) {
		loadingText = new MenuOption( text, 0 );
	}

	/**
	 * Setter en undertekst.
	 * Står stille.
	 * @param text Teksten som skal stå på skjermen
	 */
    public void setSubText( String text ) {
	    subText = new MenuOption( text, 0 );
    }

	/**
	 * @see Renderer
	 * @param g
	 */
	public void renderText(Graphics2D g) {
		Font font = ImageMap.getInstance().getFont( g );
		int y = _height / 2;
        int x = (int) (_width * 0.35);
		loadingText.drawStringSinus( g, x,y );

		Font oldFont = font;
		Font smallFont = font.deriveFont( (float) (font.getSize() * 0.4) );
        g.setFont( smallFont );

		subText.drawString( g, x, y+ 100 );

		g.setFont( oldFont );

	}

	/**
	 * @see Renderer
	 * @param e
	 */
	public void keyPressed( KeyEvent e ) {
		done = true;
	}
}
