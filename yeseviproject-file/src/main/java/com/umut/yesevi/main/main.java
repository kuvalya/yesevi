package com.umut.yesevi.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import java.util.Date;
import java.text.SimpleDateFormat;

//import org.json.JSONArray;
import org.json.JSONObject;
import com.google.protobuf.ByteString;
import com.umut.yesevi.protobuf.fileMessage;


import org.apache.avro.Schema;
import org.apache.avro.SchemaParser;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;


// import com.umut.yesevi.avro.AssetAvro;


public class main {
	private final static String OUTPUT_PATH = "output/";
	private static File outputFile;

	// number of objects to be tested:
	private final static int BASE_TEST_SIZE = 1_000;
	private static int test_size;

	// Repeating number:
//	private final static int REPEATING_NUMBER = 1_000;
	private final static int REPEATING_NUMBER = 100;

	// test data that will be used for all serialization methods: 
	private static File inputSampleFile;

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
		System.out.println("Starting tests with only-numbers-set");

		for (int i = 0; i < 3; i++) {
			test_size = (int) (BASE_TEST_SIZE * Math.pow(10, i));

			// Creating test data:
			inputSampleFile = new File("sample_input/samplePic_" + test_size + ".jpg"); // 1_000, 10_000, 100_000 kb sample files

			// Wait user for CPU-RAM usage logs:
			System.out.println("\nPress Enter key to start test JSON_test_size_" + test_size);
			s.nextLine();
			// *-*-*-*-*-* START TO JSON TEST *-*-*-*-*-*-*-*-
			System.out.println("STARTING TO JSON TEST....");
			jsonFileTest();
			// Print report:
			printReport("ONLY-NUMBERS-SET JSON REPORT");
			System.out.println("END OF JSON TEST.");
			// Wait user for CPU-RAM usage logs:
			System.out.println("\nPress Enter key to start test PROTOBUF_test_size_" + test_size);
			s.nextLine();
			// *-*-*-*-*-* START TO PROTOBUF TEST *-*-*-*-*-*-*-*-
			System.out.println("STARTING PROTOBUF TEST....");
			protobufFileTest();
			// Print report:
			printReport("ONLY-NUMBERS-SET PROTOBUF REPORT");
			System.out.println("END OF PROTOBUF TEST.");

			// Wait user to CPU-RAM usage logs:
			System.out.println("\nPress Enter key to start test AVRO_test_size_" + test_size);
			s.nextLine();
			// *-*-*-*-*-* START TO AVRO TEST *-*-*-*-*-*-*-*-
			System.out.println("STARTING AVRO TEST....");
			avroFileTest();
			// Print report:
			printReport("ONLY-NUMBERS-SET AVRO REPORT");
			System.out.println("END  OF AVRO TEST.");
		}
		System.out.println("End of tests ");
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

	private static void jsonFileTest() throws IOException {
		// reseting test variables:
		resetVariables();

		// Starting JSON serialization:
		System.out.println("Starting Json serialization....");
		for (int i = 0; i < REPEATING_NUMBER; i++) {
			// Set test variables:
			beforeSerialization("json", i);

			// Start serialization:
			jsonFileSerialization(inputSampleFile, outputFile);

			// Get test variables:
			afterSerialization();
		}

		// Starting JSON deserialization:
		System.out.println("Starting Json deserialization....");
		for (int i = 0; i < REPEATING_NUMBER; i++) {
			// Set test variables:
			beforeDeserialization();

			// Start deserialization:
			jsonFileDeserialization(new File(serializedFileNames[i]));

			// Get test variables:
			afterDeserialization();
		}
	}

	private static void jsonFileSerialization(File inputFile, File outputFile) throws IOException {
		// open input file as byte array:
		byte[] inputFileByteArray = new byte[(int) inputFile.length()];
		try (FileInputStream inputStream = new FileInputStream(inputFile)) {
			inputStream.read(inputFileByteArray);
		} catch (Exception e) {
			System.out.println("exception file read: ");
			e.printStackTrace();
		}

		// create a json object:
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("name", inputFile.getName());
		jsonObj.put("fileBytes", Base64.getEncoder().encodeToString(inputFileByteArray));

		// Serialize to disk:
		try (FileOutputStream jsonOutputStream = new FileOutputStream(outputFile)) {
			jsonOutputStream.write(jsonObj.toString().getBytes());
		} catch (Exception e) {
			System.out.println("exception file write: ");
			e.printStackTrace();
		}
	}

	private static File jsonFileDeserialization(File dataFile) throws IOException {
		String jsonData = new String(Files.readAllBytes(dataFile.toPath()));
		JSONObject outputJsonObj = new JSONObject(jsonData);
		String outputFileName = outputJsonObj.getString("name");
		String outputFileBase64 = outputJsonObj.getString("fileBytes");
		
		// Starting to write output file:
		File outputFile =  new File("sample_output/"+outputFileName);
		byte[] outputFileBytes = Base64.getDecoder().decode(outputFileBase64);
		try (FileOutputStream fos = new FileOutputStream(outputFile)) {
			   fos.write(outputFileBytes);
			}
		
		// compare input and output file size:
		if(outputFileBytes.length != inputSampleFile.length()) System.out.println("BOYUT HATASI !!!!!!!!!!!!!!!!!!!!!!!!!");

		return outputFile;
		
	}
	

