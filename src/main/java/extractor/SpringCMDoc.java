package extractor;

import java.net.URL;

public class SpringCMDoc {

	private String name;
	private URL url;
	public SpringCMDoc(String name, URL url) {
		super();
		this.name = name;
		this.url = url;
	}
	protected String getName() {
		return name;
	}
	protected void setName(String name) {
		this.name = name;
	}
	protected URL getUrl() {
		return url;
	}
	protected void setUrl(URL url) {
		this.url = url;
	}
	

}
