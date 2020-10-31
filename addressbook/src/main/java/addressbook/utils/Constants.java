package addressbook.utils;

import java.util.HashMap;
import java.util.Map;

public class Constants {

	public static final Map<String, Integer> ADDRESSBOOK_TYPE_MAP = new HashMap<>() {
		private static final long serialVersionUID = 1L;
		{
			put("General", 1);
			put("Friends", 2);
			put("Family", 3);
			put("Professional", 4);
		}
	};
}
