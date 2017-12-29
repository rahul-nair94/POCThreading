package com.bitwiseglobal.gpqa.threading_poc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Executor {
	//Default init with default value
	private static ExecutorService instance = Executors.newFixedThreadPool(5);

	public synchronized static void initializeInstance(Integer suiteDegreeOfParallelism) {
		instance = Executors.newFixedThreadPool(suiteDegreeOfParallelism);
	}

	public synchronized static ExecutorService getInstance() {
		return instance;
	}

}
