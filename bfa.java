import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Iterator;

public class bfa {

    public static String path = System.getProperty("user.dir") + "\\";

    public static void computeFreqDist(double[] freq_distribute, String fileName) throws IOException{
        FileInputStream fileInputStream = null;
        //number of occurrences of the most frequent byte value
        double no_of_occurrence_mfb = 0;//max of array entries of freq_distribute
        try {
            int byte_read;
            fileInputStream = new FileInputStream(fileName);
            while ((byte_read = fileInputStream.read()) != -1)
            {
                //System.out.print(" " + data);
                /* 	For	each byte in the file, the appropriate element of the array is incremented by one.
                For example, if the next byte in the file contained the ASCII value 32,
                then array element 32 would be incremented by one. */
                double temp = ++freq_distribute[byte_read];
                if(temp>no_of_occurrence_mfb)
                    no_of_occurrence_mfb = temp;
            }
            /*System.out.println("Frequency Distribution of bytes read : ");
            for(int i=0; i<freq_distribute.length; ++i)
                System.out.print(" " + freq_distribute[i]);
            System.out.println("");*/

            /* 	Once the number of occurrences of each byte value is obtained,
                each element in the array is divided by the number of
                occurrences of the most frequent byte value. This
                normalizes the array to frequencies in the range of 0
                to 1, inclusive. This normalization step prevents one very large file from
                skewing the file type fingerprint. Rather, each input file is provided equal weight
                regardless of size.
            */
            for(int i=0; i<freq_distribute.length; ++i)
                freq_distribute[i] = freq_distribute[i]/no_of_occurrence_mfb;
            /*System.out.println("No_of_occurrence_mfb : " + no_of_occurrence_mfb);
            System.out.println("Normalized Frequency Distribution : ");
            for(int i=0; i<freq_distribute.length; ++i)
                System.out.print(" " + freq_distribute[i]);
            System.out.println("");*/

            /* 	Some file types have one byte value that occurs much more frequently than any
                other. If this happens, the normalized frequency distribution may show a large spike at
                the common value, with almost nothing elsewhere.
                executable file that demonstrates this. The file has large regions filled
                with the byte value zero. The resulting graph has a large spike at byte value zero, with
                insufficient detail to determine patterns in the remaining byte value ranges
                pass the frequency distribution through a companding
                function to emphasize the lower values
                y = x pow (1/β); optimal β = 1.5
                    optimal value of β is defined as the value that produces the greatest difference between
                    the fingerprint with the highest frequency score and
                    the fingerprint with the second-highest frequency score.*/
            float beta = 1.5f;
            for(int i=0; i<freq_distribute.length; ++i)
                freq_distribute[i] = Math.pow(freq_distribute[i], (1/beta));
            /*System.out.println("Companded Normalized Frequency Distribution : ");
            for(int i=0; i<freq_distribute.length; ++i)
                System.out.print(" " +  freq_distribute[i]);
            System.out.println("");*/

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null)
                fileInputStream.close();
        }
    }
    public static void loadFingerPrint(String jsonFilePath, long[] no_of_files, double magnitude[] ,double correlation_strength[])throws IOException{
        JSONParser parser = new JSONParser();

        System.out.println("loadFingerPrint : " + jsonFilePath);
        try {
            FileReader fileReader = new FileReader(jsonFilePath);
            Object obj = parser.parse(fileReader);

            JSONObject jsonObject = (JSONObject) obj;

            no_of_files[0] = (Long) jsonObject.get("freq_no_of_files");
			no_of_files[1] = (Long) jsonObject.get("corr_no_of_files");
            System.out.println("freq_no_of_files: " + no_of_files[0]);
			System.out.println("corr_no_of_files: " + no_of_files[1]);

            JSONArray readData = (JSONArray) jsonObject.get("data");

            Iterator<?> iterator = readData.iterator();
            int i = 0;
            while (iterator.hasNext()) {
                JSONObject d = (JSONObject)(iterator.next());
                magnitude[i] = (Double) d.get("magnitude");
                correlation_strength[i] = (Double) d.get("corr_strength");
                i++;
            }
            /*for(int j=0; j<magnitude.length; ++j)
                System.out.println (j + " :  " + magnitude[j]);
            for(int j=0; j<correlation_strength.length; ++j)
                System.out.println (j + " :  " + correlation_strength[j]);*/
            fileReader.close();
        }
        catch(ParseException pe){
            System.out.println("ParseException at position: " + pe.getPosition());
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    @SuppressWarnings("unchecked")
    public static void populateJsonTempObj(JSONObject temp, double magnitude, double corr_strength){
        temp.put("magnitude",       magnitude);
        temp.put("corr_strength",   corr_strength);
    }
    @SuppressWarnings("unchecked")
    public static void addJsonArray(JSONArray jsonArray, JSONObject obj){
        jsonArray.add(obj);
    }
    @SuppressWarnings("unchecked")
    public static void putKeyArray_JsonObj(JSONObject json_obj, String key, JSONArray jsonArray){
        json_obj.put(key,jsonArray);
    }
    @SuppressWarnings("unchecked")
    public static void putKeyValue_JsonObj(JSONObject json_obj, String key, long no_of_files){
        json_obj.put(key,no_of_files);
    }

    public static void updateFingerPrint(String jsonFilePath,long[] no_of_files,double magnitude[] ,double correlation_strength[])throws IOException{
        System.out.println("updateFingerPrint : " + jsonFilePath + " after parsing : ");
		System.out.println("freq_no_of_files: " + no_of_files[0]);
		System.out.println("corr_no_of_files: " + no_of_files[1]);
        JSONObject jsonObj = new JSONObject();

        JSONArray jsonArray = new JSONArray();
        for(int j=0; j<magnitude.length; ++j) {
            JSONObject tempJsonObj = new JSONObject();
            populateJsonTempObj(tempJsonObj,  magnitude[j], correlation_strength[j]);
            addJsonArray(jsonArray, tempJsonObj);
        }
		putKeyValue_JsonObj(jsonObj, "freq_no_of_files", no_of_files[0]);
		putKeyValue_JsonObj(jsonObj, "corr_no_of_files", no_of_files[1]);
        putKeyArray_JsonObj(jsonObj, "data", jsonArray);
        
        //System.out.println(jsonObj.toJSONString());

        FileWriter fWrite = new FileWriter(jsonFilePath);
        fWrite.write(jsonObj.toJSONString());
        //System.out.println(jsonObj.toJSONString());
        fWrite.flush();
        fWrite.close();

    }
    public static void createFingerPrint(String jsonFilePath)throws IOException{

        JSONObject jsonObj = new JSONObject();
        long freq_no_of_files = 0;
		long corr_no_of_files = 0;
        double magnitude[] = new double[256];
        double correlation_strength[] = new double[256];
        for(int i=0; i<magnitude.length; ++i)
            magnitude[i] = 0;
        for(int i=0; i<correlation_strength.length; ++i)
            correlation_strength[i] = 0;
		
        JSONArray jsonArray = new JSONArray();
        for(int j=0; j<magnitude.length; ++j) {
            JSONObject tempJsonObj = new JSONObject();
            populateJsonTempObj(tempJsonObj,  magnitude[j], correlation_strength[j]);
            addJsonArray(jsonArray, tempJsonObj);//jsonArray.add(temp);
        }
        
        putKeyValue_JsonObj(jsonObj, "freq_no_of_files", freq_no_of_files);
		putKeyValue_JsonObj(jsonObj, "corr_no_of_files", corr_no_of_files);
		putKeyArray_JsonObj(jsonObj, "data", jsonArray);
        //System.out.println(jsonObj.toJSONString());

        FileWriter fWrite = new FileWriter(jsonFilePath);
        fWrite.write(jsonObj.toJSONString());
        //System.out.println(jsonObj.toJSONString());
        fWrite.flush();
        fWrite.close();

    }

    public static void createFingerPrintFiles(String input_directory, String output_directory)throws IOException{
        File dir = new File(path + input_directory);
        String[] fileDirs = dir.list();
        for (String fileDir : fileDirs) {
            String jsonFilePath = path + output_directory + "\\" + fileDir + ".json";
            System.out.println("Creating file FP for : " + jsonFilePath);
            createFingerPrint(jsonFilePath);
        }
        System.out.println("Created Finger Print Json Files");
    }

    public static double bellCorrelation(double x){
        double sigma = 0.0375;
        return Math.exp((-(x*x) / (2*sigma*sigma)) );
    }
    /*public static double linearCorrelation(double x)
    {
        return 1-Math.abs(x);
    }*/

    public static void computeCorrStrength(long corr_no_of_files,double[] freq_distribute,double avg_freq_distribute[] ,double correlation_strength[]){
        /* 	Aside from the byte frequency distributions, there is another related piece of
        information that can be used to refine the comparisons. The frequencies of some byte
        values are very consistent between files of some file types, while other byte values vary
        widely in frequency. For example, note that almost all of the data in the files between byte
        values 32 and 126, corresponding to printable characters in the lower ASCII range.
        This is characteristic of the RichText format. */

        /* 	*	correlation strength between the same byte values in different files can be
            measured, and used as part of the fingerprint for the byte frequency analysis
            *	In other words, if a byte value always occurs with a "regular frequency" for a
            given file type, then this is an important feature of the file type, and is useful in file type identification.
            *	A correlation factor can be calculated by
            comparing each file to the frequency scores in the
            fingerprint. The correlation factors can then be
            combined into an overall correlation strength score
            for each byte value of the frequency distribution. */
        double correlate_factors[] = new double[256];
        /* 	The correlation factor of each byte value for an input file is calculated by
        taking the difference between that byte value’s
        frequency score from the input file and the
        frequency score from the fingerprint.
        x is the difference between the new byte value frequency and
        the average byte value frequency in the fingerprint.*/
        for(int i=0; i<freq_distribute.length; ++i) {
            double diff = freq_distribute[i] - avg_freq_distribute[i];
            /* 	If the difference between the two frequency scores
            is very small, then the correlation strength should increase toward 1. If the difference is
            large, then the correlation strength should decrease toward 0. Therefore, if a byte value
            always occurs with exactly the same frequency, the correlation strength should be 1.
            If the byte value occurs with widely varying frequencies in the input files, then the
            correlation strength should be nearly 0. */
            correlate_factors[i] = bellCorrelation(diff);//linearCorrelation(diff);

            /* 	Once the input file’s correlation factor for each byte value is obtained,
            these values need to be combined with the correlation strengths in the fingerprint.
            This is accomplished by using the following simple averaging equation, which directly
            parallels the method used to calculate the frequency distribution scores*/
            correlation_strength[i] = ((correlation_strength[i] * corr_no_of_files)+correlate_factors[i])/(corr_no_of_files+1);
        }
    }

    public static void performByteFrequencyAnalysis(String input_directory, String output_directory, boolean updateCorrelationStrength){
        File dir = new File(path + input_directory);
        String[] fileDirs = dir.list();
        try {
            for (String fileDir : fileDirs) {
                System.out.println("Analysing fileDir : " + fileDir);
                String jsonFilePath = path + output_directory + "\\" + fileDir + ".json";
                double magnitude[] = new double[256];
                double correlation_strength[] = new double[256];
				long[] no_of_files ={0,0};
                loadFingerPrint(jsonFilePath, no_of_files, magnitude, correlation_strength);
				
                File typeDir = new File(path + input_directory + "\\" + fileDir);
                String[] files = typeDir.list();
				long fileCount=1;
                for(String file : files){
                    String filePath = path + input_directory + "\\" + fileDir + "\\" + file;
                    System.out.println("Analysing file : " + fileCount++ + " : " + filePath);

                    /* II.1.1 Building the byte frequency distribution  */
                    /* The first step in building a byte frequency fingerprint is to count the
                    number of occurrences of each byte value for a single input file.  */
                    /* 	This is done by constructing an array of size 256 (indexed 0 to 255),
                    and initializing all array locations to zero. */
                    double[] freq_distribute = new double [256];
                    for(int i=0; i<freq_distribute.length; ++i)
                        freq_distribute[i] = 0;
                    computeFreqDist(freq_distribute,filePath);

                    /* II.1.2 Combining frequency distributions into a fingerprint
                    A fingerprint is generated by averaging the results of multiple files of a common
                    file type into a single fingerprint file that is representative of the file type as a whole. */
                    /* To add a new file’s frequency distribution to a fingerprint,
                    the fingerprint is loaded, and
                    the frequency distribution for the new file is generated. */
                    if(updateCorrelationStrength)
					{
						computeCorrStrength(no_of_files[1],freq_distribute, magnitude, correlation_strength);
						no_of_files[1]++;
					}
					else
					{
						/* Next, each element of the new file’s frequency distribution array is added to
						the corresponding element of the fingerprint score using the following equation: */
						for(int i=0; i<freq_distribute.length; ++i)
						{
							magnitude[i] = ((magnitude[i] * no_of_files[0])+freq_distribute[i])/(no_of_files[0]+1);
						}
						/* 	This results in a simple average, where the previous fingerprint score is weighted
						by the number of files already loaded into the fingerprint. */
						no_of_files[0]++;
					}
                }
				
                updateFingerPrint(jsonFilePath,no_of_files,magnitude,correlation_strength);
                System.out.println("\n");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws IOException {
        if(args.length == 1)
        {
            if(args[0].compareTo("--help") == 0)
            {
                System.out.println("Usage : java bfa --create <input_directory> <output_directory>");
                System.out.println("Usage : java bfa --analyseFreqency <input_directory> <output_directory>");
				System.out.println("Usage : java bfa --updateCorrStrength <input_directory> <output_directory>");
                return;
            }
        }
        if(args.length != 3) {
            System.out.println("Invalid no of args : " + args.length);
            System.out.println("Usage : java bfa --create <input_directory> <output_directory>");
            System.out.println("Usage : java bfa --analyseFreqency <input_directory> <output_directory>");
			System.out.println("Usage : java bfa --updateCorrStrength <input_directory> <output_directory>");
            return;
        }

        String option = args[0];
        String input_directory = args[1];
		String output_directory = args[2];
        if(option.compareTo("--create") == 0)
        {
            createFingerPrintFiles(input_directory,output_directory);
            return;
        }
        else if(option.compareTo("--analyseFreqency") == 0)
        {
            performByteFrequencyAnalysis(input_directory,output_directory, false);
            return;
        }
		else if(option.compareTo("--updateCorrStrength") == 0)
        {
            performByteFrequencyAnalysis(input_directory,output_directory, true);
            return;
        }
        else{
            System.out.println("Usage : java bfa --create <input_directory> <output_directory>");
            System.out.println("Usage : java bfa --analyseFreqency <input_directory> <output_directory>");
			System.out.println("Usage : java bfa --updateCorrStrength <input_directory> <output_directory>");
            return;
        }
    }
}



