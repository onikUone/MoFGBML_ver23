package cilabo.utility;

import java.util.concurrent.ForkJoinPool;

public class Parallel {
	// ************************************************************
	// Fields
	public static ForkJoinPool learningForkJoinPool = new ForkJoinPool(1);

	// ************************************************************
	// Constructor

	// ************************************************************
	// Methods

	public static void initLearningForkJoinPool(int core) {
		Parallel.learningForkJoinPool = new ForkJoinPool(core);
	}

}
