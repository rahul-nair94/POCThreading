package com.bitwiseglobal.gpqa.threading_poc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class Scheduler {

	ExecutorService executor = null;
	List<ConfigJob> jobs = null;
	Integer parallelismLimit = null;
	List<Future<?>> futures = new ArrayList<Future<?>>();

	public Scheduler(List<ConfigJob> jobs, Integer parallelismLimit) {
		executor = Executor.getInstance();
		this.jobs = jobs;
		this.parallelismLimit = parallelismLimit;
	}

	public List<Future<?>> startSubmit() {

		if (jobs != null && parallelismLimit != null && jobs.size() != 0 && parallelismLimit != 0) {
			Iterator<ConfigJob> jobIterator = jobs.iterator();
			System.out.println(Thread.currentThread() + "size" + futures.size());
			System.out.println(Thread.currentThread() + "limit" + parallelismLimit);
			while (jobIterator.hasNext()) {
				if (futures.size() < parallelismLimit) {
					ConfigJob job = jobIterator.next();
					System.out.println("Submitting job " + job + " on thread " + Thread.currentThread());
					Future<?> resultOfThisJob = executor.submit(job);
					futures.add(resultOfThisJob);
				} else {
					waitForFreeThread();
				}
			}
		} else {
			System.err.println(
					"************************************\njobs or parallelism Limit Not Parsed\n************************************");
		}
		return futures;
	}

	private void waitForFreeThread() {
		try {
			boolean status = true;
			while (status) {
				for (Future<?> future : futures) {
					if (future.isDone()) {
						futures.remove(future);
						status = false;
						break;
					}
				}
				Thread.sleep(2000);
			}
		} catch (Exception e) {
			System.err.println("Exception in waiting for thread!");
		}
	}

}
