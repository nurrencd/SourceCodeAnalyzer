package ourStuff;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

public class FileCreatorAnalyzer implements Analyzer{
	
	private List<Filter> filters;

	public void getSVG(String text) throws IOException{
		SourceStringReader read = new SourceStringReader(text);
		File file = new File("./picture.svg");
		OutputStream outStream = new FileOutputStream(file);
		FileFormatOption option = new FileFormatOption(FileFormat.SVG, false);

		read.generateImage(outStream, option);
		System.out.println("open " + file.getAbsolutePath());
		Desktop.getDesktop().open(file);
	}

	@Override
	public Data analyze(Data data) {
		try {
			this.getSVG(data.stringMap.get("class-diagram-code"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	@Override
	public void addFilter(Filter filter) {
		this.filters.add(filter);
	}
}
