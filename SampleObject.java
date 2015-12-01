/** Et lite testobjekt. Gj�r absolutt ingenting.
	Man kan i denne sette image filen. Det er ikke vanlig for GameObject.
	@author Erik Axel Nielsen (erikaxel@stud.ntnu.no)
  */

public class SampleObject extends GameObject {

	/**
	 * Konstrukt�r.
	 * @param gr GameRenderen som eier objektet.
	 * @param baseImageName Basenavnet p� en bildefil som objektet skal ha.
	 */
	public SampleObject( GameRenderer gr, String baseImageName, int x, int y ) {
		super( gr, baseImageName, x, y );
	}

	/**
	 * Dette objektet gj�r ingen verdens ting.
	 */
	public void action() {

	}

	/**
	 * Reagerer ikke p� kollisjon.
	 * @param otherObject Det andre objektet.
	 */
	public void collide(GameObject otherObject) {
		// NOOP
	}
}