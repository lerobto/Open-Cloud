/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.wrapper.workload;

import de.tammo.cloud.core.threading.ThreadBuilder;
import de.tammo.cloud.core.workload.Workload;
import de.tammo.cloud.core.workload.WorkloadFactory;
import de.tammo.cloud.wrapper.Wrapper;
import de.tammo.cloud.wrapper.network.packets.out.WrapperWorkloadOutPacket;

import java.util.ArrayList;

public class WorkloadProvider implements Runnable {

	private Thread thread;

	public void start() {
		this.thread = new ThreadBuilder("Workload Thread", this).setDaemon().start();
	}

	public void stop() {
		this.thread.interrupt();
	}

	public void run() {
		while (Wrapper.getWrapper().isRunning()) {
			final Workload workload = this.getAverage(Wrapper.getWrapper().getConfiguration().getAverageSeconds());
			Wrapper.getWrapper().getNetworkHandler().sendPacketToMaster(new WrapperWorkloadOutPacket(workload.getCpu(), workload.getMemory()));
		}
	}

	private Workload getAverage(final int seconds) {
		final ArrayList<Workload> average = new ArrayList<>();

		for (int i = 0; i < seconds; i++) {
			average.add(new WorkloadFactory().newWorkload());

			try {
				Thread.sleep(1000);
			} catch (InterruptedException ignored) {}
		}

		int cpu = 0;
		int memory = 0;

		for (final Workload workload : average) {
			cpu += workload.getCpu();
			memory += workload.getMemory();
		}

		return new Workload(cpu / average.size(), memory / average.size());
	}
}
