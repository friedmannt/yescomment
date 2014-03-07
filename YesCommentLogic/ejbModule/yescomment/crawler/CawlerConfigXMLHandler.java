package yescomment.crawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * Class for serializing and deserializing the crawler configurations to/from
 * XML
 * 
 * @author Friedmann Tamás
 * 
 */
public class CawlerConfigXMLHandler {

	

	/**
	 * Export
	 * 
	 * @param crawlerConfigs
	 * @param selectedFile
	 * @throws IOException
	 * @throws JAXBException
	 */
	public static void marshal(@NotNull List<CrawlerConfig> crawlerConfigs, @NotNull File selectedFile) throws IOException, JAXBException {
		JAXBContext context;
		BufferedWriter writer = null;
		writer = new BufferedWriter(new FileWriter(selectedFile));
		context = JAXBContext.newInstance(CrawlerConfigs.class);
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		m.marshal(new CrawlerConfigs(crawlerConfigs), writer);
		writer.close();
	}

	/**
	 * Import
	 * 
	 * @param importFile
	 * @return
	 * @throws JAXBException
	 */
	public static List<CrawlerConfig> unmarshal(@NotNull File importFile) throws JAXBException {
		CrawlerConfigs crawlerConfigs = new CrawlerConfigs();

		JAXBContext context = JAXBContext.newInstance(CrawlerConfigs.class);
		Unmarshaller um = context.createUnmarshaller();
		crawlerConfigs = (CrawlerConfigs) um.unmarshal(importFile);

		return crawlerConfigs.getCrawlerConfigs();
	}

}
