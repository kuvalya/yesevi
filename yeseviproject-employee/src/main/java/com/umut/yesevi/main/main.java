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

import org.apache.avro.Schema;
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

import com.umut.yesevi.avro.EmployeeArrayAvro;
import com.umut.yesevi.avro.EmployeeAvro;
import com.umut.yesevi.protobuf.EmployeeProtoArray;
import com.umut.yesevi.protobuf.EmployeeProto;
import com.umut.yesevi.thrift.EmployeeList;
import com.umut.yesevi.thrift.EmployeeThrift;

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
		System.out.println("Starting tests with string-numbers-set");

		for (int i = 0; i < 3; i++) {
			test_size = (int) (BASE_TEST_SIZE * Math.pow(10, i));

			// Creating test data:
			createEmployeeTestArray();

			// Wait user for CPU-RAM usage logs:
			System.out.println("\nPress Enter key to start test JSON_test_size_" + test_size);
//			s.nextLine();
			// *-*-*-*-*-* START TO JSON TEST *-*-*-*-*-*-*-*-
			System.out.println("STARTING TO JSON TEST....");
			jsonEmployeeTest();
			// Print report:
			printReport("STRING-NUMBERS-SET JSON REPORT");
			System.out.println("END OF JSON TEST.");

			// Wait user to CPU-RAM usage logs:
			System.out.println("\nPress Enter key to start test THRIFT_test_size_" + test_size);
//			s.nextLine();
			// *-*-*-*-*-* START TO THRIFT TEST *-*-*-*-*-*-*-*-
			System.out.println("STARTING THRIFT TEST....");
			thriftEmployeeTest();
			// Print report:
			printReport("ONLY-NUMBERS-SET THRIFT REPORT");
			System.out.println("END  OF THRIFT TEST.");


			// Wait user for CPU-RAM usage logs:
			System.out.println("\nPress Enter key to start test PROTOBUF_test_size_" + test_size);
//			s.nextLine();
			// *-*-*-*-*-* START TO PROTOBUF TEST *-*-*-*-*-*-*-*-
			System.out.println("STARTING PROTOBUF TEST....");
			protobufEmployeeTest();
			// Print report:
			printReport("STRING-NUMBERS-SET PROTOBUF REPORT");
			System.out.println("END OF PROTOBUF TEST.");

			// Wait user to CPU-RAM usage logs:
			System.out.println("\nPress Enter key to start test AVRO_test_size_" + test_size);
//			s.nextLine();
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
		//serializedFileNames = new String[REPEATING_NUMBER];
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

		// String path = OUTPUT_PATH + fileSuffix + "/";
		String path = OUTPUT_PATH;

		outputFile = new File(path + fileName);

		// save file names for deserialization:
		//serializedFileNames[element] = path + fileName;
		serializedFileNames.add(path + fileName);

		// Set test variables:
		startTime = System.currentTimeMillis();
	}
	

	private static void afterSerialization(int i) {
		// Do not calculate first 3 processes:
		if(i>2) {
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
	}
	

	private static void beforeDeserialization() {
		// Set test variables:
		startTime = System.currentTimeMillis();
	}
	

	private static void afterDeserialization(int i) {
		// Do not calculate first 3 processes:
		if(i>2) {
			// Get test variables:
			finishTime = System.currentTimeMillis();

			// TIME min, max and total:
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

	
	private static void jsonEmployeeTest() throws IOException {
		// reseting test variables:
		resetVariables();

		// Starting JSON serialization:
		System.out.println("Starting Json serialization....");
		for (int i = 0; i < REPEATING_NUMBER+3; i++) {	// Do not calculate first 3 progress
			// Set test variables:
			beforeSerialization("json", i);

			// Start serialization:
			jsonEmployeeSerialization(employeeTestArray, outputFile);

			// Get test variables:
			afterSerialization(i);
		}

		// Starting JSON deserialization:
		System.out.println("Starting Json deserialization....");
		for (int i = 0; i < REPEATING_NUMBER+3; i++) {	// Do not calculate first 3 progress
			// Set test variables:
			beforeDeserialization();

			// Start deserialization:
			jsonEmployeeDeserialization(new File(serializedFileNames.get(i)));

			// Get test variables:
			afterDeserialization(i);
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

		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile))) {
			bos.write(jsonArr.toString().getBytes());
           }
		/*
		FileOutputStream jsonOutputStream = new FileOutputStream(outputFile);
		jsonOutputStream.write(jsonArr.toString().getBytes());
		jsonOutputStream.close();
		*/
	}

	private static JSONArray jsonEmployeeDeserialization(File dataFile) throws IOException {
		String jsonData = new String(Files.readAllBytes(dataFile.toPath()));
		JSONArray jsonArr = new JSONArray(jsonData);

		return jsonArr;
	}
	

	private static void thriftEmployeeTest() throws IOException, TException {
		// reseting test variables:
		resetVariables();

		// Starting Thrift serialization:
		System.out.println("Starting Thrift serialization....");
		for (int i = 0; i < REPEATING_NUMBER+3; i++) {	// Do not calculate first 3 progress
			// Set test variables:
			beforeSerialization("thrift", i);

			// Start serialization:
			thriftEmployeeSerialization(employeeTestArray, outputFile);

			// Get test variables:
			afterSerialization(i);
		}

		// Starting Thrift deserialization:
		System.out.println("Starting Thrift deserialization....");
		for (int i = 0; i < REPEATING_NUMBER+3; i++) {	// Do not calculate first 3 progress
			// Set test variables:
			beforeDeserialization();

			// Start deserialization:
			thriftEmployeeDeserialization(new File(serializedFileNames.get(i)));

			// Get test variables:
			afterDeserialization(i);
		}
	}

	
    public static void thriftEmployeeSerialization(Employee[] testArray, File outputFile) throws TException, IOException {
		// Creating thrift data array:
    	EmployeeList thriftEmployeeList = new EmployeeList();
		int len = testArray.length;
		for (int i = 0; i < len; i++) {
            EmployeeThrift employee = new EmployeeThrift();
            employee.setRecid(testArray[i].getRecid());  
            employee.setShare(testArray[i].getShare());
            employee.setJobcode(testArray[i].getJobcode());
            employee.setTitle(testArray[i].getTitle());
            thriftEmployeeList.addToEmployees(employee);		
		}
		
		// Serializing to disk.
		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile));
			     TTransport transport = new TIOStreamTransport(bos)) {
        		TCompactProtocol protocol = new TCompactProtocol(transport);
        		thriftEmployeeList.write(protocol);  
           }
    }

    
    public static EmployeeList thriftEmployeeDeserialization(File dataFile) throws TException, IOException {
        EmployeeList thriftList = new EmployeeList();
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(dataFile));
        	     TTransport transport = new TIOStreamTransport(bis)){
        	TCompactProtocol protocol = new TCompactProtocol(transport);
            thriftList.read(protocol);
        }
        return thriftList;
    }
	

	private static void protobufEmployeeTest() throws IOException {
		// reseting test variables:
		resetVariables();

		// Starting Protobuf serialization:
		System.out.println("Starting Protobuf serialization....");
		for (int i = 0; i < REPEATING_NUMBER+3; i++) {	// Do not calculate first 3 progress
			// Set test variables:
			beforeSerialization("protobuf", i);

			// Start serialization:
			protobufEmployeeSerialization(employeeTestArray, outputFile);

			// Get test variables:
			afterSerialization(i);
		}

		// Starting Protobuf deserialization:
		System.out.println("Starting Protobuf deserialization....");
		for (int i = 0; i < REPEATING_NUMBER+3; i++) {	// Do not calculate first 3 progress
			// Set test variables:
			beforeDeserialization();

			// Start deserialization:
			protobufEmployeeDeserialization(new File(serializedFileNames.get(i)));

			// Get test variables:
			afterDeserialization(i);
		}
	}

	private static void protobufEmployeeSerialization(Employee[] testArray, File outputFile) throws IOException {
		// Protobuf builders:
		EmployeeProto.Builder proBuilder = EmployeeProto.newBuilder();
		EmployeeProtoArray.Builder proArrayBuilder = EmployeeProtoArray.newBuilder();

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
		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile))) {
			proArrayBuilder.build().writeTo(bos);  
           }
	}

	private static EmployeeProtoArray protobufEmployeeDeserialization(File dataFile) throws IOException {
		EmployeeProtoArray proArray = EmployeeProtoArray.parseFrom(new BufferedInputStream(new FileInputStream(dataFile)));
		return proArray;
	}

	private static void avroEmployeeTest() throws IOException {
		// reseting test variables:
		resetVariables();

		// create AVRO schema:
		Schema avroSchema = new Schema.Parser().parse(new File("avro/employee.avsc"));

		// Starting Avro serialization:
		System.out.println("Starting Avro serialization....");
		for (int i = 0; i < REPEATING_NUMBER+3; i++) {	// Do not calculate first 3 progress
			// Set test variables:
			beforeSerialization("avro", i);

			// Start serialization:
			avroEmployeeSerialization(employeeTestArray, outputFile);

			// Get test variables:
			afterSerialization(i);
		}

		// Starting Avro deserialization:
		System.out.println("Starting Avro deserialization....");
		for (int i = 0; i < REPEATING_NUMBER+3; i++) {	// Do not calculate first 3 progress
			// Set test variables:
			beforeDeserialization();

			// Start deserialization:
			avroEmployeeDeserialization(new File(serializedFileNames.get(i)));

			// Get test variables:
			afterDeserialization(i);
		}
	}

	private static void avroEmployeeSerialization(Employee[] testArray, File outputFile) throws IOException {		
		DatumWriter<EmployeeArrayAvro> datumWriter = new SpecificDatumWriter<EmployeeArrayAvro>(EmployeeArrayAvro.class);
		try (DataFileWriter<EmployeeArrayAvro> dataFileWriter = new DataFileWriter<EmployeeArrayAvro>(datumWriter);) {
			//dataFileWriter.create(schema, outputFile);
			// create AVRO list with assets:
			List<EmployeeAvro> employeeList = new ArrayList<>();	
			for(Employee employee:testArray) {
				employeeList.add(EmployeeAvro.newBuilder()
						.setRecid(employee.getRecid())
						.setShare(employee.getShare())
						.setJobcode(employee.getJobcode())
						.setTitle(employee.getTitle())
						.build());
			}	
			// Create AVRO array:
			EmployeeArrayAvro employeeArrayAvro =  new EmployeeArrayAvro(employeeList);

			// Write AVRO array to file:
			dataFileWriter.create(EmployeeArrayAvro.getClassSchema(), outputFile);
			dataFileWriter.append(employeeArrayAvro);
		}
	}

	private static List<EmployeeArrayAvro> avroEmployeeDeserialization(File dataFile) throws IOException {

		DatumReader<EmployeeArrayAvro> datumReader = new SpecificDatumReader<EmployeeArrayAvro>(EmployeeArrayAvro.class);
		List<EmployeeArrayAvro> employeeArray = null ;
        try (DataFileReader<EmployeeArrayAvro> dataFileReader = new DataFileReader<EmployeeArrayAvro>(dataFile, datumReader)) {
			while (dataFileReader.hasNext()) {
			    GenericRecord record = dataFileReader.next();
			    employeeArray =  (List<EmployeeArrayAvro>) record.get("employees");
			}
			return employeeArray;
		}
	}
	
	
}
