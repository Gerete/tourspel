package nl.gerete.tourspel;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import to.etc.util.FileTool;

import java.io.File;
import java.util.Objects;
import java.util.Properties;

/**
 * Access the property file found in the .developers.properties file.
 *
 * @author <a href="mailto:marc.mol@itris.nl">Marc Mol</a>
 * @since Jan 28, 2014
 */
public class PropertyFile {

	static private final PropertyFile m_propertyFile = new PropertyFile();

	@Nullable
	public static Properties m_properties;

	private boolean m_initialized;

	static public void initialize(@NonNull File configFile) throws Exception {
		m_propertyFile.init(configFile);
	}

	@NonNull
	static public PropertyFile getInstance() {
		m_propertyFile.checkInit();
		return m_propertyFile;
	}

	private synchronized void checkInit() {
		if(!m_initialized)
			throw new IllegalStateException("PollingWorkerQueue has not been initialized");
	}

	/**
	 * Initialize:
	 *
	 * @throws Exception
	 */
	private synchronized void init(@NonNull File configFile) throws Exception {
		m_properties = FileTool.loadProperties(configFile);
		m_initialized = true;
	}

	@Nullable
	public String getProperty(@NonNull String parameter) {
		return Objects.requireNonNull(m_properties).getProperty(parameter);
	}

	@Nullable
	public String getProperty(@NonNull String parameter, @Nullable String defaultValue) {
		return Objects.requireNonNull(m_properties).getProperty(parameter, defaultValue);
	}
}

