package weka.util;

import static weka.Container.documentBuilder;
import static weka.Container.elementToMarkableConverter;
import static weka.Container.markableService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import weka.domain.Markable;

public class MarkableProvider {

	public List<Markable> provide(File file) throws Exception {
		List<Markable> markables = new ArrayList<>();

		Document document = documentBuilder.parse(file);
		NodeList markableNodeList = document.getElementsByTagName("markable");
		for (int i = 0; i < markableNodeList.getLength(); i++) {
			Element element = (Element) markableNodeList.item(i);
			Markable markable = elementToMarkableConverter.convert(element);
			markables.add(markable);
		}

		return markableService.sortAndUnique(markables);
	}

}
