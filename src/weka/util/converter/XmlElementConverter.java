package weka.util.converter;

import com.google.common.collect.Range;

public abstract class XmlElementConverter {

	protected int getId(String id) {
		return Integer.parseInt(id.replaceAll("^" + getIdPrefix() + "_", ""));
	}

	protected Range<Integer> getSpanRange(String span) {
		String[] endpoints = span.split("\\.\\.");
		endpoints[0] = endpoints[0].replaceAll("word_", "");
		Integer min = Integer.valueOf(endpoints[0]);
		Integer max = null;
		if (endpoints.length == 1) {
			max = min;
		} else {
			endpoints[1] = endpoints[1].replaceAll("word_", "");
			try {
				max = Integer.valueOf(endpoints[1].split(",")[0]);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		return Range.closed(min, max);
	}

	protected abstract String getIdPrefix();

}
