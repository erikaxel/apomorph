import java.io.File;
import java.io.FilenameFilter;

/**
 * Denne klassen brukes for å filer basert på en regex.
 * @author Erik Axel Nielsen (erikaxel@stud.ntnu.no)
 */

class MyFileFilter implements FilenameFilter {
	private String _regex;

	public MyFileFilter( String regex ) {
		_regex = regex;
	}

	public boolean accept( File ignore, String filename ) {
		return filename.matches( _regex );
	}
}