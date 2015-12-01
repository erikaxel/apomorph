/**
 *
 *	Monster som teleporterer seg rundt omkring på skjermen
 *  og skyter etter hvor du er hen
 *
 * @author Egil Sørensen
 * @see EnemyObject
 */

class SpliBall extends EnemyObject  {
	private int shake;
	GameObject target;
	private int shotDirection;
	private final int ageShake = 80;
	private final int ageRest = 20;
	private final int shakeDuration = 20;


	/**
	*
	* Konstruktor
	* @see EnemyObject
	*
	*/

	public SpliBall( GameRenderer gr, Integer x, Integer y, Integer option ) {
		super( gr, "spliball1", x, y, option, 10, 1000);
		_damage = 1;
		shake = shakeDuration;

	}



    /**
     * Står i ro og skyter, teleporterer seg av og til
     */

	public void action() {

		if (_age > ageShake)
		{

			shakeMonster();

		}

		else if (_age % 5 == 0 && _age > ageRest)
		{
			_xSpeed = 0;
			_ySpeed = 0;
			fire();


		}




	}

	private void fire() {
		target = _gameRenderer.getRandomObject(_gameRenderer.LAYER_PLAYER);

		if (target.x > x)
		{
			shotDirection = 30;
		}
		else
		{
			shotDirection = -30;
		}


		_gameRenderer.addObject(new Shot1E(_gameRenderer,x+5,y+20, shotDirection, 0));

	}

	private void shakeMonster() {
		if (_xSpeed == 0 || _ySpeed == 0)
		{
			_xSpeed = 10;
			_ySpeed = 10;
		}

		if (shake > 0)
		{
			_age = ageShake * 2;

			_xSpeed = -_xSpeed;
			_ySpeed = -_ySpeed;
			shake--;

		}
		else
		{
			_age = 0;
			shake = shakeDuration;
			_xSpeed = 0;
			_ySpeed = 0;
			teleport();
		}
	}

	private void teleport()
	{
		x = (int) (Math.random() * _gameRenderer.getWidth());
		y = (int) (Math.random() * _gameRenderer.getHeight());
	}




}
