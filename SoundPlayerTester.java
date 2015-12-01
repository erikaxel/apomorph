import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;

/**
 * Denne klassen brukes utelukkende for å teste SoundPlayer-klassen,
 * og er ikke egentlig en del av prosjektet.
 * 
 * @author Knut Auvor Grythe
 */
public class SoundPlayerTester {

	/**
	 * main-metode for å teste dritten.
	 * Litt grisete, men funker.
	 */
	public static void main(String[] args){
		InputStreamReader stream = new InputStreamReader(System.in);
		BufferedReader buffer = new BufferedReader(stream);
		StreamTokenizer tokenizer = new StreamTokenizer(buffer);
		int tokenType = 0;
		int i = 0;
		String help = (
			  "  **********************************************************\n"
			+ "  * sound <lydfil>  Spill av en lyd                        *\n"
			+ "  * music <midifil> Velg en MIDI-fil                       *\n"
			+ "  * play            Start avspillingen av MIDI             *\n"
			+ "  * pause           Pause avspillingen av MIDI             *\n"
			+ "  * stop            Stopp avspillingen av MIDI             *\n"
			+ "  * speed <tall>    Sett farten til <tall> (1.0 er vanlig) *\n"
			+ "  * quit            Avslutt                                *\n"
			+ "  **********************************************************\n"
		);
		Log.setLogLevel( 4 );
		Log.setErrorLevel( 5 );
		
		System.setProperty("java.util.prefs.PreferencesFactory", "DisabledPreferencesFactory");
						
		/* Tweak the tokenizer */
		tokenizer.parseNumbers();
		tokenizer.wordChars(041,126);
		tokenizer.eolIsSignificant(true);

		/* Print simple help message */
		System.out.println("\nKontroller:");
		System.out.println(help);

		/* Listen for commands */
		while (true) {
			try {
				tokenizer.nextToken();
			} catch (IOException e) {
				System.out.println("Feil ved lesing fra tastatur.");
			}
			if ("sound".equals(tokenizer.sval)) {
				try {
					tokenType = tokenizer.nextToken();
				} catch (IOException e) {
					System.out.println("Feil ved lesing fra tastatur.");
				}
				//FIXME: Bugger når filnavnet starter med et tall
				if (tokenType == tokenizer.TT_WORD) {
					SoundPlayer.getInstance().play(tokenizer.sval);
				} else {
					System.out.println("Syntax: sound <fil>");
				}
			} else if ("music".equals(tokenizer.sval)) {
				try {
					tokenType = tokenizer.nextToken();
				} catch (IOException e) {
					System.out.println("Feil ved lesing fra tastatur.");
				}
				//FIXME: Bugger når filnavnet starter med et tall
				if (tokenType == tokenizer.TT_WORD) {
					SoundPlayer.getInstance().startLoop(tokenizer.sval);
				} else {
					System.out.println("Syntax: music <midifil>");
				}
			} else if ("play".equals(tokenizer.sval)) {
				SoundPlayer.getInstance().resumeLoop();
			} else if ("pause".equals(tokenizer.sval)) {
				SoundPlayer.getInstance().pauseLoop();
			} else if ("stop".equals(tokenizer.sval)) {
				SoundPlayer.getInstance().stopLoop();
			} else if ("speed".equals(tokenizer.sval)) {
				try {
					tokenType = tokenizer.nextToken();
				} catch (IOException e) {
					System.out.println("Feil ved lesing fra tastatur.");
				}
				if (tokenType == tokenizer.TT_NUMBER) {
					SoundPlayer.getInstance().loopSpeed((float)tokenizer.nval);
				} else {
					System.out.println("Syntax: speed <tall>");
				}
			} else if ("quit".equals(tokenizer.sval) 
					|| "exit".equals(tokenizer.sval)) {
				System.exit(0);
			} else {
				System.out.println("\nBruk en av disse, du:");
				System.out.println(help);
			}
			
			/* Skip past extra options */
			i = 0;
			while (tokenizer.ttype != tokenizer.TT_EOL) {
				try {
					tokenizer.nextToken();
					i++;
				} catch (IOException e) {
					System.out.println("Feil ved lesing fra tastatur.");
				}
			}
			if ( i > 1 ) {
				i--; //Ikke tell med EOL
				System.out.println("Du skrev " + i + " options for mye. "
			                     + "Disse ble ignorert.");
			}
		}
	}
}
