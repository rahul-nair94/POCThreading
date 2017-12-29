package com.bitwiseglobal.gpqa.threading_poc;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BDDXMLParser {
	String xmlFilePath;

	public BDDXMLParser(String xmlFilePath) {
		this.xmlFilePath = xmlFilePath;
	}

	public Suite parseXMLFile() {

		Suite suite = null;
		try {

			File fXmlFile = new File(xmlFilePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();

			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

			NodeList nList = doc.getElementsByTagName("suiteSettings");

			System.out.println("----------------------------");

			// single instance of suiteSettings
			Node nNode = nList.item(0);
			Element suiteSettingsElement = null;
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				suiteSettingsElement = (Element) nNode;
			} else {
				System.err.println("Suite settings not properly defined!!\nSystem now Exits!!");
				System.exit(1);
			}

			suite = new Suite(suiteSettingsElement.getElementsByTagName("reportingPath").item(0).getTextContent(),
					suiteSettingsElement.getElementsByTagName("executionEnv").item(0).getTextContent(),
					suiteSettingsElement.getElementsByTagName("testDataFilePath").item(0).getTextContent(),
					suiteSettingsElement.getElementsByTagName("sauceLabsDeviceConfigFile").item(0).getTextContent(),
					suiteSettingsElement.getElementsByTagName("suiteDegreeOfParallelism").item(0).getTextContent());

			Element configurationsTag = (Element) doc.getElementsByTagName("configurations").item(0);
			NodeList configurationNodeList = configurationsTag.getElementsByTagName("configuration");
			for (int i = 0; i < configurationNodeList.getLength(); i++) {
				suite.addConfiguration(getConfiguration(configurationNodeList.item(i)));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return suite;
	}

	private Configuration getConfiguration(Node item) {
		Element configElement = (Element) item;
		Configuration config = null;
		Integer configId = Integer.parseInt(configElement.getElementsByTagName("configID").item(0).getTextContent());
		Integer configDegreeOfParallelism = Integer
				.parseInt(configElement.getElementsByTagName("degreeOfParallelism").item(0).getTextContent());
		List<Integer> dependencies = new ArrayList<Integer>();
		String dependenciesString = configElement.getElementsByTagName("configDependency").item(0).getTextContent();
		if (dependenciesString != null && !dependenciesString.equals(""))
			dependencies = Arrays.asList((dependenciesString).split(",")).stream().map(e -> Integer.parseInt(e.trim()))
					.collect(Collectors.toList());
		else
			dependencies = null;
		boolean parallelSwitch = configElement.getElementsByTagName("configID").item(0).getTextContent().trim()
				.equalsIgnoreCase("true") ? true : false;
		config = new Configuration(configId, configDegreeOfParallelism, dependencies, parallelSwitch);
		List<ConfigJob> jobs = getConfigJobs(configElement);
		config.setJobs(jobs);
		System.out.println("returning config " + config);
		return config;
	}

	private List<ConfigJob> getConfigJobs(Element configElement) {
		List<ConfigJob> jobs = new ArrayList<ConfigJob>();
		try {
			String browsersConfig = configElement.getElementsByTagName("browsers").item(0).getTextContent();
			if (browsersConfig == null || browsersConfig.trim().equals("")) {
				System.err.println("\nBrowser configuration not found!!\n Setting default browser "
						// QA1 and chrome hardcoded
						+ "QA1" + "!!\n\n");
				browsersConfig = "chrome";
			}
			List<String> browsers = Arrays.asList(browsersConfig.split(","));// GlobalConstants.COMMA_SEPARATOR));
			String plugin = "";// GlobalConstants.EXTENT_PLUGIN_CLASS;

			String gluePath = configElement.getElementsByTagName("glue").item(0).getTextContent();
			// properties.getProperty(GlobalConstants.Config_Keys.GLUE_PATH,
			// null);
			String featuresPath = configElement.getElementsByTagName("glue").item(0).getTextContent();
			// properties.getProperty(GlobalConstants.Config_Keys.FEATURES_PATH,
			// null);
			String tags = configElement.getElementsByTagName("glue").item(0).getTextContent();
			// properties.getProperty(GlobalConstants.Config_Keys.TAGS_FOR_EXECUTION,
			// null);
			String sauceLabsURL = configElement.getElementsByTagName("glue").item(0).getTextContent();
			// properties.getProperty(GlobalConstants.Config_Keys.SAUCELABS_URL_KEY,
			// "");
			String screenResolution = configElement.getElementsByTagName("glue").item(0).getTextContent();
			// properties.getProperty(GlobalConstants.Config_Keys.SPECIFIC_SCREEN_RESOLUTION_KEY);
			String autoCreateDestroy = configElement.getElementsByTagName("glue").item(0).getTextContent();
			// properties.getProperty(GlobalConstants.Config_Keys.AUTO_CREATE_DESTROY_DRIVER);
			System.out.println("Browsers found " + browsers);
			for (String browser : browsers) {
				// if (DriverFactory.isBrowserName(browser)) {
				// RunConfiguration config = new RunConfiguration(browser,
				// plugin, gluePath, featuresPath, tags,
				// sauceLabsURL, screenResolution, autoCreateDestroy);
				// returnList.add(config);
				// }
				ConfigJob job = new ConfigJob(browser);
				jobs.add(job);

			}
			System.out.println("Browsers " + browsers);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Number Of jobs = " + jobs.size());
		return jobs;
	}

}
