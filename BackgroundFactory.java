
/**
 * Klasse som genererer bakgrunnsobjekter av forskjellige typer
 * slik at det blir laget bakgrunn etter hver som vi "flyr" fremover
 *
 * @author Egil Sørensen
 */
public class BackgroundFactory {

    private GameRenderer gameRenderer;
    private int xStart;
    private int planetType;
    private String planetPic;
    private int tick;

    /**
     * Konstruktor som tar inn en GameRenderer for å vite åssen de forskjellige
     * objektene skal legges til
     */

    public BackgroundFactory(GameRenderer _gameRenderer){

        gameRenderer = _gameRenderer;
        xStart = gameRenderer.getWidth() - 30;
        tick = 1;

    }


    /**
     * Metode som legger til et bakgrunnsobjekt (stjerne) med en tilfeldig fart og y-posisjon
     * hver 10. "turn"
     * @param tick
     */

    public void addBackgroundObject()
    {
        //Stjerner
        if (tick == 1)
        {
            initBackground();
        }
        if (tick % 15 == 0)
        {
            Background bg = new Background(gameRenderer, xStart, (int) (Math.random() * gameRenderer.getHeight()) - 50, -2,"star");
            gameRenderer.addObject(bg);
        }
        if (tick % 20 == 0)
        {
            Background bg = new Background(gameRenderer, xStart, (int) (Math.random() * gameRenderer.getHeight()) - 50,-3,"star2");
            gameRenderer.addObject(bg);
        }
        if (tick % 25 == 0)
        {
            Background bg = new Background(gameRenderer, xStart, (int) (Math.random() * gameRenderer.getHeight()) - 50,-3,"star3");
            gameRenderer.addObject(bg);
        }
        if (tick % 30 == 0)
        {
            Background bg = new Background(gameRenderer, xStart, (int) (Math.random() * gameRenderer.getHeight()) - 50,-4,"star4");
            gameRenderer.addObject(bg);
        }

        //Planeter
        planetType = (int) (Math.random() * 4);

        switch (planetType)
        {

			case 0:
			{
				planetPic = "planet";
				break;
			}
			case 1:
			{
				planetPic = "planet2";
				break;
			}
			case 2:
			{
				planetPic = "planet3";
				break;
			}
			case 3:
			{
				planetPic = "planet4";
				break;
			}
			default:
			{
				planetPic = "planet";
				break;
			}


		}

        if ((tick + ((int) (Math.random() * 100))) % 170 == 0)
        {
            Background bg = new Background(gameRenderer, xStart - 60, (int) (Math.random() * gameRenderer.getHeight()) - 50,-5,planetPic);
            gameRenderer.addObject(bg);
        }

        tick++;



    }

    /**
     * Lager stjerner til starten av spillet
     */
    private void initBackground()
    {
        for (int i = 0; i < 10; i++)
        {
        Background bg = new Background(gameRenderer, (int) (Math.random() * gameRenderer.getWidth()), (int) (Math.random() * gameRenderer.getHeight()), -2,"star");
        gameRenderer.addObject(bg);
        }
        for (int i = 0; i < 10; i++)
        {
        Background bg2 = new Background(gameRenderer, (int) (Math.random() * gameRenderer.getWidth()), (int) (Math.random() * gameRenderer.getHeight()),-3,"star2");
        gameRenderer.addObject(bg2);
        }
        for (int i = 0; i < 10; i++)
        {
        Background bg3 = new Background(gameRenderer, (int) (Math.random() * gameRenderer.getWidth()), (int) (Math.random() * gameRenderer.getHeight()),-3,"star3");
        gameRenderer.addObject(bg3);
        }
        for (int i = 0; i < 10; i++)
        {
        Background bg4 = new Background(gameRenderer, (int) (Math.random() * gameRenderer.getWidth()), (int) (Math.random() * gameRenderer.getHeight()),-4,"star4");
        gameRenderer.addObject(bg4);
        }
    }

}