	private static void protobufFileTest() throws IOException {
		// reseting test variables:
		resetVariables();

		// Starting Protobuf serialization:
		System.out.println("Starting Protobuf serialization....");
		for (int i = 0; i < REPEATING_NUMBER; i++) {
			// Set test variables:
			beforeSerialization("protobuf", i);

			// Start serialization:
			protobufFileSerialization(inputSampleFile, outputFile);;

			// Get test variables:
			afterSerialization();
		}

		// Starting Protobuf deserialization:
		System.out.println("Starting Protobuf deserialization....");
		for (int i = 0; i < REPEATING_NUMBER; i++) {
			// Set test variables:
			beforeDeserialization();

			// Start deserialization:
			protobufFileDeserialization(new File(serializedFileNames[i]));

			// Get test variables:
			afterDeserialization();
		}
	}

	private static void protobufFileSerialization(File inputFile, File outputFile) throws IOException {
		// open input file as byte array:
		byte[] inputFileByteArray = new byte[(int) inputFile.length()];
		try (FileInputStream inputStream = new FileInputStream(inputFile)) {
			inputStream.read(inputFileByteArray);
		} catch (Exception e) {
			System.out.println("exception protobuf serilize file read: ");
			e.printStackTrace();
		}

		// fill protobuf message:
		fileMessage.Builder fileBuilder = fileMessage.newBuilder();
		fileBuilder.setName(inputFile.getName());
		fileBuilder.setFileBytes(ByteString.copyFrom(inputFileByteArray));
		
		// Serialize to disk.
		try(FileOutputStream output = new FileOutputStream(outputFile) ){
			fileBuilder.build().writeTo(output);
		} catch (Exception e) {
			System.out.println("exception protobuf serilize file write: ");
			e.printStackTrace();
		}

	}

	private static File protobufFileDeserialization(File dataFile) throws IOException {
		fileMessage outputFileMessage =
				fileMessage.parseFrom(new FileInputStream(dataFile));
		String outputFileName = outputFileMessage.getName();
		byte[] outputFileBytes = outputFileMessage.getFileBytes().toByteArray(); 

		File outputFile =  new File("sample_output/"+outputFileName);
		try (FileOutputStream fos = new FileOutputStream(outputFile)) {
			   fos.write(outputFileBytes);
			}

		return outputFile;
	}

	private static void avroFileTest() throws IOException {
		// reseting test variables:
		resetVariables();


		// create AVRO schema:
		//Schema avroSchema = new Schema.Parser().parse(new File("avro/asset.avsc"));
		Schema avroSchema = new SchemaParser().parse(new File("avro/file.avsc")).mainSchema();


		// Starting Avro serialization:
		System.out.println("Starting Avro serialization....");
		for (int i = 0; i < REPEATING_NUMBER; i++) {
			// Set test variables:
			beforeSerialization("avro", i);

			// Start serialization:
			avroFileSerialization(avroSchema, inputSampleFile, outputFile);

			// Get test variables:
			afterSerialization();
		}

		// Starting Avro deserialization:
		System.out.println("Starting Avro deserialization....");
		for (int i = 0; i < REPEATING_NUMBER; i++) {
			// Set test variables:
			beforeDeserialization();

			// Start deserialization:
			avroFileDeserialization(avroSchema, new File(serializedFileNames[i]));

			// Get test variables:
			afterDeserialization();
		}
	}

	private static void avroFileSerialization(Schema schema, File inputFile, File outputFile) throws IOException {
		// open input file as byte array:
		byte[] inputFileByteArray = new byte[(int) inputFile.length()];
		try (FileInputStream inputStream = new FileInputStream(inputFile)) {
			inputStream.read(inputFileByteArray);
		} catch (Exception e) {
			System.out.println("exception avro serialize file read: ");
			e.printStackTrace();
		}

		// set avro input
		GenericRecord inputAvro = new GenericData.Record(schema);
		inputAvro.put("filename", inputFile.getName());
		inputAvro.put("filebytes", ByteBuffer.wrap(inputFileByteArray));

		// create AVRO records:
		DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<GenericRecord>(schema);
		try(DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<GenericRecord>(datumWriter)) {
			dataFileWriter.create(schema, outputFile);
			dataFileWriter.append(inputAvro);
		}
		
	}

	private static File avroFileDeserialization(Schema schema, File dataFile) throws IOException {
		// Deserialize from disk
		DatumReader<GenericRecord> datumReader = new GenericDatumReader<GenericRecord>(schema);
		try (DataFileReader<GenericRecord> dataFileReader = new DataFileReader<GenericRecord>(outputFile,
				datumReader)) {
			GenericRecord outputAvro = null;
			while (dataFileReader.hasNext()) {
				// Reuse avro object by passing it to next(). This saves us from
				// allocating and garbage collecting many objects for files with
				// many items.
				outputAvro = dataFileReader.next(outputAvro);
				try (FileOutputStream fs = new FileOutputStream("sample_output/" + outputAvro.get("filename"))) {
					FileChannel channel = fs.getChannel();
					channel.write((ByteBuffer) outputAvro.get("filebytes"));
					channel.close();
				}
			}
		}
		return outputFile;
	}
}
