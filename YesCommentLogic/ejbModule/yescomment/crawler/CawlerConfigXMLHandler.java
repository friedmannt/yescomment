package yescomment.crawler;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

public class CawlerConfigXMLHandler {

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "crawlerconfigs")
	public static class CrawlerConfigs {

		public CrawlerConfigs() {
		}

		public CrawlerConfigs(List<CrawlerConfig> crawlerConfigs) {
			this.crawlerConfigs = crawlerConfigs;
		}

		@XmlElement(name = "crawlerconfig", type = CrawlerConfig.class)
		private List<CrawlerConfig> crawlerConfigs = new ArrayList<CrawlerConfig>();

		public List<CrawlerConfig> getCrawlerConfigs() {
			return crawlerConfigs;
		}

		public void setCrawlerConfigs(List<CrawlerConfig> crawlerConfigs) {
			this.crawlerConfigs = crawlerConfigs;
		}

	}

	// Export
	public static void marshal(List<CrawlerConfig> crawlerConfigs,
			File selectedFile) throws IOException, JAXBException {
		JAXBContext context;
		BufferedWriter writer = null;
		writer = new BufferedWriter(new FileWriter(selectedFile));
		context = JAXBContext.newInstance(CrawlerConfigs.class);
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		m.marshal(new CrawlerConfigs(crawlerConfigs), writer);
		writer.close();
	}

	// Import
	public static List<CrawlerConfig> unmarshal(File importFile) throws JAXBException {
		CrawlerConfigs crawlerConfigs = new CrawlerConfigs();

		JAXBContext context = JAXBContext.newInstance(CrawlerConfigs.class);
		Unmarshaller um = context.createUnmarshaller();
		crawlerConfigs = (CrawlerConfigs) um.unmarshal(importFile);

		return crawlerConfigs.getCrawlerConfigs();
	}
	
	
}
