package nl.gerete.tourspel;

import to.etc.util.*;

import javax.annotation.*;
import java.io.*;
import java.util.*;

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

	static public void initialize(@Nonnull File configFile) throws Exception {
		m_propertyFile.init(configFile);
	}

	@Nonnull
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
	private synchronized void init(@Nonnull File configFile) throws Exception {
		m_properties = FileTool.loadProperties(configFile);
		m_initialized = true;
	}

	@Nullable
	public String getProperty(@Nonnull String parameter) {
		return Objects.requireNonNull(m_properties).getProperty(parameter);
	}

	@Nullable
	public String getProperty(@Nonnull String parameter, @Nullable String defaultValue) {
		return Objects.requireNonNull(m_properties).getProperty(parameter, defaultValue);
	}
}

