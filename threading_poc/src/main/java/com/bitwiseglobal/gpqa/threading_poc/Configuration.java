package com.bitwiseglobal.gpqa.threading_poc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class Configuration implements Runnable {
	Integer configId = -1;
	Integer configDegreeOfParallelism = -1;
	List<Integer> dependencies = new ArrayList<Integer>();
	boolean parallelSwitch = false;
	List<ConfigJob> jobs = new ArrayList<ConfigJob>();
	List<Future<?>> result;

	public Configuration(Integer configId, Integer configDegreeOfParallelism, List<Integer> dependencies,
			boolean parallelSwitch) {
		super();
		this.configId = configId;
		this.configDegreeOfParallelism = configDegreeOfParallelism;
		this.dependencies = dependencies;
		this.parallelSwitch = parallelSwitch;
	}

	public Integer getConfigId() {
		return configId;
	}

	public void setConfigId(Integer configId) {
		this.configId = configId;
	}

	public Integer getConfigDegreeOfParallelism() {
		return configDegreeOfParallelism;
	}

	public void setConfigDegreeOfParallelism(Integer configDegreeOfParallelism) {
		this.configDegreeOfParallelism = configDegreeOfParallelism;
	}

	public List<Integer> getDependencies() {
		return dependencies;
	}

	public void setDependencies(List<Integer> dependencies) {
		this.dependencies = dependencies;
	}

	public boolean isParallelSwitch() {
		return parallelSwitch;
	}

	public void setParallelSwitch(boolean parallelSwitch) {
		this.parallelSwitch = parallelSwitch;
	}

	public List<ConfigJob> getJobs() {
		return jobs;
	}

	public void setJobs(List<ConfigJob> jobs) {
		this.jobs = jobs;
	}

	public void addJob(ConfigJob job) {
		this.jobs.add(job);
	}

	@Override
	public String toString() {
		return "Configuration [configId=" + configId + ", configDegreeOfParallelism=" + configDegreeOfParallelism
				+ ", dependencies=" + dependencies + ", parallelSwitch=" + parallelSwitch + ", jobs=" + jobs + "]";
	}

	public void run() {
		Scheduler scheduler = new Scheduler(jobs, configDegreeOfParallelism);
		while (true) {
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (Repository.isIndependent(configId)) {
				List<Future<?>> result = scheduler.startSubmit();
				notifyDependent(configId, result);
				break;
			} else {
				synchronized (this) {
					try {

						wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}
	}

	private synchronized void notifyDependent(Integer configId, List<Future<?>> result) {
		while (true) {
			if (result.stream().filter(e -> !e.isDone()).collect(Collectors.toList()).size() == 0) {
				System.out.println("Breaking");
				break;
			}
		}
		Repository.notifyAll(configId);

	}

	public Thread getThreadByName(String threadName) {
		for (Thread t : Thread.getAllStackTraces().keySet()) {
			if (t.getName().equals(threadName))
				return t;
		}
		return null;
	}

	public boolean isExecutionCompleted() {
		boolean status = true;
		if (result != null) {
			for (Future<?> individualResult : result) {
				if (!individualResult.isDone()) {
					status = false;
					break;
				}
			}
		} else {
			status = false;
		}
		return status;
	}

}
