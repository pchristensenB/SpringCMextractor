package extractor;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.StopWatch;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

public class SpringCMExtractorWorker {
	private String token = null;
	private String apiURL = null;
	private String rootPath = "/Users/pchristensen/springcmdata";
	private long tokenTime=0l;
	private long tokenDuration=3600;
	private String clientId=null;
	private String clientSecret=null;
	private Set<File> cache = new HashSet<File>();
	
	public SpringCMExtractorWorker(String clientId, String clientSecret) {
		this.clientId=clientId;
		this.clientSecret=clientSecret;
		try {
			initToken(clientId, clientSecret);
		} catch (Exception e) {
			e.printStackTrace();
			//throw new RuntimeException("Not working with these");
		}
	}

	public void extractBasedOnCSV(String csvFile) throws Exception {
	
		for(CSVRecord record:CSVReader.csvIterator(csvFile)) {
			System.out.println(record.get(CSVReader.LOCATION_PATH));
			
			downloadDocumentToFolder(record);
		}
	}

	private void downloadDocumentToFolder(CSVRecord record) throws Exception{
		StopWatch st = new StopWatch();
		st.start();
		File path = new File(rootPath + record.get(CSVReader.LOCATION_PATH));
		if(!cache.contains(path)) {
			FileUtils.forceMkdir(path);
			cache.add(path);
			st.stop();
			System.out.println("Created " + path.toString() + " in " + st.toString());
		}
		else {
			st.stop();
			System.out.println("Read " + path.toString() + " in " + st.toString());

		}
		st.reset();
		st.start();

		SpringCMDoc doc = getDocUrl(record.get(CSVReader.DOCUMENT_ID));
		//SpringCMDoc doc = new SpringCMDoc("Platform datasheet" + UUID.randomUUID().toString() +".pdf",new URL("https://peter-demo.box.com/shared/static/mzfjfezblmfb368v4wexj90pw63029o5.pdf"));
		FileUtils.copyInputStreamToFile(downloadDocURL(doc.getUrl().toString()),new File(path+"/" + doc.getName()));
		System.out.println("Copied file from URL " + path.toString() + " in " + st.toString());
		st.stop();
	}

	private InputStream downloadDocURL(String url) throws Exception{
		HttpResponse<InputStream> resp = Unirest.get(url)
				.header("Authorization","Bearer " + getToken())
				.asBinary();
		return resp.getBody();
		
	}
	private SpringCMDoc getDocUrl(String id) throws Exception{
		HttpResponse<JsonNode> resp = Unirest.get(getApiURL() + "/v201411/documents/" + id)
				.header("Authorization","bearer " + getToken())
				.asJson();
		if(resp.getStatus()==200) {
			return new SpringCMDoc(resp.getBody().getObject().get("Name").toString(),new URL(resp.getBody().getObject().getString("DownloadDocumentHref")));
		}
		else {
			System.out.println(resp.getBody().toString());
			System.out.println("Problem getting document");
			return null;
		}
	}

	private void initToken(String clientId,String clientSecret) throws Exception{
		Map<String,String> map = new HashMap<String, String>();
		map.put("client_id",clientId);
		map.put("client_secret",clientSecret);
		HttpResponse<JsonNode> tokenResponse = Unirest.post("https://authuat.springcm.com/api/v201606/apiuser")
				.header("Content-Type","application/json")
				.body(new Gson().toJson(map))
				.asJson();
		System.out.println(tokenResponse.getBody().toString());
		this.token = tokenResponse.getBody().getObject().getString("access_token");
		this.apiURL = tokenResponse.getBody().getObject().getString("api_base_url");
		this.tokenDuration = tokenResponse.getBody().getObject().getLong("expires_in")*1000;
		this.tokenTime=System.currentTimeMillis();
	}



	protected String getToken() throws Exception{
		if(System.currentTimeMillis()-tokenTime>=tokenDuration) {
			initToken(clientId, clientSecret);
		}
		return token;
	}

	protected void setToken(String token) {
		this.token = token;
	}

	protected String getApiURL() {
		return apiURL;
	}
	protected void setApiURL(String apiURL) {
		this.apiURL = apiURL;
	}
	
	
}

