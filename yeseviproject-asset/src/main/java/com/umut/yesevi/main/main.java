package com.umut.yesevi.main;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.*;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.transport.TIOStreamTransport;
import org.apache.thrift.transport.TTransport;
import org.json.JSONArray;
import org.json.JSONObject;

import com.umut.yesevi.avro.AssetArrayAvro;
import com.umut.yesevi.avro.AssetAvro;
import com.umut.yesevi.protobuf.AssetProtoArray;
import com.umut.yesevi.protobuf.AssetProto; 
import com.umut.yesevi.thrift.AssetList;
import com.umut.yesevi.thrift.AssetThrift;

public class main {
	private final static String OUTPUT_PATH = "output/";
	private static File outputFile;

	// number of objects to be tested:
	private final static int BASE_TEST_SIZE = 1_000;
	private static int test_size;

	// repating number:
	private final static int REPEATING_NUMBER = 1_000; 

	// test data that will be used for all serialization methods: 
	private static Asset[] assetTestArray;

	// SIZE test variables
	private static long totalFileSize = 0L;
	private static List<Long> serializationSizes;
	private static List<String> serializedFileNames;

	// TIME test variables:
	private static long startTime = 0L;
	private static long finishTime = 0L;
	private static long totalSerializationTime = 0L;
	private static List<Long> serializationTimes;
	private static long totalDeserializationTime = 0L;
	private static List<Long> deserializationTimes;
	
	public static void main(String[] args) throws IOException, TException {
		Scanner s = new Scanner(System.in);
		System.out.println("Starting tests with only-numbers-set");

		for (int i = 0; i < 3; i++) {
			test_size = (int) (BASE_TEST_SIZE * Math.pow(10, i));

			// Creating test data:
			createAssetTestArray();

			// Wait user for CPU-RAM usage logs:
			System.out.println("\nPress Enter key to start test JSON_test_size_" + test_size);
			s.nextLine();
			// *-*-*-*-*-* START TO JSON TEST *-*-*-*-*-*-*-*-
			System.out.println("STARTING TO JSON TEST....");
			jsonAssetTest();
			// Print report:
			printReport("ONLY-NUMBERS-SET JSON REPORT");
			System.out.println("END OF JSON TEST.");

			// Wait user to CPU-RAM usage logs:
			System.out.println("\nPress Enter key to start test THRIFT_test_size_" + test_size);
			s.nextLine();
			// *-*-*-*-*-* START TO THRIFT TEST *-*-*-*-*-*-*-*-
			System.out.println("STARTING THRIFT TEST....");
			thriftAssetTest();
			// Print report:
			printReport("ONLY-NUMBERS-SET THRIFT REPORT");
			System.out.println("END  OF THRIFT TEST.");

			// Wait user for CPU-RAM usage logs:
			System.out.println("\nPress Enter key to start test PROTOBUF_test_size_" + test_size);
			s.nextLine();
			// *-*-*-*-*-* START TO PROTOBUF TEST *-*-*-*-*-*-*-*-
			System.out.println("STARTING PROTOBUF TEST....");
			protobufAssetTest();
			// Print report:
			printReport("ONLY-NUMBERS-SET PROTOBUF REPORT");
			System.out.println("END OF PROTOBUF TEST.");

			// Wait user to CPU-RAM usage logs:
			System.out.println("\nPress Enter key to start test AVRO_test_size_" + test_size);
			s.nextLine();
			// *-*-*-*-*-* START TO AVRO TEST *-*-*-*-*-*-*-*-
			System.out.println("STARTING AVRO TEST....");
			avroAssetTest();
			// Print report:
			printReport("ONLY-NUMBERS-SET AVRO REPORT");
			System.out.println("END OF AVRO TEST.");

			System.out.println("End of test_size " + test_size + " tests. \n\n ");


		}
		System.out.println("End of tests ");
	}

	private static void createAssetTestArray() {
		System.out.println("Creating new test data for " + test_size + " elements...");
		assetTestArray = new Asset[test_size];

		// Creating Asset data:
		for (int i = 0; i < assetTestArray.length; i++) {
			assetTestArray[i] = new Asset((double) 100000000 + i, (float) (0.1 * i), 10 * i, 100 * i);
		}
	}

