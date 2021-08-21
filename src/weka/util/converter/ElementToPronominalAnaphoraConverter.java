package weka.util.converter;

import org.w3c.dom.Element;

import weka.domain.PronominalAnaphora;

public class ElementToPronominalAnaphoraConverter extends XmlElementConverter {

	public PronominalAnaphora convert(Element element) {
		PronominalAnaphora pronominalAnaphora = new PronominalAnaphora();

		pronominalAnaphora.setId(getId(element.getAttribute("id")));
		pronominalAnaphora.setSpan(getSpanRange(element.getAttribute("span")));
		pronominalAnaphora.setAnteSubAnaphoric(element.getAttribute("ante_sub_anaphoric"));
		pronominalAnaphora.setCorefClass(element.getAttribute("coref_class"));
		pronominalAnaphora.setMmaxLevel(element.getAttribute("mmax_level"));
		pronominalAnaphora.setType(element.getAttribute("type"));
		pronominalAnaphora.setChainHead(element.getAttribute("chain_head"));

		return pronominalAnaphora;
	}

	@Override
	protected String getIdPrefix() {
		return "markable";
	}

}
