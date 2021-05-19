package mutoclient.config;

import java.util.List;

public class Format {
	public static class INIFormat {
		public static String listToStringComma(List<String> list) {

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < list.size(); i++) {
				sb.append(list.get(i));
				if (i != (list.size() - 1)) {
					sb.append(",");
				}

			}
			return sb.toString();

		}
	}
}
