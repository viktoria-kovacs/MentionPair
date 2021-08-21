package weka.util;

import static weka.Container.documentBuilder;
import static weka.Container.elementToPronominalAnaphoraConverter;
import static weka.Container.markableService;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import weka.domain.Markable;
import weka.domain.PronominalAnaphora;

public class PronominalAnaphoraProviderFromFile {

	public Collection<List<Markable>> provide(File file) throws Exception {
		Map<String, List<Markable>> pronominalAnaphoras = new HashMap<>();

		Document document = documentBuilder.parse(file);
		NodeList pronominalAnaphoraMarkables = document.getElementsByTagName("markable");
		for (int i = 0; i < pronominalAnaphoraMarkables.getLength(); i++) {
			Element element = (Element) pronominalAnaphoraMarkables.item(i);
			PronominalAnaphora pronominalAnaphora = elementToPronominalAnaphoraConverter.convert(element);

			if (!pronominalAnaphoras.containsKey(pronominalAnaphora.getCorefClass())) {
				pronominalAnaphoras.put(pronominalAnaphora.getCorefClass(), new ArrayList<>());
			}
			pronominalAnaphoras.get(pronominalAnaphora.getCorefClass()).add(pronominalAnaphora);
		}

		for (String corefClass : pronominalAnaphoras.keySet()) {
			List<Markable> markables = pronominalAnaphoras.get(corefClass);
			markables = markableService.sortAndUnique(markables);
			pronominalAnaphoras.put(corefClass, markables);
		}

		return pronominalAnaphoras.values();
	}

}
