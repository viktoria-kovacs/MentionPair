package weka.util.converter;

import org.w3c.dom.Element;

import weka.domain.Word;

public class ElementToWordConverter extends XmlElementConverter {

	public Word convert(Element element) {
		Word word = new Word();

		word.setId(getId(element.getAttribute("id")));
		word.setTextContent(element.getTextContent());

		return word;
	}

	@Override
	protected String getIdPrefix() {
		return "word";
	}

}
