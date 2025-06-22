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
import java.util.Base64;
import java.nio.ByteBuffer;

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
import org.json.JSONObject;
import com.google.protobuf.ByteString;
import com.umut.yesevi.avro.FileAvro;
import com.umut.yesevi.protobuf.fileMessage;
import com.umut.yesevi.thrift.FileThrift;

public class main {
	private final static String OUTPUT_PATH = "output/";
	private static File outputFile;

	// number of objects to be tested:
	private final static int BASE_TEST_SIZE = 100;
	private static int test_size;

	// Repeating number:
	private final static int REPEATING_NUMBER = 1_000;
	private static int SERIALIZATION_COUNTER = 0;

	// test data that will be used for all serialization methods: 
	private static File inputSampleFile;
	private final static String SAMPLE_OUTPUT_PATH = "sample_output/";

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
			System.out.println("Starting tests with file-set");

			for (int i = 0; i < 3; i++) {
				test_size = (int) (BASE_TEST_SIZE * Math.pow(10, i));

				// Creating test data:
				createInputSampleFile();

				// Wait user for CPU-RAM usage logs:
				System.out.println("\nPress Enter key to start test JSON_test_size_kb_" + test_size);
				s.nextLine();
				// *-*-*-*-*-* START TO JSON TEST *-*-*-*-*-*-*-*-
				System.out.println("STARTING TO JSON TEST....");
				jsonFileTest();
				// Print report:
				printReport("FILE-SET JSON REPORT");
				System.out.println("END OF JSON TEST.");

				// Wait user to CPU-RAM usage logs:
				System.out.println("\nPress Enter key to start test THRIFT_test_size_kb_" + test_size);
				s.nextLine();
				// *-*-*-*-*-* START TO THRIFT TEST *-*-*-*-*-*-*-*-
				System.out.println("STARTING THRIFT TEST....");
				thriftFileTest();
				// Print report:
				printReport("FILE-SET THRIFT REPORT");
				System.out.println("END OF THRIFT TEST.");

				// Wait user for CPU-RAM usage logs:
				System.out.println("\nPress Enter key to start test PROTOBUF_test_size_kb_" + test_size);
				s.nextLine();
				// *-*-*-*-*-* START TO PROTOBUF TEST *-*-*-*-*-*-*-*-
				System.out.println("STARTING PROTOBUF TEST....");
				protobufFileTest();
				// Print report:
				printReport("FILE-SET PROTOBUF REPORT");
				System.out.println("END OF PROTOBUF TEST.");

				// Wait user to CPU-RAM usage logs:
				System.out.println("\nPress Enter key to start test AVRO_test_size_kb_" + test_size);
				s.nextLine();
				// *-*-*-*-*-* START TO AVRO TEST *-*-*-*-*-*-*-*-
				System.out.println("STARTING AVRO TEST....");
				avroFileTest();
				// Print report:
				printReport("FILE-SET AVRO REPORT");
				System.out.println("END OF AVRO TEST.");

				System.out.println("End of test_size " + test_size + " tests. \n\n ");
			}
		}
		resetVariables();
		System.out.println("End of tests.");
	}

	private static void createInputSampleFile () {
		System.out.println("Creating new test data for " + test_size + " kb sample file...");
		// Creating test data:
		inputSampleFile = new File("sample_input/samplePic_" + test_size + ".jpg"); // 100, 1_000, 10_000 kb sample files
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


	private static void jsonFileTest() throws IOException {
		// reseting test variables:
		resetVariables();

		// Starting JSON serialization:
		System.out.println("Starting Json serialization....");
		for (int i = 0; i < REPEATING_NUMBER+3; i++) {	// Do not calculate first 3 progress
			// Set test variables:
			beforeSerialization("json", i);

			// Start serialization:
			jsonFileSerialization(inputSampleFile, outputFile);

			// Get test variables:
			afterSerialization(i);
		}

		// Starting JSON deserialization:
		System.out.println("Starting Json deserialization....");
		for (int i = 0; i < REPEATING_NUMBER+3; i++) {	// Do not calculate first 3 progress
			// Set test variables:
			beforeDeserialization();

			// Start deserialization:
			jsonFileDeserialization(new File(serializedFileNames.get(i)));

			// Get test variables:
			afterDeserialization(i);
		}
	}

	private static void jsonFileSerialization(File inputFile, File outputFile) throws IOException {
		// open input file as byte array:
		byte[] inputFileByteArray = new byte[(int) inputFile.length()];
		try (FileInputStream inputStream = new FileInputStream(inputFile)) {
			inputStream.read(inputFileByteArray);
		}

		// create a json object:
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("name", inputFile.getName());
		jsonObj.put("fileBytes", Base64.getEncoder().encodeToString(inputFileByteArray));

		// Serialize to disk:
		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile))) {
			bos.write(jsonObj.toString().getBytes());
           }
	}

	private static File jsonFileDeserialization(File dataFile) throws IOException {
		String jsonData = new String(Files.readAllBytes(dataFile.toPath()));
		JSONObject outputJsonObj = new JSONObject(jsonData);
		String outputFileName = outputJsonObj.getString("name");
		String outputFileBase64 = outputJsonObj.getString("fileBytes");
		
		// Starting to write output file:
		File outputFile =  new File(SAMPLE_OUTPUT_PATH+outputFileName);
		byte[] outputFileBytes = Base64.getDecoder().decode(outputFileBase64);
		try (FileOutputStream fos = new FileOutputStream(outputFile)) {
			   fos.write(outputFileBytes);
			}
		return outputFile;
	}
	

	private static void thriftFileTest() throws IOException, TException {
		// reseting test variables:
		resetVariables();

		// Starting Thrift serialization:
		System.out.println("Starting Thrift serialization....");
		for (int i = 0; i < REPEATING_NUMBER+3; i++) {	// Do not calculate first 3 progress
			// Set test variables:
			beforeSerialization("thrift", i);

			// Start serialization:
			thriftFileSerialization(inputSampleFile, outputFile);

			// Get test variables:
			afterSerialization(i);
		}

		// Starting Thrift deserialization:
		System.out.println("Starting Thrift deserialization....");
		for (int i = 0; i < REPEATING_NUMBER+3; i++) {	// Do not calculate first 3 progress
			// Set test variables:
			beforeDeserialization();

			// Start deserialization:
			thriftFileDeserialization(new File(serializedFileNames.get(i)));

			// Get test variables:
			afterDeserialization(i);
		}
	}

    public static void thriftFileSerialization(File inputFile, File outputFile) throws IOException, TException {
		// Creating thrift data:
        FileThrift fileData = new FileThrift();
        fileData.setName(inputFile.getName());
        fileData.setFileBytes(Files.readAllBytes(inputFile.toPath()));
    	
		// Serializing to disk.
		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile));
			     TTransport transport = new TIOStreamTransport(bos)) {
        		TCompactProtocol protocol = new TCompactProtocol(transport);
        		fileData.write(protocol);  
           }
    }

    public static File thriftFileDeserialization(File dataFile) throws TException, IOException {
        File sampleOutputFile = null;
    	FileThrift thriftData = new FileThrift();
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(dataFile));
       	     TTransport transport = new TIOStreamTransport(bis)){
       	TCompactProtocol protocol = new TCompactProtocol(transport);
       	thriftData.read(protocol);
       }
        
        sampleOutputFile = new File(SAMPLE_OUTPUT_PATH+thriftData.getName());
        try (FileOutputStream fos = new FileOutputStream(sampleOutputFile) ) {
            fos.write(thriftData.getFileBytes());
        }
        
        return sampleOutputFile;
    }


	private static void protobufFileTest() throws IOException {
		// reseting test variables:
		resetVariables();

		// Starting Protobuf serialization:
		System.out.println("Starting Protobuf serialization....");
		for (int i = 0; i < REPEATING_NUMBER+3; i++) {	// Do not calculate first 3 progress
			// Set test variables:
			beforeSerialization("protobuf", i);

			// Start serialization:
			protobufFileSerialization(inputSampleFile, outputFile);

			// Get test variables:
			afterSerialization(i);
		}

		// Starting Protobuf deserialization:
		System.out.println("Starting Protobuf deserialization....");
		for (int i = 0; i < REPEATING_NUMBER+3; i++) {	// Do not calculate first 3 progress
			// Set test variables:
			beforeDeserialization();

			// Start deserialization:
			protobufFileDeserialization(new File(serializedFileNames.get(i)));

			// Get test variables:
			afterDeserialization(i);
		}
	}

	private static void protobufFileSerialization(File inputFile, File outputFile) throws IOException {
		// open input file as byte array:
		byte[] inputFileByteArray = new byte[(int) inputFile.length()];
		try (FileInputStream inputStream = new FileInputStream(inputFile)) {
			inputStream.read(inputFileByteArray);
		}

		// fill protobuf message:
		fileMessage.Builder fileBuilder = fileMessage.newBuilder();
		fileBuilder.setName(inputFile.getName());
		fileBuilder.setFileBytes(ByteString.copyFrom(inputFileByteArray));

		// Serializing to disk.
		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile))) {
			fileBuilder.build().writeTo(bos);  
           }
	}
	
	private static File protobufFileDeserialization(File dataFile) throws IOException {
		File sampleOutputFile =  null;
		fileMessage outputFileMessage = null;
		try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(dataFile))) {
			outputFileMessage = fileMessage.parseFrom(bis);
		} 
		sampleOutputFile =  new File(SAMPLE_OUTPUT_PATH+outputFileMessage.getName());
		try (FileOutputStream fos = new FileOutputStream(sampleOutputFile)) {
			   fos.write(outputFileMessage.getFileBytes().toByteArray());
			}
		return sampleOutputFile;
	}

	
	private static void avroFileTest() throws IOException {
		// reseting test variables:
		resetVariables();
		
		// Starting Avro serialization:
		System.out.println("Starting Avro serialization....");
		for (int i = 0; i < REPEATING_NUMBER+3; i++) {	// Do not calculate first 3 progress
			// Set test variables:
			beforeSerialization("avro", i);

			// Start serialization:
			avroFileSerialization(inputSampleFile, outputFile);

			// Get test variables:
			afterSerialization(i);
		}

		// Starting Avro deserialization:
		System.out.println("Starting Avro deserialization....");
		for (int i = 0; i < REPEATING_NUMBER+3; i++) {	// Do not calculate first 3 progress
			// Set test variables:
			beforeDeserialization();

			// Start deserialization:
			avroFileDeserialization(new File(serializedFileNames.get(i)));

			// Get test variables:
			afterDeserialization(i);
		}
	}

	private static void avroFileSerialization(File inputFile, File outputFile) throws IOException {
		// open input file as byte array:
		byte[] inputFileByteArray = new byte[(int) inputFile.length()];
		try (FileInputStream inputStream = new FileInputStream(inputFile)) {
			inputStream.read(inputFileByteArray);
		}
		
		// Create avro data:
		DatumWriter<FileAvro> datumWriter = new SpecificDatumWriter<FileAvro>(FileAvro.class);
		try (DataFileWriter<FileAvro> dataFileWriter = new DataFileWriter<FileAvro>(datumWriter);) {
			FileAvro fileAvro = FileAvro.newBuilder()
					.setFilename(inputFile.getName())
					.setFilebytes(ByteBuffer.wrap(inputFileByteArray))
					.build();
			
		// Write AVRO array to file:
		dataFileWriter.create(FileAvro.getClassSchema(), outputFile);
		dataFileWriter.append(fileAvro);
		}
	}

	private static File avroFileDeserialization(File dataFile) throws IOException {
		File sampleOutputFile = null;
		DatumReader<FileAvro> datumReader = new SpecificDatumReader<FileAvro>(FileAvro.class);
		FileAvro outputAvro = null;
		try (DataFileReader<FileAvro> dataFileReader = new DataFileReader<FileAvro>(dataFile, datumReader)) {
		    if (dataFileReader.hasNext()) {
		    	outputAvro = dataFileReader.next(outputAvro);
		    	sampleOutputFile =  new File(SAMPLE_OUTPUT_PATH+outputAvro.getFilename());
				try (FileOutputStream fos = new FileOutputStream(sampleOutputFile)) {
					   fos.write(outputAvro.getFilebytes().array());
					}
		    }
		}
		return sampleOutputFile;
	}
	
}
