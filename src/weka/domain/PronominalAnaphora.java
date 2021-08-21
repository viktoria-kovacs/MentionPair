package weka.domain;

public class PronominalAnaphora extends Markable {

	private String anteSubAnaphoric;
	private String corefClass;
	private String type;
	private String chainHead;

	public String getAnteSubAnaphoric() {
		return anteSubAnaphoric;
	}

	public void setAnteSubAnaphoric(String anteSubAnaphoric) {
		this.anteSubAnaphoric = anteSubAnaphoric;
	}

	public String getCorefClass() {
		return corefClass;
	}

	public void setCorefClass(String corefClass) {
		this.corefClass = corefClass;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getChainHead() {
		return chainHead;
	}

	public void setChainHead(String chainHead) {
		this.chainHead = chainHead;
	}

	@Override
	public String toString() {
		return "PronominalAnaphora [id=" + getId() + ", span=" + getSpan() + ", anteSubAnaphoric=" + anteSubAnaphoric
				+ ", corefClass=" + corefClass + ", mmaxLevel=" + getMmaxLevel() + ", type=" + type + ", chainHead="
				+ chainHead + "]";
	}

}
