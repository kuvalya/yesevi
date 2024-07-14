package com.umut.yesevi.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.*;
import org.apache.avro.specific.SpecificDatumReader;
import org.json.JSONArray;
import org.json.JSONObject;

import com.umut.yesevi.avro.EmployeeAvro;
import com.umut.yesevi.protobuf.EmployeeArray;
import com.umut.yesevi.protobuf.EmployeePro;

public class main {
	private final static String OUTPUT_PATH = "output/";
	private static File outputFile;

	// number of objects to be tested:
	private final static int BASE_TEST_SIZE = 1_000;
	private static int test_size;

	// repating number:
	private final static int REPEATING_NUMBER = 1_000;

	// test data that will be used for all serialization methods:
	private static Employee[] employeeTestArray;

	// SIZE test variables
	private static long totalFileSize = 0L;
	private static List<Long> serializationSizes;
	private static String[] serializedFileNames;

	// TIME test variables:
	private static long startTime = 0L;
	private static long finishTime = 0L;
	private static long totalSerializationTime = 0L;
	private static List<Long> serializationTimes;
	private static long totalDeserializationTime = 0L;
	private static List<Long> deserializationTimes;

	public static void main(String[] args) throws IOException {
		Scanner s = new Scanner(System.in);
		System.out.println("Starting tests with string-numbers-set");

		for (int i = 0; i < 3; i++) {
			test_size = (int) (BASE_TEST_SIZE * Math.pow(10, i));

			// Creating test data:
			createEmployeeTestArray();

			// Wait user for CPU-RAM usage logs:
			System.out.println("\nPress Enter key to start test JSON_test_size_" + test_size);
			s.nextLine();
			// *-*-*-*-*-* START TO JSON TEST *-*-*-*-*-*-*-*-
			System.out.println("STARTING TO JSON TEST....");
			jsonEmployeeTest();
			// Print report:
			printReport("STRING-NUMBERS-SET JSON REPORT");
			System.out.println("END OF JSON TEST.");

			// Wait user for CPU-RAM usage logs:
			System.out.println("\nPress Enter key to start test PROTOBUF_test_size_" + test_size);
			s.nextLine();
			// *-*-*-*-*-* START TO PROTOBUF TEST *-*-*-*-*-*-*-*-
			System.out.println("STARTING PROTOBUF TEST....");
			protobufEmployeeTest();
			// Print report:
			printReport("STRING-NUMBERS-SET PROTOBUF REPORT");
			System.out.println("END OF PROTOBUF TEST.");

			// Wait user to CPU-RAM usage logs:
			System.out.println("\nPress Enter key to start test AVRO_test_size_" + test_size);
			s.nextLine();
			// *-*-*-*-*-* START TO AVRO TEST *-*-*-*-*-*-*-*-
			System.out.println("STARTING AVRO TEST....");
			avroEmployeeTest();
			// Print report:
			printReport("STRING-NUMBERS-SET AVRO REPORT");
			System.out.println("END  OF AVRO TEST.");
		}
		System.out.println("End of tests ");
	}

