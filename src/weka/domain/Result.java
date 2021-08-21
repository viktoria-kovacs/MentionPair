package weka.domain;

import static weka.Container.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Result {

	private File folder;
	private boolean test;
	private List<ResultLine> lines = new ArrayList<>();

	public Result(File folder, boolean test) {
		this.folder = folder;
		this.test = test;
	}

	public File getFolder() {
		return folder;
	}

	public boolean isTest() {
		return test;
	}

	public void addLine(ResultLine line) {
		lines.add(line);
	}

	public List<ResultLine> getLines() {
		return lines;
	}

	public String getResultFileName(boolean debug) {
		return "resources/result_" + folder.getName() + "_" + config.postfix + "_" + (test ? "test" : "train")
				+ (debug ? "_debug" : "") + ".csv";
	}

}
