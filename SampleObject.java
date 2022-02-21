/** Et lite testobjekt. Gjør absolutt ingenting.
	Man kan i denne sette image filen. Det er ikke vanlig for GameObject.
	@author Erik Axel Nielsen (erikaxel@stud.ntnu.no)
  */

public class SampleObject extends GameObject {

	/**
	 * Konstruktør.
	 * @param gr GameRenderen som eier objektet.
	 * @param baseImageName Basenavnet på en bildefil som objektet skal ha.
	 */
	public SampleObject( GameRenderer gr, String baseImageName, int x, int y ) {
		super( gr, baseImageName, x, y );
	}

	/**
	 * Dette objektet gjør ingen verdens ting.
	 */
	public void action() {

	}

	/**
	 * Reagerer ikke på kollisjon.
	 * @param otherObject Det andre objektet.
	 */
	public void collide(GameObject otherObject) {
		// NOOP
	}
}