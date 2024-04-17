-->Task 1<---
I have created task1 folder and inside it ReadAndSave.class to read sample-data.zip data,
convert it into mergedData.csv and saved locally.
-Initialized the zipFilePath variable with the path to the ZIP file containing the data to be processed.
-The convertToJson method is called with the zipFilePath as an argument.
Inside the convertToJson method:
-A directory path for the output CSV files is defined.
-A File object dir is created to represent this directory path.
-If the directory doesn't exist, it is created.
-A mergedCsvFile object is created within this directory with the name "mergedData.csv".
-A ZipFile object zipFile is created to handle the input ZIP file specified by zipFilePath.
-A PrintWriter object writer is created to write to the mergedCsvFile.
-The header row for the CSV file is written to mergedCsvFile.
-For each entry in the ZIP file:
--If the entry is not a directory and ends with ".json.gz":
 ---An InputStream is obtained from the ZIP file entry.
 ---A GZIPInputStream is created to decompress the data from the input stream.
 ---A BufferedReader is created to read text from the decompressed input stream.
 ---Each line of JSON data is read from the buffered reader, converted to a JSONObject, and then converted to a CSV row using jsonObjectToCsvRow() method.
 ---The CSV row is written to the output file.
 ---Upon completion of processing, "saved successfully" message is printed.
-Exceptions such as IOException and JSONException are caught and printed.
-After processing all entries in the ZIP file or encountering an exception, the ZipFile object and PrintWriter object are closed in their respective try-with-resources blocks.


-->Task 2 and 4<--
For task 2, I have created configuration, entity, controller, service and repository classes to read and
save .csv file data into the elastic search
--Configuration
    --ElasticApplicationConfiguration.class
        -This is the configuration class where the class has extend ElasticsearchConfiguration which provides additional
        configuration or customization. The clientConfiguration method is overridden and
        configures Elasticsearch client settings:
            --connects to localhost.
            --Configures SSL settings using buildSSLContext() method.
            --Sets basic authentication credentials
--Entity
        - @Data annotation to use any getter, setter needs.
        - @Document annotation is used to mark a Java class as being mapped to a document in an Elasticsearch index.
        - And Respective name is given
        - This is used as a instance by spring data elasticsearch data to map with elasticsearch document
--repository
    --CSVRepository.class
        -it extends ElasticseachRepostiory<CSVData, String> which enables it to interact with Elasticsearch and perform CRUD (Create, Read, Update, Delete) operations on CSVData documents in the Elasticsearch index.
--Controller
    --CsvElasticSearchController.class
        -"/api/insert-csv-to-elasticsearch" is created to insert data into elasticsearch
        -"/api/get-data" to find out top 10 CPT codes by cost(negotiated rates)

--Service
    --CsvElasticsearchService
       ---For saving data into elastic search
      --Method insertCsvDataToElasticsearch():
            I have Defined a method to insert CSV data into Elasticsearch.
      It Specifies the file path of the CSV file (filePath) and the batch size (batchSize)
      and Reads the CSV file using a BufferedReader. It Skips the first line (header line) of the CSV file, processes the CSV data in batches and read each line of the CSV file.
      It Parses the CSV data into an array of strings, converts the array of strings into a CSVData entity using the createEntityFromCsv() method and adds the entity to a batch list.
      Checks if the batch size limit is reached:
      If reached, calls the saveBatch() method to save the batch to Elasticsearch.
      It clears the batch list. After processing all lines, saves any remaining documents in the batch.
      It also catches and prints IOException if any occurs during file reading.

     --Method saveBatch(List<CSVData> batch):
      It saves a batch of CSVData entities to Elasticsearch using the csvRepository bean.

      Method createEntityFromCsv(String[] data):
      It converts an array of CSV data strings into a CSVData entity and Sets the fields of the entity based on the CSV data array.
executeElasticsearchQuery

   --For elastic query execution
   -->executeElasticsearchQuery()
    --This method executes an Elasticsearch query to retrieve documents matching certain criteria,
     retrieves the top 10 results,
     filters out null values, maps the results to DTOs, and returns the DTOs as a list.

--Task 3--
    The query is saved in Elasticsearch and sql folder inside resources folder.