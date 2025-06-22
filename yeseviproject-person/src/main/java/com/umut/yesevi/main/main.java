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
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.transport.TIOStreamTransport;
import org.apache.thrift.transport.TTransport;
import org.json.JSONArray;
import org.json.JSONObject;

import com.umut.yesevi.avro.PersonArrayAvro;
import com.umut.yesevi.avro.PersonAvro;
import com.umut.yesevi.protobuf.PersonProto;
import com.umut.yesevi.protobuf.PersonProtoArray;
import com.umut.yesevi.thrift.PersonList;
import com.umut.yesevi.thrift.PersonThrift;

public class main {
	private final static String OUTPUT_PATH = "output/";
	private static File outputFile;

	// number of objects to be tested:
	private final static int BASE_TEST_SIZE = 1_000;
	private static int test_size;

	// repating number:
	private final static int REPEATING_NUMBER = 1_000;
	private static int SERIALIZATION_COUNTER = 0;

	// test data that will be used for all serialization methods:
	private static Person[] personTestArray;

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
		try (Scanner s = new Scanner(System.in)) {
			System.out.println("Starting tests with only-string-set");

			for (int i = 0; i < 3; i++) {
				test_size = (int) (BASE_TEST_SIZE * Math.pow(10, i));

				// Creating test data:
				createPersonTestArray();

				// Wait user for CPU-RAM usage logs:
				System.out.println("\nPress Enter key to start test JSON_test_size_" + test_size);
				s.nextLine();
				// *-*-*-*-*-* START TO JSON TEST *-*-*-*-*-*-*-*-
				System.out.println("STARTING TO JSON TEST....");
				jsonPersonTest();
				// Print report:
				printReport("ONLY-STRING-SET JSON REPORT");
				System.out.println("END OF JSON TEST.");

				// Wait user to CPU-RAM usage logs:
				System.out.println("\nPress Enter key to start test THRIFT_test_size_" + test_size);
				s.nextLine();
				// *-*-*-*-*-* START TO THRIFT TEST *-*-*-*-*-*-*-*-
				System.out.println("STARTING THRIFT TEST....");
				thriftPersonTest();
				// Print report:
				printReport("ONLY-STRING-SET THRIFT REPORT");
				System.out.println("END OF THRIFT TEST.");

				// Wait user for CPU-RAM usage logs:
				System.out.println("\nPress Enter key to start test PROTOBUF_test_size_" + test_size);
				s.nextLine();
				// *-*-*-*-*-* START TO PROTOBUF TEST *-*-*-*-*-*-*-*-
				System.out.println("STARTING PROTOBUF TEST....");
				protobufPersonTest();
				// Print report:
				printReport("ONLY-STRING-SET PROTOBUF REPORT");
				System.out.println("END OF PROTOBUF TEST.");

				// Wait user to CPU-RAM usage logs:
				System.out.println("\nPress Enter key to start test AVRO_test_size_" + test_size);
				s.nextLine();
				// *-*-*-*-*-* START TO AVRO TEST *-*-*-*-*-*-*-*-
				System.out.println("STARTING AVRO TEST....");
				avroPersonTest();
				// Print report:
				printReport("ONLY-STRING-SET AVRO REPORT");
				System.out.println("END OF AVRO TEST.");

				System.out.println("End of test_size " + test_size + " tests. \n\n ");
			}
		}
		resetVariables();
		System.out.println("End of tests.");
	}

	private static void createPersonTestArray() {
		System.out.println("Creating new test data for " + test_size + " elements...");
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
		serializedFileNames = new ArrayList<String>();

		// Reseting TIME test variables:
		startTime = 0L;
		finishTime = 0L;
		totalSerializationTime = 0L;
		serializationTimes = new ArrayList<Long>();
		totalDeserializationTime = 0L;
		deserializationTimes = new ArrayList<Long>();
		
		// Reseting the counter:
		SERIALIZATION_COUNTER = 0;
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
			
			// increment counter:
			SERIALIZATION_COUNTER++;
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
		System.out.println("repeating number:\t" + SERIALIZATION_COUNTER);

		System.out.println("----------------------------------------------------------------------------------");
		System.out.println("DATA\t\t\t" + "AVG\t\t" + "MIN\t\t" + "MAX\t\t" + "NOTES");

		// SIZE avr min max
		long avrSize = totalFileSize / SERIALIZATION_COUNTER;
		System.out.print("Serialization Size" + "\t");
		System.out.print(avrSize / 1024 + "\t\t");
		System.out.print(Collections.min(serializationSizes) / 1024 + "\t\t");
		System.out.print(Collections.max(serializationSizes) / 1024 + "\t\t");
		System.out.print("kb" + "\t\t");
		System.out.println();

		// SERIALIZATION TIME avr min max
		System.out.print("Serialization Time" + "\t");
		System.out.print((double)totalSerializationTime / (double)SERIALIZATION_COUNTER + "\t\t");
		System.out.print(Collections.min(serializationTimes) + "\t\t");
		System.out.print(Collections.max(serializationTimes) + "\t\t");
		System.out.print("ms" + "\t\t");
		System.out.println();

		// DESERIALIZATION TIME avr min max
		System.out.print("Deserialization Time" + "\t");
		System.out.print((double)totalDeserializationTime / (double)SERIALIZATION_COUNTER + "\t\t");
		System.out.print(Collections.min(deserializationTimes) + "\t\t");
		System.out.print(Collections.max(deserializationTimes) + "\t\t");
		System.out.print("ms" + "\t\t");
		System.out.println();

		System.out.println("----------------------------------------------------------------------------------");
	}

	
	private static void jsonPersonTest() throws IOException {
		// reseting test variables:
		resetVariables();

		// Starting JSON serialization:
		System.out.println("Starting Json serialization....");
		for (int i = 0; i < REPEATING_NUMBER+3; i++) {	// Do not calculate first 3 progress
			// Set test variables:
			beforeSerialization("json", i);

			// Start serialization:
			jsonPersonSerialization(personTestArray, outputFile);

			// Get test variables:
			afterSerialization(i);
		}

		// Starting JSON deserialization:
		System.out.println("Starting Json deserialization....");
		for (int i = 0; i < REPEATING_NUMBER+3; i++) {	// Do not calculate first 3 progress
			// Set test variables:
			beforeDeserialization();

			// Start deserialization:
			jsonPersonDeserialization(new File(serializedFileNames.get(i)));

			// Get test variables:
			afterDeserialization(i);
		}
	}

	private static void jsonPersonSerialization(Person[] testArray, File outputFile) throws IOException {
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

		// Serialize to disk:
		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile))) {
			bos.write(jsonArr.toString().getBytes());
           }
	}

	private static JSONArray jsonPersonDeserialization(File dataFile) throws IOException {
		String jsonData = new String(Files.readAllBytes(dataFile.toPath()));
		JSONArray jsonArr = new JSONArray(jsonData);
		return jsonArr;
	}
	

	private static void thriftPersonTest() throws IOException, TException {
		// reseting test variables:
		resetVariables();

		// Starting Thrift serialization:
		System.out.println("Starting Thrift serialization....");
		for (int i = 0; i < REPEATING_NUMBER+3; i++) {	// Do not calculate first 3 progress
			// Set test variables:
			beforeSerialization("thrift", i);

			// Start serialization:
			thriftPersonSerialization(personTestArray, outputFile);

			// Get test variables:
			afterSerialization(i);
		}

		// Starting Thrift deserialization:
		System.out.println("Starting Thrift deserialization....");
		for (int i = 0; i < REPEATING_NUMBER+3; i++) {	// Do not calculate first 3 progress
			// Set test variables:
			beforeDeserialization();

			// Start deserialization:
			thriftPersonDeserialization(new File(serializedFileNames.get(i)));

			// Get test variables:
			afterDeserialization(i);
		}
	}

    public static void thriftPersonSerialization(Person[] testArray, File outputFile) throws TException, IOException {
		// Creating thrift data array:
    	PersonList thriftPersonList = new PersonList();
		int len = testArray.length;
		for (int i = 0; i < len; i++) {
            PersonThrift person = new PersonThrift();
            person.setName(testArray[i].getName());  
            person.setLastname(testArray[i].getLastname());
            person.setAddress(testArray[i].getAddress());
            person.setCity(testArray[i].getCity());
            thriftPersonList.addToPersons(person);
		}
		
		// Serializing to disk.
		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile));
			     TTransport transport = new TIOStreamTransport(bos)) {
        		TCompactProtocol protocol = new TCompactProtocol(transport);
        		thriftPersonList.write(protocol);  
           }
    }

    public static PersonList thriftPersonDeserialization(File dataFile) throws TException, IOException {
    	PersonList thriftList = new PersonList();
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(dataFile));
        	     TTransport transport = new TIOStreamTransport(bis)){
        	TCompactProtocol protocol = new TCompactProtocol(transport);
            thriftList.read(protocol);
        }
        return thriftList;
    }

		
	private static void protobufPersonTest() throws IOException {
		// reseting test variables:
		resetVariables();

		// Starting Protobuf serialization:
		System.out.println("Starting Protobuf serialization....");
		for (int i = 0; i < REPEATING_NUMBER+3; i++) {	// Do not calculate first 3 progress
			// Set test variables:
			beforeSerialization("protobuf", i);

			// Start serialization:
			protobufPersonSerialization(personTestArray, outputFile);

			// Get test variables:
			afterSerialization(i);
		}

		// Starting Protobuf deserialization:
		System.out.println("Starting Protobuf deserialization....");
		for (int i = 0; i < REPEATING_NUMBER+3; i++) {	// Do not calculate first 3 progress
			// Set test variables:
			beforeDeserialization();

			// Start deserialization:
			protobufPersonDeserialization(new File(serializedFileNames.get(i)));
			
			// Get test variables:
			afterDeserialization(i);
		}
	}

	private static void protobufPersonSerialization(Person[] testArray, File outputFile) throws IOException {
		// Protobuf builders:
		PersonProto.Builder proBuilder = PersonProto.newBuilder();
		PersonProtoArray.Builder proArrayBuilder = PersonProtoArray.newBuilder();

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
		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile))) {
			proArrayBuilder.build().writeTo(bos);  
           }
	}

	private static PersonProtoArray protobufPersonDeserialization(File dataFile) throws IOException {
		PersonProtoArray proArray = null;
		try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(dataFile))) {
			proArray = PersonProtoArray.parseFrom(bis);
		}
		return proArray;
	}

	
	private static void avroPersonTest() throws IOException {
		// reseting test variables:
		resetVariables();

		// Starting Avro serialization:
		System.out.println("Starting Avro serialization....");
		for (int i = 0; i < REPEATING_NUMBER+3; i++) {	// Do not calculate first 3 progress
			// Set test variables:
			beforeSerialization("avro", i);

			// Start serialization:
			avroPersonSerialization(personTestArray, outputFile);

			// Get test variables:
			afterSerialization(i);
		}

		// Starting Avro deserialization:
		System.out.println("Starting Avro deserialization....");
		for (int i = 0; i < REPEATING_NUMBER+3; i++) {	// Do not calculate first 3 progress
			// Set test variables:
			beforeDeserialization();

			// Start deserialization:
			avroPersonDeserialization(new File(serializedFileNames.get(i))); 

			// Get test variables:
			afterDeserialization(i);
		}
	}

	private static void avroPersonSerialization(Person[] testArray, File outputFile) throws IOException {		
		DatumWriter<PersonArrayAvro> datumWriter = new SpecificDatumWriter<PersonArrayAvro>(PersonArrayAvro.class);
		try (DataFileWriter<PersonArrayAvro> dataFileWriter = new DataFileWriter<PersonArrayAvro>(datumWriter);) {
			// create AVRO list:
			List<PersonAvro> personList = new ArrayList<>();	
			for(Person person:testArray) {
				personList.add(PersonAvro.newBuilder()
						.setName(person.getName())
						.setLastname(person.getLastname())
						.setAddress(person.getAddress())
						.setCity(person.getCity())
						.build());
			}	
			// Create AVRO array:
			PersonArrayAvro personArrayAvro =  new PersonArrayAvro(personList);

			// Write AVRO array to file:
			dataFileWriter.create(PersonArrayAvro.getClassSchema(), outputFile);
			dataFileWriter.append(personArrayAvro);
		}
	}
	
	private static PersonArrayAvro avroPersonDeserialization(File dataFile) throws IOException {
		PersonArrayAvro avroArray = null;
		DatumReader<PersonArrayAvro> datumReader = new SpecificDatumReader<PersonArrayAvro>(PersonArrayAvro.class);
        try (DataFileReader<PersonArrayAvro> dataFileReader = new DataFileReader<PersonArrayAvro>(dataFile, datumReader)) {
			if (dataFileReader.hasNext()) {
				avroArray = dataFileReader.next(avroArray);
			}
		}
		return avroArray;
	}
	
}
