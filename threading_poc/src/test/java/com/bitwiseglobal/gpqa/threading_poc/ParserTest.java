package com.bitwiseglobal.gpqa.threading_poc;

public class ParserTest {

	public static void main(String[] args) {
		BDDXMLParser parser = new BDDXMLParser("execution.xml");
		Suite suite = parser.parseXMLFile();
		System.out.println("Suite \n\n"+suite);
		Repository.printRepo();
		suite.execute();
	}
}
