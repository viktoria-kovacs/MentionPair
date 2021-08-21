package weka.util;

import static weka.Container.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import weka.domain.FileSet;

public class FileSetProvider {

	public List<FileSet> provide(File folder) {
		List<FileSet> fileSets = new ArrayList<>();

		for (File parsedFile : folder.listFiles(config.filenameFilter)) {
			fileSets.add(new FileSet(parsedFile));
		}

		return fileSets;
	}

}
