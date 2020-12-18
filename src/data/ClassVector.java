package data;

import java.util.Arrays;

public class ClassVector extends Classes<Integer[]> {
	// ************************************************************
	// Fields

	// ************************************************************
	// Constructor
	public ClassVector(int length) {
		this.classLabel = new Integer[length];
	}

	public ClassVector(Integer[] classLabel) {
		this.classLabel = Arrays.copyOf(classLabel, classLabel.length);
	}

	// ************************************************************
	// Methods

	/**
	 *
	 */
	@Override
	public Integer[] getClassLabel() {
		return this.classLabel;
	}

	/**
	 *
	 */
	@Override
	public void setClassLabel(Integer[] classLabel) {
		this.classLabel = Arrays.copyOf(classLabel, classLabel.length);
	}


}
