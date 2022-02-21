/** En stor slem boss
  *	@author Jørgen Braseth (jorgebr@stud.ntnu.no), Egil Sørensen (egil@stud.ntnu.no)
  * @see EnemyObject
  */

class Boss1 extends EnemyObject  {
	private final int burstDuration = 40;
	private final int idleTime = 50;
	private int burstLeft;
	private int state;
	private boolean override;


	/**
	 *  Konstruktor
	 *  @see EnemyObject
	 */

	public Boss1( GameRenderer gr, Integer x, Integer y, Integer option ) {
		super( gr, "boss1", x, y, option, 1000, 18000 );
		state = 8;
		override = true;
	}


    /** Stor slem boss som går opp og ned
     *  og skyter slemme burster
     */

    public void action() {




		switch (state)
		{
			case 0:
			{
				if (y > 0)
				{
					_ySpeed = -5;
				}
				else
				{
					state = 1;
				}
				break;
			}


			case 1:
			{
				_ySpeed = 3;
				_xSpeed = -6;
				if (x < ((int)_gameRenderer.getWidth()*1/3))
				{
					state = 2;
				}
				break;
			}

			case 2:
			{
				_ySpeed = 3;
				_xSpeed = 6;

				if (x > _gameRenderer.getWidth() - 200)
				{
					state = 3;
				}



				break;
			}

			case 3:
			{
				_ySpeed = -2;
				_xSpeed = -10;

				if (x > ((int)_gameRenderer.getWidth() * 2/3))
				{
					state=4;
				}
				break;
			}

			case 4:
			{

				_xSpeed = 15;

				if (x > (_gameRenderer.getWidth() - 200))
				{
					state=0;
				}

				break;
			}

			case 8:
			{
				SoundPlayer.getInstance().play("mayham.wav");
				_xSpeed = -5;

				if (_age > 10)
				{
					state=0;
					override = false;
				}
			}


			default:
			{
				break;
			}
		}



		if (x > _gameRenderer.getWidth() - 150 && !override)
		{
			x = _gameRenderer.getWidth() -150;
		}



	    if (_age > idleTime) { burst(); }

	    if (_age % 50 == 0) { fire(3); }



	}


    private void burst()
    {


		if (burstLeft > 0)

		{
			if( (_age % 2) == 0){fire(1);}
		    if( (_age % 2) == 1){fire(2);}
		    burstLeft--;
		}

		else
		{
			burstLeft = burstDuration;
			_age = 0;
		}



	}


    private void fire(int cannon) {


		switch (cannon)
		{
			case 1:
			{
				_gameRenderer.addObject(new Shot1E(_gameRenderer,(x+width/2) - 40,(y+height/2) + 70 - ((_age%5) * 3)));
				SoundPlayer.getInstance().play("autofire.wav");
				break;
			}
			case 2:
			{
				_gameRenderer.addObject(new Shot1E(_gameRenderer,(x+width/2) - 40,(y+height/2) + 70 + (((_age + 3) %5) * 3)));
				break;
			}
			case 3:
			{
				_gameRenderer.addObject(new ShotCloud(_gameRenderer,x+width/2,y+5,3));
			}
			default:
			{
				//noop
				break;
			}
		}

    }
}


