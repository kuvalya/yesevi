package com.umut.yesevi.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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

import com.umut.yesevi.avro.PersonAvro;
import com.umut.yesevi.protobuf.PersonArray;
import com.umut.yesevi.protobuf.PersonPro;

public class main {
	private final static String OUTPUT_PATH = "output/";
	private static File outputFile;

	// number of objects to be tested:
	private final static int BASE_TEST_SIZE = 1_000;
	private static int test_size;

	// repating number:
	private final static int REPEATING_NUMBER = 1_000;

	// test data that will be used for all serialization methods:
	private static Person[] personTestArray;

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
		System.out.println("Starting tests with only-string-set....");

		for (int i = 0; i < 3; i++) {
			test_size = (int) (BASE_TEST_SIZE * Math.pow(10, i));

			// Creating test data:
			createTestArray();

			// Wait user for CPU-RAM usage logs:
			System.out.println("\nPress Enter key to start test JSON_test_size_" + test_size);
//			s.nextLine();
			// *-*-*-*-*-* START TO JSON TEST *-*-*-*-*-*-*-*-
			System.out.println("STARTING TO JSON TEST....");
			jsonTest();
			// Print report:
			printReport("ONLY-STRING-SET JSON REPORT");
			System.out.println("END OF JSON TEST.");

			// Wait user for CPU-RAM usage logs:
			System.out.println("\nPress Enter key to start test PROTOBUF_test_size_" + test_size);
//			s.nextLine();
			// *-*-*-*-*-* START TO PROTOBUF TEST *-*-*-*-*-*-*-*-
			System.out.println("STARTING PROTOBUF TEST....");
			protobufTest();
			// Print report:
			printReport("ONLY-STRING-SET PROTOBUF REPORT");
			System.out.println("END OF PROTOBUF TEST.");

			// Wait user to CPU-RAM usage logs:
			System.out.println("\nPress Enter key to start test AVRO_test_size_" + test_size);
//			s.nextLine();
			// *-*-*-*-*-* START TO AVRO TEST *-*-*-*-*-*-*-*-
			System.out.println("STARTING AVRO TEST....");
			avroTest();
			// Print report:
			printReport("ONLY-STRING-SET AVRO REPORT");
			System.out.println("END  OF AVRO TEST.");
		}
		System.out.println("End of tests.");
	}

	private static void createTestArray() {
		System.out.println("Creating test data....");
		personTestArray = new Person[test_size];

		// Creating Person data:
		for (int i = 0; i < personTestArray.length; i++) {
			personTestArray[i] = new Person("Umut_"+i, "AKTEPE_"+i, "ANKARA_"+i, "Yenimahalle_"+i);
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
		System.out.print(avrSize / 1024L + "\t\t");
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

		System.out.println("serializationTimes="+serializationTimes);
		Collections.sort(serializationTimes);
		System.out.println("serializationSort="+serializationTimes);
		System.out.println();
		System.out.println("deserializationTimes="+deserializationTimes);
		Collections.sort(deserializationTimes);
		System.out.println("deserializationSort="+deserializationTimes);

	}

	private static void jsonTest() throws IOException {
		// reseting test variables:
		resetVariables();

		// Starting JSON serialization:
		System.out.println("Starting Json serialization....");
		for (int i = 0; i < REPEATING_NUMBER; i++) {
			// Set test variables:
			beforeSerialization("json", i);

			// Start serialization:
			jsonSerialization(personTestArray, outputFile);

			// Get test variables:
			afterSerialization();
		}

		// Starting JSON deserialization:
		System.out.println("Starting Json deserialization....");
		for (int i = 0; i < REPEATING_NUMBER; i++) {
			// Set test variables:
			beforeDeserialization();

			// Start deserialization:
			jsonDeserialization(new File(serializedFileNames[i]));

			// Get test variables:
			afterDeserialization();
		}
	}

	private static void jsonSerialization(Person[] testArray, File outputFile) throws IOException {
		// create a json array:
		JSONArray jsonArr = new JSONArray();

		// create a json object:
		JSONObject jsonObj = new JSONObject();

		// save objects into array:
		for (int i = 0; i < test_size; i++) {
			jsonObj = new JSONObject();
			jsonObj.put("name", testArray[i].getName());
			jsonObj.put("lastname", testArray[i].getLastname());
			jsonObj.put("city", testArray[i].getCity());
			jsonObj.put("address", testArray[i].getAddress());
			jsonArr.put(jsonObj);
		}

		FileOutputStream jsonOutputStream = new FileOutputStream(outputFile);
		jsonOutputStream.write(jsonArr.toString().getBytes());
		jsonOutputStream.close();
	}

	private static JSONArray jsonDeserialization(File dataFile) throws IOException {
		String jsonData = new String(Files.readAllBytes(dataFile.toPath()));
		JSONArray jsonArr = new JSONArray(jsonData);

		return jsonArr;
	}

	private static void protobufTest() throws IOException {
		// reseting test variables:
		resetVariables();

		// Starting Protobuf serialization:
		System.out.println("Starting Protobuf serialization....");
		for (int i = 0; i < REPEATING_NUMBER; i++) {
			// Set test variables:
			beforeSerialization("protobuf", i);

			// Start serialization:
			protobufSerialization(personTestArray, outputFile);

			// Get test variables:
			afterSerialization();
		}

		// Starting Protobuf deserialization:
		System.out.println("Starting Protobuf deserialization....");
		for (int i = 0; i < REPEATING_NUMBER; i++) {
			// Set test variables:
			beforeDeserialization();

			// Start deserialization:
			protobufDeserialization(new File(serializedFileNames[i]));

			// Get test variables:
			afterDeserialization();
		}
	}

	private static void protobufSerialization(Person[] testArray, File outputFile) throws IOException {
		// Protobuf builders:
		PersonPro.Builder proBuilder = PersonPro.newBuilder();
		PersonArray.Builder proArrayBuilder = PersonArray.newBuilder();

		// Creating protobuf data array:
		int len = testArray.length;
		for (int i = 0; i < len; i++) {
			proArrayBuilder.addPerson(proBuilder.setName(testArray[i].getName())
					.setLastname(testArray[i].getLastname())
					.setCity(testArray[i].getCity())
					.setAddress(testArray[i].getAddress())
					.build());
		}

		// Serializing to disk.
		FileOutputStream output = new FileOutputStream(outputFile);
		proArrayBuilder.build().writeTo(output);
		output.close();
	}

	private static PersonArray protobufDeserialization(File dataFile) throws IOException {
		PersonArray proArray = PersonArray.parseFrom(new FileInputStream(dataFile.getPath()));
		return proArray;
	}

	private static void avroTest() throws IOException {
		// reseting test variables:
		resetVariables();

		// create AVRO schema:
		Schema avroSchema = new Schema.Parser().parse(new File("avro/person.avsc"));

		// Starting Avro serialization:
		System.out.println("Starting Avro serialization....");
		for (int i = 0; i < REPEATING_NUMBER; i++) {
			// Set test variables:
			beforeSerialization("avro", i);

			// Start serialization:
			avroSerialization(avroSchema, personTestArray, outputFile);

			// Get test variables:
			afterSerialization();
		}

		// Starting Avro deserialization:
		System.out.println("Starting Avro deserialization....");
		for (int i = 0; i < REPEATING_NUMBER; i++) {
			// Set test variables:
			beforeDeserialization();

			// Start deserialization:
			avroDeserialization(avroSchema, new File(serializedFileNames[i]));

			// Get test variables:
			afterDeserialization();
		}
	}

	private static void avroSerialization(Schema schema, Person[] testArray, File outputFile) throws IOException {
		DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<GenericRecord>(schema);
		DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<GenericRecord>(datumWriter);
		dataFileWriter.create(schema, outputFile);

		// create AVRO records:
		int arrayLength = testArray.length;
		for (int i = 0; i < arrayLength; i++) {
			GenericRecord avroRecord = new GenericData.Record(schema);
			avroRecord.put("name", testArray[i].getName());
			avroRecord.put("lastname", testArray[i].getLastname());
			avroRecord.put("city", testArray[i].getCity());
			avroRecord.put("address", testArray[i].getAddress());
			dataFileWriter.append(avroRecord);
		}
		dataFileWriter.close();
	}

	private static PersonAvro[] avroDeserialization(Schema schema, File dataFile) throws IOException {
		PersonAvro[] avroArray = new PersonAvro[test_size];

		DatumReader<PersonAvro> datumReader = new SpecificDatumReader<PersonAvro>(PersonAvro.class);
		DataFileReader<PersonAvro> dataFileReader = new DataFileReader<PersonAvro>(dataFile, datumReader);

		int i = 0;
		while (dataFileReader.hasNext()) {
			avroArray[i++] = dataFileReader.next();
		}
		dataFileReader.close();

		return avroArray;
	}

}
