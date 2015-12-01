public final class HighResTimer {
	private double time;
	private native double getTime();
	private static boolean _useNative;
	/** Gir oss frekvensen på klokken i Hz */
	private native double getFrequency();

	static {
		try {
			System.loadLibrary( "HighResTimer" );
	        _useNative = true;
		} catch ( UnsatisfiedLinkError e ) {
			Log.log( "Couldn't load dll's. Falling back to JVM timer." );
            _useNative = false;
		}
	}

	/**
	 * Konstruktør
	 */
	public HighResTimer() {
		timeElapsed();
	}

	/**
	 * Vil returnerer antall sekunder siden forrige gang timeElapsed() ble kalt.
	 * Hvis den ikke er kalt før, vil den returnere tid siden objektet ble laget.
	 * @return Antall sekunder siden forrige kall.
	 */
	public double timeElapsed() {
		double previousTime = time;
		if( _useNative ) {
			time = getTime();
		} else {
			time = System.currentTimeMillis() / 1000;
		}
		return time - previousTime;
	}

	public double getFrequencyInHz() {
		if( _useNative ) {
			return getFrequency();
		} else {
			return 1000.0; // standard linux oppløsning
		}
	}

    /**
     * Kun for testing.  Jeg fikk ca 1ms som beste tid.
     * Men dette kan selvsagt også være pga Thread.sleep()
     * @param args Bare tull
     */
	public static void main( String[] args ) {
		HighResTimer hrt = new HighResTimer();

		try {
			double avgSum = 0;
			int sum = 100;
			for( int i=0;i<sum;i++ ) {
				hrt.timeElapsed();
				Thread.sleep( 0,500 );
                avgSum += hrt.timeElapsed();
			}
            System.out.println( "Time: " + avgSum / sum );

		} catch ( Exception e ) {
			e.printStackTrace();
		}

	}
}