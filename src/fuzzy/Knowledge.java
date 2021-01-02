package fuzzy;

import java.util.ArrayList;

public class Knowledge {
	// ************************************************************
	// Fields

	/**  */
	float[][] ranges;

	/**  */
	String[] inputVariableNames;

	/**  */
	String[] classLabels;

	/**  */
	ArrayList<String> outputVariableNames = new ArrayList<>();

	// ************************************************************
	// Constructor
	public Knowledge() {}

	public Knowledge(int dimension, int Cnum, int fuzzifyH) {
		this.ranges = new float[dimension][];
		for(int i = 0; i < dimension; i++) {
			this.ranges[i] = new float[2];	//{domainLeft, domainRight}
		}
		this.inputVariableNames = new String[dimension];
		this.classLabels = new String[Cnum];
	}

	// ************************************************************
	// Methods

	/**
	 *
	 */

}
