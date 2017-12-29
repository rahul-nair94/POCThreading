package com.bitwiseglobal.gpqa.threading_poc;

import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Repository {
	private static Map<Integer, List<Integer>> dependencyRepository = new HashMap<Integer, List<Integer>>();
	private static Map<Thread, Configuration> allThreads = new HashMap<Thread, Configuration>();

	public static List<Integer> getDependency(Integer configID) {
		return dependencyRepository.get(configID);
	}

	public static boolean isIndependent(Integer id) {
		boolean status = true;
		List<Integer> dependencies = getDependency(id);
		if (id == null)
			return true;
		else {
			if (dependencies != null && !dependencies.contains(null))
				for (Integer confID : dependencies) {
					if (!isConfPresentAndDead(confID))
						status = false;
				}
			else
				return true;
		}
		return status;
	}

	private static boolean isConfPresentAndDead(Integer confID) {
		boolean status = false;
		for (Thread thread : allThreads.keySet()) {
			if (!thread.isAlive() && thread.getName().trim().equals(confID.toString()))
				status = true;
		}
		return status;
	}

	public static void addDependency(Integer configID, List<Integer> dependency) {
		dependencyRepository.put(configID, dependency);
	}

	public static void printRepo() {
		System.out.println("key\t\t\t\t\t\t\t");
		for (Integer key : dependencyRepository.keySet()) {
			System.out.println(key + "\t\t\t\t\t\t\t" + dependencyRepository.get(key));
		}
	}

	public static void addThread(Thread thread, Configuration configuration) {
		allThreads.put(thread, configuration);
	}

	public static void startAllThreads() {
		System.out.println(allThreads);
		allThreads.keySet().parallelStream().forEach(e -> e.start());
	}

	public static Map<Thread, Configuration> getThreadList() {
		return allThreads;
	}

	public static void notifyAll(Integer configId) {
		System.out.println("Thread " + configId + " is attempting to notify!!");
		for (Thread thread : getThreadList().keySet()) {
			System.out.println("In " + thread.getName() + " Config Id  " + configId);
			// if (!thread.getName().trim().equals(configId.toString())) {
			// System.out.println("Contains " +
			// getThreadList().containsKey(thread));
			// System.out.println("Notifying all");
			// synchronized (getThreadList().get(thread)) {
			// System.out.println("Thread with config ID " + thread.getName() +
			// " is notified!!");
			// getThreadList().get(thread).notify();
			// }
			// }

			if (allThreads.get(thread).getDependencies() != null
					&& allThreads.get(thread).getDependencies().contains(configId)) {
				System.out.println("Contains " + getThreadList().containsKey(thread));
				System.out.println("Notify "+thread.getName());
				synchronized (getThreadList().get(thread)) {
					System.out.println("Thread with config ID " + thread.getName() + " is notified!!");
					getThreadList().get(thread).notify();
				}
			}

		}
		if (getThreadList().keySet().stream().filter(e -> e.isAlive()).collect(Collectors.toList()).size() == 0) {
			System.out.println("All threads completed execution!!");
			System.exit(0);
		}

	}
}
