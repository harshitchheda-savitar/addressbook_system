package addressbook.utils;

import java.util.List;

public class StringUtils {

	public static String replacePlaceHolders(String query, List<String> replaceValues) {

		if (replaceValues == null)
			return query;
		for (String values : replaceValues) {
			query = query.replaceFirst("\\?", "'" + values + "'");
		}
		return query;
	}
}
