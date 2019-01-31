package pl.tscript3r.dbdd.utility;

import org.apache.commons.io.FileUtils;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

import static pl.tscript3r.dbdd.utility.Logger.print;

class FileIO {
	
	public static boolean isValidPath(String path) {
		try {
			Paths.get(path);
		} catch (InvalidPathException | NullPointerException e) {
			return false;
		}
		return true;
	}

	public static boolean isExistingPath(String path) {
		return Files.exists(Paths.get(path));
	}
	
	public static void savePage(Document page, String savePath) throws IOException {
		File f = new File(savePath + ".htm");
		print("Saving ", f.getName());
		FileUtils.writeStringToFile(f, page.outerHtml(), "UTF-8");
	}
}
