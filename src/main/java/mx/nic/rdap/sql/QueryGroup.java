package mx.nic.rdap.sql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import mx.nic.rdap.db.exception.http.NotImplementedException;

/**
 * A bunch of queries read from a .sql file.
 */
public class QueryGroup {

	private static final String SCHEMA_KEY = "{schema}";

	private static Pattern SCHEMA_PATTERN = Pattern.compile(Pattern.quote(SCHEMA_KEY));

	/** The queries, indexed by means of their names. */
	private Map<String, String> queries;

	private static String DEFAULT_SQL_FILES_DIR = "META-INF/sql/";

	/**
	 * Loads the query group described by file <code>file</code>.
	 * 
	 * @param file
	 *            name of the file where the queries are stored, minus
	 *            extension. The function will assume that the file can be found
	 *            in the classpath, under the "META-INF/sql/" directory.
	 * @throws IOException
	 *             Problems reading the <code>file</code> file.
	 */
	public QueryGroup(String file, String schema) throws IOException {
		InputStream in;

		if (SQLProviderConfiguration.isUserSQL()) {
			in = getStreamFromUserDir(file);
			if (in == null) {
				queries = Collections.emptyMap();
				return;
			}
		} else {
			String filePath = DEFAULT_SQL_FILES_DIR + file + ".sql";
			in = QueryGroup.class.getClassLoader().getResourceAsStream(filePath);
		}

		queries = new HashMap<String, String>();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

			StringBuilder querySB = new StringBuilder();
			String queryName = null;
			String currentLine;

			while ((currentLine = reader.readLine()) != null) {
				if (currentLine.startsWith("#")) {
					queryName = currentLine.substring(1).trim();
				} else {
					querySB.append(currentLine);
					if (currentLine.trim().endsWith(";")) {
						// If the query has no name, it will be ignored.
						if (queryName != null) {
							String queryString = querySB.toString();
							if (schema != null) {
								queryString = SCHEMA_PATTERN.matcher(queryString).replaceAll(schema);
							}
							String oldQuery = queries.put(queryName, queryString);
							if (oldQuery != null) {
								throw new IllegalArgumentException("There is more than one '" + queryName + "' query.");
							}
						}
						querySB.setLength(0);
					} else {
						querySB.append(" ");
					}
				}
			}
		}
	}

	private InputStream getStreamFromUserDir(String file) throws IOException {
		String userSQLFilesPath = SQLProviderConfiguration.getUserSQLFilesPath();
		if (userSQLFilesPath != null) {
			Path path = Paths.get(userSQLFilesPath, file + ".sql");
			return Files.newInputStream(path);
		}

		userSQLFilesPath = Paths.get(SQLProviderConfiguration.DEFAULT_USER_SQL_FILE_PATH, file + ".sql").toString();
		return SQLProviderConfiguration.class.getClassLoader().getResourceAsStream(userSQLFilesPath);
	}

	/**
	 * Returns the query whose name is <code>name</code>.
	 * 
	 * @param name
	 *            label that (in the file) prefixes the query along with a '#'
	 *            symbol.
	 * @return the query whose name is <code>name</code>.
	 */
	public String getQuery(String name) {
		return queries.get(name);
	}

	/**
	 * @return the queries
	 */
	public Map<String, String> getQueries() {
		return queries;
	}

	/**
	 * For the main objects (entity, ns, domain, asn, ip), if the user overwrite
	 * the queries, check that the query to be used is available.
	 * 
	 * @param query
	 *            Query string to check
	 * @throws NotImplementedException
	 *             If the user wrote the queries and did not write the query to
	 *             use.
	 */
	public static void userImplemented(String query) throws NotImplementedException {
		if (SQLProviderConfiguration.isUserSQL() && query == null) {
			throw new NotImplementedException();
		}

		return;
	}

}
