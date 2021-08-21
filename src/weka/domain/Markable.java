package weka.domain;

import com.google.common.collect.Range;

public class Markable {

	private String mmaxLevel;
	private int id;
	private Range<Integer> span;

	public String getMmaxLevel() {
		return mmaxLevel;
	}

	public void setMmaxLevel(String mmaxLevel) {
		this.mmaxLevel = mmaxLevel;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Range<Integer> getSpan() {
		return span;
	}

	public void setSpan(Range<Integer> span) {
		this.span = span;
	}

	@Override
	public String toString() {
		return "Markable [mmaxLevel=" + mmaxLevel + ", id=" + id + ", span=" + span + "]";
	}

}
