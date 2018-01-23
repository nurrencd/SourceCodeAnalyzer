package design.team.nothing;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

public class FileCreator{

	public void getSVG(String text) throws IOException{
		SourceStringReader read = new SourceStringReader(text);
		File file = new File("./picture.svg");
		OutputStream outStream = new FileOutputStream(file);
		FileFormatOption option = new FileFormatOption(FileFormat.SVG, false);

		read.generateImage(outStream, option);
		System.out.println("open " + file.getAbsolutePath());
		Desktop.getDesktop().open(file);
	}

	
}
