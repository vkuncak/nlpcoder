package selection.loaders;

import java.util.List;
import java.util.Map;

import definitions.ClassInfo;

import selection.IWordExtractor;
import selection.WordExtractorFromName;

public interface IJarLoader {
	Map<String, ClassInfo> getClassFiles(List<String> jarFiles, IWordExtractor extractor);
}
