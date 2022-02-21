/**
 * Bakgrunnsobjekt
 *
 * @author Egil Sørensen
 * @see GameObject
 */


public class Background extends GameObject  {


/**
 *
 * Konstruktor som stiller hvordan bakgrunnsobjektet skal se ut, samt
 * hvor fort det skal gå
 * @see GameObject
 *
 */
	Background( GameRenderer gr, int x, int y , int speed, String pic) {
		super( gr, pic,x, y );
		_layer = gr.LAYER_BACKGROUND;
		_xSpeed = speed;
		if (y > gr.getHeight() - height)
		{
			y = gr.getHeight() - height;
		}

	}

	/**
	 * Gjør ingenting utenom å bevege seg med angitt fart
	 */

	public void action() {
      //NOOP
	}


	/**
	 * Gjør at objektene slettes når de forsvinner ut
	 * av skjermen
	 */

	public void outOfScreenAction() {
		if (x < 0)
		{
		die();
		}

    }

}
