import org.apache.tika.Tika;

import java.io.File;

public class fileAnalyse {

	public static String cwd = System.getProperty("user.dir") + "\\";
    public static void main(String[] args) throws Exception {
		String dirAnalyse = args[0];
		Tika tika = new Tika();
		File dir = new File(dirAnalyse);
		String[] paths = dir.list();
		for(String fName:paths) 
		{
			String curLocation = cwd + dirAnalyse + "\\" + fName;
			//System.out.println("curLocation : " + curLocation);
			File curfile = new File(curLocation);
			String fileType = tika.detect(curfile);
			System.out.print("fileType : " + fileType);
			System.out.println(" : " + fName);
		}
	}
}