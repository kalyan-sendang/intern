package org.practise.task.task1;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipFile;

public class ReadAndSave {
    public void convertToJson(String zipFilePath) {
        String csvOutputDir = "/home/zakipoint/Internship/task/src/main/resources/output";
        File dir = new File(csvOutputDir); // dir file object is created in path cvsOutputDir
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File mergedCsvFile = new File(dir, "mergedData.csv");
        try (ZipFile zipFile = new ZipFile(zipFilePath)) { //ZipFile class is used for reading ZIP files
            try (PrintWriter writer = new PrintWriter(new FileWriter(mergedCsvFile))) {
                // file writer used to write character data to file
                // Print Writer provide convenient way to write formatted text, different data types
                writer.println("bCls,bC,bCT,negA,npi,tin,tinT,zip,negT,negR,posH,mdH,nrP,_dT");

                zipFile.stream().forEach(entry -> {
                    if (!entry.isDirectory() && entry.getName().endsWith(".json.gz")) { // checks directory or .json.gz file or not
                        try {
                            //inputStreamReader converts bytes from gzipInputStream to character
                            //Buffered Reader wraps inputStreamReader and providing buffering functionality and convenient method for reading text
                            // reads data from specified entry in zip file after data is decompressed by GzipInputStream
                            InputStream inputStream = zipFile.getInputStream(entry);// Abstract class which is used to read bytes
                            GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);//subclass of InputStream and read GZIP format file and decompress it
                            BufferedReader reader = new BufferedReader(new InputStreamReader(gzipInputStream));

                            String line;
                            while ((line = reader.readLine()) != null) {
                                // Convert JSON object to CSV row
                                JSONObject jsonObject = new JSONObject(line);
                                writer.println(jsonObjectToCsvRow(jsonObject));//convert jsonObject into csv-formatted strings

                            }
                            System.out.println("saved successfully");
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to convert a JSON object to a CSV row
    private String jsonObjectToCsvRow(JSONObject jsonObject) {
        StringBuilder rowContent = new StringBuilder();

        rowContent.append(jsonObject.get("bCls")).append(",");
        rowContent.append(jsonObject.get("bC")).append(",");
        rowContent.append(jsonObject.get("bCT")).append(",");
        rowContent.append(jsonObject.get("negA")).append(",");
        rowContent.append(jsonObject.get("npi")).append(",");
        rowContent.append(jsonObject.get("tin")).append(",");
        rowContent.append(jsonObject.get("tinT")).append(",");
        rowContent.append(jsonObject.get("zip")).append(",");
        rowContent.append(jsonObject.get("negT")).append(",");
        rowContent.append(jsonObject.get("negR")).append(",");
        rowContent.append(jsonObject.get("posH")).append(",");
        rowContent.append(jsonObject.get("mdH")).append(",");
        rowContent.append(jsonObject.get("nrP")).append(",");
        rowContent.append(jsonObject.get("_dT"));
        return rowContent.toString();
    }

    public static void main(String[] args) {
        String zipFilePath = "/home/zakipoint/Downloads/sample-data.zip";
        ReadAndSave task = new ReadAndSave();
        task.convertToJson(zipFilePath);
    }
}
