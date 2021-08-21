package weka.util.converter;

import org.w3c.dom.Element;

import weka.domain.Markable;

public class ElementToMarkableConverter extends XmlElementConverter {

	public Markable convert(Element element) {
		Markable markable = new Markable();

		markable.setMmaxLevel(element.getAttribute("mmax_level"));
		markable.setId(getId(element.getAttribute("id")));
		markable.setSpan(getSpanRange(element.getAttribute("span")));

		return markable;
	}

	@Override
	protected String getIdPrefix() {
		return "markable";
	}

}
