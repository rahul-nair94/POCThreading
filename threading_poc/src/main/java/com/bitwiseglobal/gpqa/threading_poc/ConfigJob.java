package com.bitwiseglobal.gpqa.threading_poc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ConfigJob implements Runnable {
	String browserName = null;
	List<String> list = new ArrayList<String>();

	public ConfigJob(String browser) {
		this.browserName = browser;
		list.add("A");
		list.add("B");
		list.add("C");
		list.add("D");
		list.add("E");
		list.add("F");
	}

	@Override
	public void run() {
		waitRandom();
		list.stream().forEach(e -> System.out.println(Thread.currentThread().getName() + " " + e));
		waitRandom();
		list.stream().forEach(e -> System.out.println(Thread.currentThread().getName() + "  --> " + e));

	}

	public void waitRandom() {
		try {
			Random random = new Random();
			int seconds = random.nextInt(15000);
			System.out.println(Thread.currentThread() + "RandomWait Seconds : " + seconds);
			Thread.sleep(seconds);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "ConfigJob [browserName=" + browserName + ", list=" + list + "]";
	}

}