	private static void resetVariables() throws IOException {
		// Reseting test variables:
		// Deleting old files:
		if (serializedFileNames != null) {
			for (String name : serializedFileNames) {
				new File(name).delete();
			}
		}

		// Reseting SIZE test variables
		totalFileSize = 0L;
		serializationSizes = new ArrayList<Long>();
		serializedFileNames = new ArrayList<String>();

		// Reseting TIME test variables:
		startTime = 0L;
		finishTime = 0L;
		totalSerializationTime = 0L;
		serializationTimes = new ArrayList<Long>();
		totalDeserializationTime = 0L;
		deserializationTimes = new ArrayList<Long>();
	}

	private static void beforeSerialization(String fileSuffix, int element) {
		// setting output file:
		String fileName = fileSuffix + "Test_" + test_size + "_" + System.currentTimeMillis() + "." + fileSuffix;
		String path = OUTPUT_PATH;
		outputFile = new File(path + fileName);

		// save file names for deserialization:
		serializedFileNames.add(path + fileName);

		// Set test variables:
		startTime = System.currentTimeMillis();
	}
	

	private static void afterSerialization(int i) {
		// Get test variables:
		finishTime = System.currentTimeMillis();
		// Do not calculate first 3 processes:
		if(i>2) {
			// set SIZE:
			Long fileSize = outputFile.length();
			serializationSizes.add(fileSize);
			totalFileSize += fileSize;

			// set TIME:
			Long timeForSerialization = finishTime - startTime;
			serializationTimes.add(timeForSerialization);
			totalSerializationTime += timeForSerialization;
		}
	}
	

	private static void beforeDeserialization() {
		// Set test variables:
		startTime = System.currentTimeMillis();
	}
	

	private static void afterDeserialization(int i) {
		// Get test variables:
		finishTime = System.currentTimeMillis();
		// Do not calculate first 3 processes:
		if(i>2) {
			// Set TIME:
			Long timeForDeserialization = finishTime - startTime;
			deserializationTimes.add(timeForDeserialization);
			totalDeserializationTime += timeForDeserialization;
		}
	}
	

	private static void printReport(String reportName) {
		System.out.println("***************************** " + reportName + " *******************************");
		System.out.println("number of data:\t" + test_size);
		System.out.println("repeating number:\t" + REPEATING_NUMBER);

		System.out.println("----------------------------------------------------------------------------------");
		System.out.println("DATA\t\t\t" + "AVG\t\t" + "MIN\t\t" + "MAX\t\t" + "NOTES");

		// SIZE avr min max
		long avrSize = totalFileSize / REPEATING_NUMBER;
		System.out.print("Serialization Size" + "\t");
		System.out.print(avrSize / 1024 + "\t\t");
		System.out.print(Collections.min(serializationSizes) / 1024 + "\t\t");
		System.out.print(Collections.max(serializationSizes) / 1024 + "\t\t");
		System.out.print("kb" + "\t\t");
		System.out.println();
		
		// SERIALIZATION TIME avr min max
		System.out.print("Serialization Time" + "\t");
		System.out.print((double)totalSerializationTime / (double)REPEATING_NUMBER + "\t\t");
		System.out.print(Collections.min(serializationTimes) + "\t\t");
		System.out.print(Collections.max(serializationTimes) + "\t\t");
		System.out.print("ms" + "\t\t");
		System.out.println();
		
		// DESERIALIZATION TIME avr min max
		System.out.print("Deserialization Time" + "\t");
		System.out.print((double)totalDeserializationTime / (double)REPEATING_NUMBER + "\t\t");
		System.out.print(Collections.min(deserializationTimes) + "\t\t");
		System.out.print(Collections.max(deserializationTimes) + "\t\t");
		System.out.print("ms" + "\t\t");
		System.out.println();

		System.out.println("----------------------------------------------------------------------------------");
	}
		


