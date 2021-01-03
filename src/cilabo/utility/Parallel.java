package cilabo.utility;

import java.util.concurrent.ForkJoinPool;

public class Parallel {
	// ************************************************************
	// Fields
	public static ForkJoinPool forkJoinPool = new ForkJoinPool(1);

	// ************************************************************
	// Constructor

	// ************************************************************
	// Methods

	public static void initForkJoinPool(int core) {
		Parallel.forkJoinPool = new ForkJoinPool(core);
	}

}
