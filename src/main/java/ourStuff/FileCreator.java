package ourStuff;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import net.sourceforge.plantuml.SourceStringReader;

public class FileCreator {

	public void getPNG(String text) throws IOException{
		SourceStringReader read = new SourceStringReader(text);
		File file = new File("C:/Users/moormaet/Documents/GitHub/term-project/src/main/java/ourStuff/picture.png");
		read.generateImage(file);
		System.out.println("open " + file.getAbsolutePath());
		Desktop.getDesktop().open(file);
	}
}
