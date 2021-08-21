package weka.util;

import static weka.Container.documentBuilder;
import static weka.Container.elementToWordConverter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import weka.domain.Word;

public class WordFileReader {

	public Map<Integer, Word> readById(File file) throws Exception {
		Map<Integer, Word> words = new HashMap<>();

		Document document = documentBuilder.parse(file);
		NodeList nodeList = document.getElementsByTagName("word");

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				Word word = elementToWordConverter.convert(element);
				words.put(word.getId(), word);
			}
		}

		return words;
	}

}