	private static void createEmployeeTestArray() {
		System.out.println("Creating test data....");
		employeeTestArray = new Employee[test_size];

		// Creating Employee data:
		for (int i = 0; i < employeeTestArray.length; i++) {
			employeeTestArray[i] = new Employee((double) 100000000 + i, (float) (0.1 * i), 10 * i, "Title_"+i);
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
		serializedFileNames = new String[REPEATING_NUMBER];

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

		// String path = OUTPUT_PATH + fileSuffix + "/";
		String path = OUTPUT_PATH;

		outputFile = new File(path + fileName);

		// save file names for deserialization:
		serializedFileNames[element] = path + fileName;

		// Set test variables:
		startTime = System.currentTimeMillis();
	}
	

	private static void afterSerialization() {
		// Get test variables:
		finishTime = System.currentTimeMillis();

		// set SIZE:
		Long fileSize = outputFile.length();
		serializationSizes.add(fileSize);
		totalFileSize += fileSize;

		// set TIME:
		Long timeForSerialization = finishTime - startTime;
		serializationTimes.add(timeForSerialization);
		totalSerializationTime += timeForSerialization;
	}
	

	private static void beforeDeserialization() {
		// Set test variables:
		startTime = System.currentTimeMillis();
	}
	

	private static void afterDeserialization() {
		// Get test variables:
		finishTime = System.currentTimeMillis();

		// TIME min, max and total:
		Long timeForDeserialization = finishTime - startTime;
		deserializationTimes.add(timeForDeserialization);
		totalDeserializationTime += timeForDeserialization;
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

	
	private static void jsonEmployeeTest() throws IOException {
		// reseting test variables:
		resetVariables();

		// Starting JSON serialization:
		System.out.println("Starting Json serialization....");
		for (int i = 0; i < REPEATING_NUMBER; i++) {
			// Set test variables:
			beforeSerialization("json", i);

			// Start serialization:
			jsonEmployeeSerialization(employeeTestArray, outputFile);

			// Get test variables:
			afterSerialization();
		}

		// Starting JSON deserialization:
		System.out.println("Starting Json deserialization....");
		for (int i = 0; i < REPEATING_NUMBER; i++) {
			// Set test variables:
			beforeDeserialization();

			// Start deserialization:
			jsonEmployeeDeserialization(new File(serializedFileNames[i]));

			// Get test variables:
			afterDeserialization();
		}
	}

	private static void jsonEmployeeSerialization(Employee[] testArray, File outputFile) throws IOException {
		// create a json array:
		JSONArray jsonArr = new JSONArray();

		// create a json object:
		JSONObject jsonObj = new JSONObject();

		// save objects into array:
		for (int i = 0; i < test_size; i++) {
			jsonObj = new JSONObject();
			jsonObj.put("recid", testArray[i].getRecid());
			jsonObj.put("share", testArray[i].getShare());
			jsonObj.put("jobcode", testArray[i].getJobcode());
			jsonObj.put("title", testArray[i].getTitle());
			jsonArr.put(jsonObj);
		}

		FileOutputStream jsonOutputStream = new FileOutputStream(outputFile);
		jsonOutputStream.write(jsonArr.toString().getBytes());
		jsonOutputStream.close();
	}

	private static JSONArray jsonEmployeeDeserialization(File dataFile) throws IOException {
		String jsonData = new String(Files.readAllBytes(dataFile.toPath()));
		JSONArray jsonArr = new JSONArray(jsonData);

		return jsonArr;
	}

	private static void protobufEmployeeTest() throws IOException {
		// reseting test variables:
		resetVariables();

		// Starting Protobuf serialization:
		System.out.println("Starting Protobuf serialization....");
		for (int i = 0; i < REPEATING_NUMBER; i++) {
			// Set test variables:
			beforeSerialization("protobuf", i);

			// Start serialization:
			protobufEmployeeSerialization(employeeTestArray, outputFile);

			// Get test variables:
			afterSerialization();
		}

		// Starting Protobuf deserialization:
		System.out.println("Starting Protobuf deserialization....");
		for (int i = 0; i < REPEATING_NUMBER; i++) {
			// Set test variables:
			beforeDeserialization();

			// Start deserialization:
			protobufEmployeeDeserialization(new File(serializedFileNames[i]));

			// Get test variables:
			afterDeserialization();
		}
	}

	private static void protobufEmployeeSerialization(Employee[] testArray, File outputFile) throws IOException {
		// Protobuf builders:
		EmployeePro.Builder proBuilder = EmployeePro.newBuilder();
		EmployeeArray.Builder proArrayBuilder = EmployeeArray.newBuilder();

		// Creating protobuf data array:
		int len = testArray.length;
		for (int i = 0; i < len; i++) {
			proArrayBuilder.addEmployee(proBuilder.setRecid(testArray[i].getRecid())
					.setShare(testArray[i].getShare())
					.setJobcode(testArray[i].getJobcode())
					.setTitle(testArray[i].getTitle())
					.build());
		}

		// Serializing to disk.
		FileOutputStream output = new FileOutputStream(outputFile);
		proArrayBuilder.build().writeTo(output);
		output.close();
	}

	private static EmployeeArray protobufEmployeeDeserialization(File dataFile) throws IOException {
		EmployeeArray proArray = EmployeeArray.parseFrom(new FileInputStream(dataFile.getPath()));
		return proArray;
	}

	private static void avroEmployeeTest() throws IOException {
		// reseting test variables:
		resetVariables();

		// create AVRO schema:
		Schema avroSchema = new Schema.Parser().parse(new File("avro/employee.avsc"));

		// Starting Avro serialization:
		System.out.println("Starting Avro serialization....");
		for (int i = 0; i < REPEATING_NUMBER; i++) {
			// Set test variables:
			beforeSerialization("avro", i);

			// Start serialization:
			avroEmployeeSerialization(avroSchema, employeeTestArray, outputFile);

			// Get test variables:
			afterSerialization();
		}

		// Starting Avro deserialization:
		System.out.println("Starting Avro deserialization....");
		for (int i = 0; i < REPEATING_NUMBER; i++) {
			// Set test variables:
			beforeDeserialization();

			// Start deserialization:
			avroEmployeeDeserialization(avroSchema, new File(serializedFileNames[i]));

			// Get test variables:
			afterDeserialization();
		}
	}

	private static void avroEmployeeSerialization(Schema schema, Employee[] testArray, File outputFile) throws IOException {
		DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<GenericRecord>(schema);
		DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<GenericRecord>(datumWriter);
		dataFileWriter.create(schema, outputFile);

		// create AVRO records:
		int arrayLength = testArray.length;
		for (int i = 0; i < arrayLength; i++) {
			GenericRecord avroRecord = new GenericData.Record(schema);
			avroRecord.put("recid", testArray[i].getRecid());
			avroRecord.put("share", testArray[i].getShare());
			avroRecord.put("jobcode", testArray[i].getJobcode());
			avroRecord.put("title", testArray[i].getTitle());
			dataFileWriter.append(avroRecord);
		}
		dataFileWriter.close();
	}

	private static EmployeeAvro[] avroEmployeeDeserialization(Schema schema, File dataFile) throws IOException {
		EmployeeAvro[] avroArray = new EmployeeAvro[test_size];

		DatumReader<EmployeeAvro> datumReader = new SpecificDatumReader<EmployeeAvro>(EmployeeAvro.class);
		DataFileReader<EmployeeAvro> dataFileReader = new DataFileReader<EmployeeAvro>(dataFile, datumReader);

		int i = 0;
		while (dataFileReader.hasNext()) {
			avroArray[i++] = dataFileReader.next();
		}
		dataFileReader.close();

		return avroArray;
	}

}
