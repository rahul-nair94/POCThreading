package com.bitwiseglobal.gpqa.threading_poc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Config implements Runnable {

	List<String> list = new ArrayList<String>();

	@Override
	public void run() {
		list.add("A");
		list.add("B");
		list.add("C");
		list.add("D");
		list.add("E");
		list.add("F");
		list.stream().forEach(e -> System.out.println(Thread.currentThread() + " " + e));

		try {
			Random random = new Random();
			int seconds = random.nextInt(50000);
			System.out.println(Thread.currentThread()+" Seconds "+seconds);
			Thread.sleep(seconds);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		list.stream().forEach(e -> System.out.println(Thread.currentThread() + "  --> " + e));

	}

}
