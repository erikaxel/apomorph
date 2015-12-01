
import java.awt.*;

/**
 * @author Erik Axel Nielsen (erikaxel@stud.ntnu.no)
 */
public abstract class TextRenderer extends Renderer {
	Image _logo;

	public TextRenderer( DisplayMode resolution ) {
		super(resolution);
		_logo = ImageMap.getInstance().getImage( "logo" )[0];
	}


	/**
	 * Starter en loop som returnerer når brukeren har valgt noe.
	 * @param g Et Graphics objekt som vi kan tegne på.
	 */
    public void render( Graphics2D g) {
		g.setFont( ImageMap.getInstance().getFont( g ) );

		g.setColor( Color.BLACK );              // bakgrunnsfarge color
		g.clearRect( 0,0, _width, _height );

		g.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );

		// Tegne logoen vår øverst:
		g.drawImage( _logo, (_width - _logo.getWidth( null ))/2, 0, null );

		g.setColor( Color.WHITE );              // tekst farge

		renderText( g );

	}
    /**
     * Underklassenes egen rendering.
     * @param g Et grafikkobjekt å tegne på
     */
	public abstract void renderText( Graphics2D g );

}
