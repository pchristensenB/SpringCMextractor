package extractor;

public class SpringCMExtractor {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		String clientId = "";
		String clientSecret="";
		SpringCMExtractorWorker worker = new SpringCMExtractorWorker(clientId,clientSecret);
		worker.extractBasedOnCSV("./sample_data.csv");
		
	}
}

	
