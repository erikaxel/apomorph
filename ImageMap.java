import java.net.URL;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

/**
 * Singleton som forhåndslaster bildet og gjør dem lett tilgjengelige
 * via en hash-map.
 * Den lagerer bildene i en hash-map, sortert på basenavn.
 * Basenavn er
 * @author Erik Axel Nielsen (erikaxel@stud.ntnu.no)
 * @author Knut Auvor Grythe
 */

class ImageMap {
	private HashMap imageMap;
	private String dir;
	private Font font;
	private Font normalFont;
	private static ImageMap currentMap;

	private ImageMap() {
		imageMap = new HashMap();
		normalFont = new Font( "Serif", Font.PLAIN, 16);
	}

	/**
	 * Få tak i singeltonen som har våre fine flotte bilder.
	 * @return Singeltonen som har bildene
	 */
	public static ImageMap getInstance() {
		if( currentMap == null ) {
			currentMap = new ImageMap();
		}
		return currentMap;
	}
	/**
	 * Setter hvilken directory bildene ligger i.
	 * @param directory Diren der bildene ligger. Husk trailing slash!
	 */
	public void setImageDir( String directory ){
		dir = directory;
	}

	/**
	 * Legger til bildene spesifisert av basename til image-cachen.
	 * @param basename Hvilke bilder som skal legges til
	 */
	public void addImages( String basename[] ) {
		for( int i=0; i<basename.length; i++ ) {
			addImage( basename[i] );
		}
	}
	/**
	 * Legg til et bilde i cachet
	 * @param basename basenavnet til bildet
	 */
	public void addImage(String basename){
		Log.log( "Caching image: " + basename );
		imageMap.put(basename, loadImages(basename));
	}

	/**
	 * Henter et bilde fra map'en.
	 * @param desc Beskrivelsen (basenavnet)
	 * @return Bildet som hører til basenavnet.
	 */
	public Image[] getImage( String desc ) {
		if (!imageMap.containsKey(desc)) {
			//System.out.println( "Her starter jeg caching av: " + desc );
			addImage(desc);
			///System.out.println( "Her er jeg ferdig.");
		}
		return (Image[]) imageMap.get( desc );
	}

	/**
	 * Returnerer integeren med to sifre.
	 * Eksempel: 3 blir 03.
	 * @param i Tallet som skal forandres
	 * @return Tallet med korrekt antall sifre
	 */
	private String pad(int i){
		return ((i < 10) ? "0" : "") + i;
	}

	/**
	 * Legger inn leser filer fra disk og returnerer som array
	 * @param basename basenavnet til bildene.
	 */
	private Image[] loadImages(String basename){
		ArrayList images = new ArrayList();
		ImageIcon ii;
		int i = 1;

		ii = loadImage( dir + basename + "_" + pad( i ) + ".png" );
		while (ii != null) {
			images.add(ii.getImage());
			i++;
			ii = loadImage( dir + basename + "_" + pad( i ) + ".png" );
		}

		return (Image[]) images.toArray( new Image[ images.size() ] );
	}

	/**
	 * returnerer et Image fra fil
	 * @param filename Filnavnet til bildet
	 * @return Bildet som var lagret i filen
	 */
	private ImageIcon loadImage(String filename){
		ImageIcon ii;
		try {
			ii = new ImageIcon( getClass().getResource( filename ) );
		} catch (NullPointerException e){
			return null;
		}
		if (ii.getImageLoadStatus() == MediaTracker.COMPLETE) {
			return ii;
		} else {
			return null;
		}
	}

	/**
	 * Setting the systemfont
	 * @param fontName Name of the TrueType font to load
	 */
	public void setFont( String fontName ) {
		try {
			URL url =  getClass().getResource( fontName );
			if( url == null ) {
				throw new Exception();
			} else {
				InputStream in = url.openStream();
			    Font temp = Font.createFont( Font.TRUETYPE_FONT, in );
			    font = temp.deriveFont( (float)50 );
			}
		} catch (Exception e) {
			Log.log( "Couldn't load font, using normal font" );
		}
	}

	public Font getFont( Graphics2D g ) {
    	if( font == null ) {
		    font = g.getFont();
			font = font.deriveFont( (float)font.getSize() * 3);
	    }
		return font;
	}

	public Font getNormalFont() {
		return normalFont;
	}
}
