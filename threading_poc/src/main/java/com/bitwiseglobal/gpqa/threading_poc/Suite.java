package com.bitwiseglobal.gpqa.threading_poc;

import java.util.ArrayList;
import java.util.List;

public class Suite {
	String reportingPath;
	String executionEnv;
	String testDataFilePath;
	String sauceLabsDeviceConfigFile;
	String suiteDegreeOfParallelism;
	List<Configuration> configurations = new ArrayList<Configuration>();
	List<Thread> threads = new ArrayList<Thread>();

	Integer numberOfConfigurations = 0;

	public Suite(String reportingPath, String executionEnv, String testDataFilePath, String sauceLabsDeviceConfigFile,
			String suiteDegreeOfParallelism) {
		super();
		this.reportingPath = reportingPath;
		this.executionEnv = executionEnv;
		this.testDataFilePath = testDataFilePath;
		this.sauceLabsDeviceConfigFile = sauceLabsDeviceConfigFile;
		this.suiteDegreeOfParallelism = suiteDegreeOfParallelism;
	}

	public void execute() {
		if (!isCyclicDependent(configurations)) {
			for (Configuration con : configurations) {
				Thread thread = new Thread(con);
				thread.setName(con.getConfigId().toString());
				Repository.addThread(thread, con);
			}
			Repository.startAllThreads();
			System.out.println("\n\n\n");
			Repository.printRepo();
		} else {
			System.err.println(
					"Suite is configured with cyclic dependencies! \nPlease resolve dependencies in the provided XML file and rerun the suite!!");
		}
	}

	private boolean isCyclicDependent(List<Configuration> configurations2) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean listSubsetOfAnotherList(List<Integer> mainList, List<Integer> subList) {
		boolean status = true;
		for (Integer integer : subList) {
			if (!mainList.contains(integer))
				status = false;
		}
		return status;
	}

	public String getReportingPath() {
		return reportingPath;
	}

	public void setReportingPath(String reportingPath) {
		this.reportingPath = reportingPath;
	}

	public String getExecutionEnv() {
		return executionEnv;
	}

	public void setExecutionEnv(String executionEnv) {
		this.executionEnv = executionEnv;
	}

	public String getTestDataFilePath() {
		return testDataFilePath;
	}

	public void setTestDataFilePath(String testDataFilePath) {
		this.testDataFilePath = testDataFilePath;
	}

	public String getSauceLabsDeviceConfigFile() {
		return sauceLabsDeviceConfigFile;
	}

	public void setSauceLabsDeviceConfigFile(String sauceLabsDeviceConfigFile) {
		this.sauceLabsDeviceConfigFile = sauceLabsDeviceConfigFile;
	}

	public String getSuiteDegreeOfParallelism() {
		return suiteDegreeOfParallelism;
	}

	public void setSuiteDegreeOfParallelism(String suiteDegreeOfParallelism) {
		this.suiteDegreeOfParallelism = suiteDegreeOfParallelism;
	}

	public List<Configuration> getConfigurations() {
		return configurations;
	}

	public void addConfiguration(Configuration configuration) {
		this.configurations.add(configuration);
		Repository.addDependency(configuration.getConfigId(), configuration.getDependencies());
		numberOfConfigurations++;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Suite [reportingPath=" + reportingPath + ", executionEnv=" + executionEnv + ", testDataFilePath="
				+ testDataFilePath + ", sauceLabsDeviceConfigFile=" + sauceLabsDeviceConfigFile
				+ ", suiteDegreeOfParallelism=" + suiteDegreeOfParallelism + ", numberOfConfigurations="
				+ numberOfConfigurations + "]");
		for (Configuration conf : configurations) {
			sb.append("\n").append("Configuration : ").append(conf);
		}
		return sb.toString();
	}

}
