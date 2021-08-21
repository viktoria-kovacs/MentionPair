package weka.util.converter;

import com.google.common.collect.Range;

import weka.domain.Markable;
import weka.domain.Word;

public class WordToMarkableConverter {

	public Markable convert(Word... words) {
		Markable markable = new Markable();
		Range<Integer> span;
		if (words.length == 1) {
			span = Range.singleton(words[0].getId());
		} else {
			span = Range.closed(words[0].getId(), words[1].getId());
		}
		markable.setSpan(span);
		return markable;
	}

}
