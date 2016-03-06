import org.apache.tika.Tika;

import java.io.File;
import java.util.*;

public class fileClassify {

	public static String cwd = System.getProperty("user.dir") + "\\";
    public static void main(String[] args) throws Exception {
		
        Tika tika = new Tika();
		HashMap<String, ArrayList<String>> hashmap = new HashMap<String, ArrayList<String>>();

        for (String arg : args) {
			
            File dir = new File(arg);
            String[] paths = dir.list();
            for(String fName:paths) {
				
				String curLocation = cwd + arg + "/" + fName;
				System.out.println("curLocation : " + curLocation);
				File curfile =new File(curLocation);
				if(curfile.isDirectory())
					continue;
                String fileType = tika.detect(curfile);
                System.out.print("fileType : " + fileType);
                System.out.println(" : " + fName);
				String directory = fileType.replace('/','_');
				//System.out.println(directory);
				
				if(hashmap.containsKey(directory)){
					(hashmap.get(directory)).add(fName);
					//System.out.println("No of files : " + (hashmap.get(directory)).size());
				}
				else{
					ArrayList<String> arraylist = new ArrayList<String>();
					arraylist.add(fName);
					//System.out.println("added " + directory + " key ");
					hashmap.put(directory, arraylist);
					//create a directory
					File newDir = new File(cwd + arg + "/" + directory);
					if(newDir.mkdir())
						System.out.println("\nDirectory created  : " +cwd + arg + "/" + directory + "\n");
					else
						System.out.println("Failed to create " +cwd + arg + "/" + directory);
				}

				
				String newlocation = cwd + arg + "/" + directory + "/" + fName;
				if(curfile.renameTo(new File(newlocation))){
					System.out.println("File is moved successful! to " +  newlocation);
				}else{
					System.out.println("File is failed to move! to " +  newlocation);
				}

            }
        }

		System.out.println("\n\nFile Classification Status with current run ( NOTE : Files already classified into directories are not accounted!!)");
		for (Map.Entry<String, ArrayList<String>> entry : hashmap.entrySet()) {
			String key = entry.getKey();
			ArrayList<String> valueList = entry.getValue();
			System.out.println(key + " : " + valueList.size() );
		}
    }
}
