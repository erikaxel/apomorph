/**
 * Level1 Skudd
 *
 * @see GameObject
 * @author Egil S�rensen (egil@stud.ntnu.no)
 */



public class Shot1E extends EnemyShot {

    /**
	 * Konstruktor
	 * @see EnemyShot
	 */

    public Shot1E( GameRenderer gr, int x, int y ) {
	    super( gr, "shot2", x, y, 0 );
		_damage = 1;
		_xSpeed = -30;
    }


    /**
	 * Konstruktor hvor fart opp kan stilles
	 * @see EnemyShot
	 */

	public Shot1E( GameRenderer gr, int x, int y, int ySpeed ) {
		super( gr, "shot6", x, y, ySpeed );
		_damage = 1;
		_xSpeed = -30;
	}




    /**
	 * Konstruktor hvor b�de fart bort og opp kan stilles
	 * @see EnemyShot
	 */

    public Shot1E( GameRenderer gr, int x, int y , int xSpeed, int ySpeed) {
	    super( gr, "shot6", x, y, 0 );
		_damage = 1;
		_xSpeed = xSpeed;
    }


	/**
	 * Gj�r ingenting (g�r bare rett frem)
	 */

	public void action() {
    	// NOOP
	}
}
