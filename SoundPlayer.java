import javax.sound.midi.*;
import java.net.URL;
import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import java.util.HashMap;


/**
 * Denne klassen tar seg av avspilling av lyder og musikk
 * @author Knut Auvor Grythe (knutauvo@stud.ntnu.no)
 */
public class SoundPlayer {
	private static SoundPlayer currentPlayer;
	private static final int MIDI_EOT = 47;
	private Sequencer sequencer = null;
	private Synthesizer synthesizer = null;
	private Receiver synthReceiver = null;
	private Transmitter seqTransmitter = null;
    private String _dir;
	private HashMap soundMap;
	private HashMap musicMap;
	public boolean musicEnabled;


	public static SoundPlayer getInstance(){
		if (currentPlayer == null) {
			currentPlayer = new SoundPlayer();
		}
		return currentPlayer;
	}

	private SoundPlayer(){
		soundMap = new HashMap();
		musicMap = new HashMap();
		musicEnabled = true;
		_dir = "";

		try {
			initializeMidi();
		} catch (MidiUnavailableException e) {
			Log.error("SoundPlayer: Denne boksen har ingen ledige MIDI-ressurser");

		}
	}

	/**
	 * Initialize MIDI <CODE>Sequencer</CODE> and <CODE>Synthesizer</CODE>
	 * @throws MidiUnavailableException
	 */
	private void initializeMidi() throws MidiUnavailableException{
		/* Initialize the Sequencer */
		sequencer = MidiSystem.getSequencer();
		if (sequencer == null) {
			System.out.println( "Ooops. Vi har ingen sequencer :-(" );
		}

		/*
		 * Make the stream loop by restarting it when we hit
		 * End Of Track (EOT)
		 */
		sequencer.addMetaEventListener(new MetaEventListener(){
			public void meta(MetaMessage event) {
				if (event.getType() == MIDI_EOT) {
					sequencer.setTickPosition( (long) 0 );
					sequencer.start();
				}
			}
		});

		/* Open the Sequencer */
		sequencer.open();

		/* Initialize the Synthesizer */
		synthesizer = MidiSystem.getSynthesizer();
		synthesizer.open();
		synthReceiver = synthesizer.getReceiver();
		seqTransmitter = sequencer.getTransmitter();
		seqTransmitter.setReceiver(synthReceiver);
	}

	/**
	 * Velger en MIDI-fil og starter å spille den i loop.
	 * @param midiFile Filen som skal spilles av
	 */
	public void startLoop(String midiFile){
		midiFile = _dir + midiFile;
		if (musicEnabled) {
			Log.log("Starting playback of " + midiFile);
			//File file = new File(midiFile);
			//File file = getMusic( midiFile );

			Sequence sequence = null;
			if (sequencer.isRunning()) {
				sequencer.stop();
			}
			sequence = getMusic( midiFile );
			if (sequence != null) {
				try {
					sequencer.setSequence(sequence);
				} catch (InvalidMidiDataException e) {
					e.printStackTrace();
				}
				sequencer.start();
			} else {
				System.out.println("\nJava sliter litt med å finne en "
						+ "sequence i denne fila. Prøv en annen.");
			}
		}
	}

	/**
	 * Starter å spille fra gjeldende sted i loopen
	 */
	public void resumeLoop(){
		if (musicEnabled) {
			sequencer.start();
		}
	}

	/**
	 * Pauser avspillingen av MIDI-loopen
	 */
	public void pauseLoop(){
		sequencer.stop();
	}

	/**
	 * Stopper avspillingen av MIDI-loopen og spoler tilbake
	 */
	public void stopLoop(){
		sequencer.stop();
		sequencer.setTickPosition( (long) 0 );
	}

	/**
	 * Setter hastigheten på midi-sangen
	 * @param speed Farten. 1.0 er vanlig fart, 2.0 er dobbel, etc.
	 */
	public void loopSpeed(float speed) {
		sequencer.setTempoFactor(speed);
	}

