import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;
import java.lang.*;

public class compare{
	
	private static void load(HashMap<String, long[]> hashmap, String jsonFilePath)
	{
		JSONParser parser = new JSONParser();
		try {
            FileReader fileReader = new FileReader(jsonFilePath);
            Object obj = parser.parse(fileReader);

            JSONArray readData = (JSONArray) obj;
			Iterator<?> iterator = readData.iterator();
            while (iterator.hasNext()) {
                JSONObject d = (JSONObject)(iterator.next());
                long count 		= (long)	d.get("count");
                String mimeType = (String) 	d.get("mimeType");
				//System.out.println("count  : " + count);
				if(!hashmap.containsKey(mimeType))
				{
					hashmap.put(mimeType, new long[2]);
				}
				if(jsonFilePath.contains("initialClassification"))
					(hashmap.get(mimeType))[0] = count;
				else
					(hashmap.get(mimeType))[1] = count;
				//System.out.println("(hashmap.get(mimeType))[0]  : " + (hashmap.get(mimeType))[0]);
				//System.out.println("(hashmap.get(mimeType))[1]  : " + (hashmap.get(mimeType))[1]);
            }
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
    public static void populateJsonTempObj(JSONObject temp, String mimeType, long count){
        temp.put("count",       count);
        temp.put("mimeType",   mimeType);
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
	public static void main(String args[])
	{		
		HashMap<String, long[] > hashmap = new HashMap<String, long[]>();
			
		load(hashmap,"initialClassification.json");
		load(hashmap,"finalClassification.json");
		System.out.printf("%80s		%s 		%s		%s\n", "MIME", "Before", "After", "Comparison");
		for (Map.Entry<String, long[]> entry : hashmap.entrySet()) {
			String key = entry.getKey();
			long[] counts = entry.getValue();
			int diff = (int)(counts[1]-counts[0]);
			System.out.printf("%80s		%d 		%d		%d\n", key, counts[0], counts[1], counts[1]-counts[0]);
			//System.out.println(counts[0] + "   " +  counts[1] + "   " + counts[0]-counts[1]);
		}

		JSONArray jsonArray = new JSONArray();
		for (Map.Entry<String, long[]> entry : hashmap.entrySet()) {
			String key = entry.getKey();
			long[] counts = entry.getValue();
			int diff = (int)(counts[1]-counts[0]);
			//System.out.printf("%80s		%d 		%d		%d\n", key, counts[0], counts[1], diff);
			if(diff != 0){
				JSONObject tempJsonObj = new JSONObject();
				populateJsonTempObj(tempJsonObj,  key, diff);
				addJsonArray(jsonArray, tempJsonObj);
			}
		}
		//System.out.println(jsonArray.toJSONString());
		try{
		FileWriter fWrite = new FileWriter("comparison.json");
        fWrite.write(jsonArray.toJSONString());
        fWrite.flush();
        fWrite.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}