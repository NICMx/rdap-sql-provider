package mx.nic.rdap.sql;

/**
 * Utilities to process Data
 *
 */
public class Util {
	/**
	 * Prepared statement don't allow set a unique value for an "in" clause, so,
	 * you have to manually add the list of parameters as ?
	 */
	public static String createDynamicQueryWithInClause(int listSize, String query) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < listSize; i++) {
			builder.append("?,");
		}
		String dynamicQuery = query.replaceFirst("\\?", builder.deleteCharAt(builder.length() - 1).toString());
		return dynamicQuery;
	}

}
