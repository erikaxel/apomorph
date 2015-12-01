import java.util.prefs.PreferencesFactory;
import java.util.prefs.Preferences;

/**
 * Returns do-nothing Preferences implementation.  We don't use this
 * facility, so we want to avoid the hassles that come with the JVM's
 * implementation. 
 */
public class DisabledPreferencesFactory implements PreferencesFactory {

    public Preferences systemRoot() {
        return new DisabledPreferences();
    }

    public Preferences userRoot() {
        return new DisabledPreferences();
    }
}