	private static void jsonAssetTest() throws IOException {
		// reseting test variables:
		resetVariables();

		// Starting JSON serialization:
		System.out.println("Starting Json serialization....");
		for (int i = 0; i < REPEATING_NUMBER+3; i++) {	// Do not calculate first 3 progress
			// Set test variables:
			beforeSerialization("json", i);

			// Start serialization:
			jsonAssetSerialization(assetTestArray, outputFile);

			// Get test variables:
			afterSerialization(i);
		}

		// Starting JSON deserialization:
		System.out.println("Starting Json deserialization....");
		for (int i = 0; i < REPEATING_NUMBER+3; i++) {	// Do not calculate first 3 progress
			// Set test variables:
			beforeDeserialization();

			// Start deserialization:
			jsonAssetDeserialization(new File(serializedFileNames.get(i)));

			// Get test variables:
			afterDeserialization(i);
		}
	}

	private static void jsonAssetSerialization(Asset[] testArray, File outputFile) throws IOException {
		// create a json array:
		JSONArray jsonArr = new JSONArray();

		// create a json object:
		JSONObject jsonObj = new JSONObject();

		// save objects into array:
		for (int i = 0; i < test_size; i++) {
			jsonObj = new JSONObject();
			jsonObj.put("id", testArray[i].getId());
			jsonObj.put("rationum", testArray[i].getRationum());
			jsonObj.put("barcode", testArray[i].getBarcode());
			jsonObj.put("itemnum", testArray[i].getItemnum());
			jsonArr.put(jsonObj);
		}

		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile))) {
			bos.write(jsonArr.toString().getBytes());
           }
	}

	private static JSONArray jsonAssetDeserialization(File dataFile) throws IOException {
		String jsonData = new String(Files.readAllBytes(dataFile.toPath()));
		JSONArray jsonArr = new JSONArray(jsonData);

		return jsonArr;
	}
	

	private static void thriftAssetTest() throws IOException, TException {
		// reseting test variables:
		resetVariables();

		// Starting Thrift serialization:
		System.out.println("Starting Thrift serialization....");
		for (int i = 0; i < REPEATING_NUMBER+3; i++) {	// Do not calculate first 3 progress
			// Set test variables:
			beforeSerialization("thrift", i);

			// Start serialization:
			thriftAssetSerialization(assetTestArray, outputFile);

			// Get test variables:
			afterSerialization(i);
		}

		// Starting Thrift deserialization:
		System.out.println("Starting Thrift deserialization....");
		for (int i = 0; i < REPEATING_NUMBER+3; i++) {	// Do not calculate first 3 progress
			// Set test variables:
			beforeDeserialization();

			// Start deserialization:
			thriftAssetDeserialization(new File(serializedFileNames.get(i)));

			// Get test variables:
			afterDeserialization(i);
		}
	}

	
    public static void thriftAssetSerialization(Asset[] testArray, File outputFile) throws TException, IOException {
		// Creating thrift data array:
    	AssetList thriftAssetList = new AssetList();
		int len = testArray.length;
		for (int i = 0; i < len; i++) {
            AssetThrift asset = new AssetThrift();
            asset.setId(testArray[i].getId());  
            asset.setRationum(testArray[i].getRationum());
            asset.setBarcode(testArray[i].getBarcode());
            asset.setItemnum(testArray[i].getItemnum());
            thriftAssetList.addToAssets(asset);		
		}
		
		// Serializing to disk.
		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile));
			     TTransport transport = new TIOStreamTransport(bos)) {
        		TCompactProtocol protocol = new TCompactProtocol(transport);
               thriftAssetList.write(protocol);  
           }
    }

    
    public static AssetList thriftAssetDeserialization(File dataFile) throws TException, IOException {
        AssetList thriftList = new AssetList();
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(dataFile));
        	     TTransport transport = new TIOStreamTransport(bis)){
        	TCompactProtocol protocol = new TCompactProtocol(transport);
            thriftList.read(protocol);
        }
        return thriftList;
    }
	
	
	private static void protobufAssetTest() throws IOException {
		// reseting test variables:
		resetVariables();

		// Starting Protobuf serialization:
		System.out.println("Starting Protobuf serialization....");
		for (int i = 0; i < REPEATING_NUMBER+3; i++) {	// Do not calculate first 3 progress
			// Set test variables:
			beforeSerialization("protobuf", i);

			// Start serialization:
			protobufAssetSerialization(assetTestArray, outputFile);

			// Get test variables:
			afterSerialization(i);
		}

		// Starting Protobuf deserialization:
		System.out.println("Starting Protobuf deserialization....");
		for (int i = 0; i < REPEATING_NUMBER+3; i++) {	// Do not calculate first 3 progress
			// Set test variables:
			beforeDeserialization();

			// Start deserialization:
			protobufAssetDeserialization(new File(serializedFileNames.get(i)));

			// Get test variables:
			afterDeserialization(i);
		}
	}

	private static void protobufAssetSerialization(Asset[] testArray, File outputFile) throws IOException {
		// Protobuf builders:
		AssetProto.Builder proBuilder = AssetProto.newBuilder();
		AssetProtoArray.Builder proArrayBuilder = AssetProtoArray.newBuilder();

		// Creating protobuf data array:
		int len = testArray.length;
		for (int i = 0; i < len; i++) {
			proArrayBuilder.addAsset(proBuilder.setId(testArray[i].getId())
									.setRationum(testArray[i].getRationum())
									.setBarcode(testArray[i].getBarcode())
									.setItemnum(testArray[i].getItemnum())
									.build());
		}

		// Serializing to disk.
		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile))) {
			proArrayBuilder.build().writeTo(bos);  
           }
	}

	private static AssetProtoArray protobufAssetDeserialization(File dataFile) throws IOException {
		AssetProtoArray proArray = null;
		try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(dataFile))) {
			AssetProtoArray.parseFrom(bis);			
		}
		return proArray;
	}

	private static void avroAssetTest() throws IOException {
		// reseting test variables:
		resetVariables();

		// Starting Avro serialization:
		System.out.println("Starting Avro serialization....");
		for (int i = 0; i < REPEATING_NUMBER+3; i++) {	// Do not calculate first 3 progress
			// Set test variables:
			beforeSerialization("avro", i);

			// Start serialization:
			avroAssetSerialization(assetTestArray, outputFile);

			// Get test variables:
			afterSerialization(i);
		}

		// Starting Avro deserialization:
		System.out.println("Starting Avro deserialization....");
		for (int i = 0; i < REPEATING_NUMBER+3; i++) {	// Do not calculate first 3 progress
			// Set test variables:
			beforeDeserialization();

			// Start deserialization:
			avroAssetDeserialization(new File(serializedFileNames.get(i)));

			// Get test variables:
			afterDeserialization(i);
		}
	}

	private static void avroAssetSerialization(Asset[] testArray, File outputFile) throws IOException {		
		DatumWriter<AssetArrayAvro> datumWriter = new SpecificDatumWriter<AssetArrayAvro>(AssetArrayAvro.class);
		try (DataFileWriter<AssetArrayAvro> dataFileWriter = new DataFileWriter<AssetArrayAvro>(datumWriter);) {
			// create AVRO list:
			List<AssetAvro> assetList = new ArrayList<>();	
			for(Asset asset:testArray) {
				assetList.add(AssetAvro.newBuilder()
						.setId(asset.getId())
						.setRationum(asset.getRationum())
						.setBarcode(asset.getBarcode())
						.setItemnum(asset.getItemnum())
						.build());
			}	
			// Create AVRO array:
			AssetArrayAvro assetArrayAvro =  new AssetArrayAvro(assetList);

			// Write AVRO array to file:
			dataFileWriter.create(AssetArrayAvro.getClassSchema(), outputFile);
			dataFileWriter.append(assetArrayAvro);
		}
	}

	private static List<AssetArrayAvro> avroAssetDeserialization(File dataFile) throws IOException {
		DatumReader<AssetArrayAvro> datumReader = new SpecificDatumReader<AssetArrayAvro>(AssetArrayAvro.class);
		List<AssetArrayAvro> avroArray = null ;
        try (DataFileReader<AssetArrayAvro> dataFileReader = new DataFileReader<AssetArrayAvro>(dataFile, datumReader)) {
			while (dataFileReader.hasNext()) {
			    GenericRecord record = dataFileReader.next();
			    avroArray =  (List<AssetArrayAvro>) record.get("assets");
			}
			return avroArray;
		}
	}
	
}
