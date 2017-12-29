package com.bitwiseglobal.gpqa.threading_poc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadTest {

	public void main(String[] args) {
		ExecutorService fjp = Executors.newFixedThreadPool(5);

		fjp.submit(new Config());
		fjp.submit(new Config());
		fjp.submit(new Config());
		fjp.submit(new Config());
		fjp.submit(new Config());
		fjp.submit(new Config());
		fjp.submit(new Config());
		fjp.submit(new Config());
		fjp.execute(() -> System.out.println("\n\n\n\nFirst\n\n\n\n"));
		fjp.submit(new Config());
		fjp.submit(new Config());
		fjp.submit(new Config());
		fjp.submit(new Config());
		fjp.submit(new Config());
		fjp.submit(new Config());
		fjp.submit(new Config());
		fjp.submit(new Config());
		fjp.submit(new Config());
		fjp.submit(new Config());
		fjp.submit(new Config());
		fjp.submit(new Config());
		fjp.submit(new Config());
		fjp.submit(new Config());
		fjp.submit(new Config());
		fjp.submit(new Config());
		fjp.submit(new Config());
		fjp.submit(new Config());
		fjp.submit(new Config());
		fjp.submit(new Config());
		fjp.submit(new Config());
		fjp.submit(new Config());
		fjp.submit(new Config());
		fjp.submit(new Config());
		fjp.submit(new Config());
		fjp.submit(new Config());
		fjp.submit(new Config());
		fjp.submit(new Config());
		fjp.execute(() -> System.out.println("\n\n\n\nSecond\n\n\n\n"));
		fjp.shutdown();
	}
}
