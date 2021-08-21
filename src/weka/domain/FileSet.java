package weka.domain;

import static weka.Container.config;

import java.io.File;

public class FileSet {

	private String name;
	private File parsedFile;
	private File wordFile;
	private File npFile;
	private File cpFile;
	private File pronominalAnaphoraFile;

	public FileSet(File parsedFile) {
		initialize(parsedFile);
	}

	private void initialize(File parsedFile) {
		name = parsedFile.getName();
		if (!config.fileSetNamePostfix.isEmpty()) {
			name = name.replaceAll(config.fileSetNamePostfix, "");
		}

		this.parsedFile = parsedFile;
		String basePath = parsedFile.getParent();
		wordFile = new File(basePath + "/" + config.pathPrefix + name + ".conv.xml");
		npFile = new File(wordFile.getAbsolutePath() + "_np_level.xml");
		cpFile = new File(wordFile.getAbsolutePath() + "_cp_level.xml");
		pronominalAnaphoraFile = new File(wordFile.getAbsolutePath() + "_pronominal.anaphora_level.xml");
	}

	public String getName() {
		return name;
	}

	public File getParsedFile() {
		return parsedFile;
	}

	public File getWordFile() {
		return wordFile;
	}

	public File getNpFile() {
		return npFile;
	}

	public File getCpFile() {
		return cpFile;
	}

	public File getPronominalAnaphoraFile() {
		return pronominalAnaphoraFile;
	}

	@Override
	public String toString() {
		return "FileSet [name=" + name + ", parsedFile=" + parsedFile + ", wordFile="
				+ wordFile + ", npFile=" + npFile + ", cpFile=" + cpFile + ", pronominalAnaphoraFile="
				+ pronominalAnaphoraFile + "]";
	}

}