	/**
	 * Justerer hastigheten på midi-lyden
	 * @param adjustment Tallet det justeres med.
	 *		Positivt tall justerer opp, negativt justerer ned.
	 */
	public void loopSpeedAdjust(float adjustment) {
		sequencer.setTempoFactor( sequencer.getTempoFactor()
		                          + adjustment );
	}

	/**
	 * Multipliserer hastigheten på midi-sangen med <CODE>multiplier</CODE>
	 * @param multiplier Tallet man skal multiplisere med
	 */
	public void loopSpeedMultiply(float multiplier) {
		sequencer.setTempoFactor( sequencer.getTempoFactor()
		                          * multiplier );
	}

	/**
	 * Spiller av en fil av typen WAV, AIFF, AU eller MIDI.
	 * @param soundFile Filen som skal spilles av
	 */
	public void play(String soundFile){
		AudioClip sound = null;

		sound = getSound( soundFile );
		sound.play();
	}

	/**
	 * Setter hvilken dir lyder skal hentes fra
	 * @param soundDir diren lyder skal hentes fra
	 */
	public void setSoundDir( String soundDir ) {
		_dir = soundDir;
	}

	/**
	 * Returnerer en URL som peker til en fil
	 * @param fileName filnavnet til fila
	 * @return en URL som peker til fila
	 */
	private URL getURL( String fileName ){
		return getClass().getResource( fileName );
	}

	/**
	 * Legger til en rekke med lyder
	 * @param soundNames Array med filnavnene
	 */
	public void addSounds( String[] soundNames ) {
		for( int i=0; i<soundNames.length; i++ ) {
			addSound( soundNames[i] );
		}
	}

	/**
	 * Legger til en lyd
	 * @param soundName filnavnet til lyden
	 * @return Filen som ble lagt til.
	 */
	public AudioClip addSound( String soundName ){
		if (!soundMap.containsKey(soundName)) {
			AudioClip sound = null;
			try {
				sound = Applet.newAudioClip( getURL( soundName ) );
			} catch (NullPointerException e) {
				Log.error("SoundPlayer: Feil ved lasting av " + soundName);
				return null;
			}
			soundMap.put( soundName, sound );
			Log.log( "SoundPlayer: " + soundName + " cached.");
			return sound;
		} else {
			return null;
		}
	}

	/**
	 * Henter en lyd fra cachet, og cacher den om den ikke allerede er cachet
	 * @param soundName filnavnet til lyden
	 */
	public AudioClip getSound( String soundName){
		AudioClip file;

		soundName = _dir + soundName;

		file = (AudioClip) soundMap.get( soundName );
		if (file == null) {
			file = addSound( soundName );
		}
		return file;
	}

	/**
	 * Legger til en rekke med musikk
	 * @param musicNames Array med filnavnene
	 */
	public void addMusic( String[] musicNames ) {
		for( int i=0; i<musicNames.length; i++ ) {
			addMusic( musicNames[i] );
		}
	}

	/**
	 * Legger til en lyd
	 * @param musicName filnavnet til lyden
	 * @return Filen som ble lagt til.
	 */
	public Sequence addMusic( String musicName ){
		if (musicEnabled) {
			Sequence music;
			musicName = _dir + musicName;
			if (!musicMap.containsKey(musicName)) {
				try {
					music = MidiSystem.getSequence( getURL( musicName ) );
				} catch (Exception e) {
					Log.error("Feil ved lasting av " + musicName);
					e.printStackTrace();
					return null;
				}
				musicMap.put( musicName, music );
				Log.log( "MusicPlayer: " + musicName + " cached.");
				return music;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * Henter en lyd fra cachet, og cacher den om den ikke allerede er cachet
	 * @param musicName filnavnet til lyden
	 */
	private Sequence getMusic( String musicName){
		Sequence file;

		file = (Sequence) musicMap.get( musicName );
		if (file == null) {
			file = addMusic( musicName );
		}
		return file;
	}

}
