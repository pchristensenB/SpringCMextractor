package extractor;

import java.io.FileReader;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class CSVReader {

	public static final String LOCATION_PATH = "LOCATION PATH";
	public static final String LOCATION_ID = "LOCATION ID";
	public static final String LOCATION = "LOCATION";
	public static final String CREATED_DATE = "CREATED DATE";
	public static final String FILE_SIZE = "FILE SIZE";
	public static final String DOCUMENT_IS_IN_TRASH = "DOCUMENT IS IN TRASH";
	public static final String DESCRIPTION = "DESCRIPTION";
	public static final String DOCUMENT_ID = "DOCUMENT ID";
	public static final String DOCUMENT_NAME = "DOCUMENT NAME";

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static Iterable<CSVRecord> csvIterator(String filePath) throws Exception{
		String[] HEADERS = {DOCUMENT_NAME,DOCUMENT_ID,DESCRIPTION,DOCUMENT_IS_IN_TRASH,FILE_SIZE,CREATED_DATE,LOCATION,LOCATION_ID,LOCATION_PATH};
	    Reader in = new FileReader(filePath);
	    Iterable<CSVRecord> records = CSVFormat.DEFAULT
	      .withHeader(HEADERS)
	      .withFirstRecordAsHeader()
	      .parse(in);
	   return records;
	
	}
}
