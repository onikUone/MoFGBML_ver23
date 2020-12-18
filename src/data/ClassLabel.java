package data;

public class ClassLabel extends Classes<Integer> {
	// ************************************************************
	// Fields

	// ************************************************************
	// Constructor
	public ClassLabel() {}

	public ClassLabel(Integer classLabel) {
		this.classLabel = classLabel;
	}

	// ************************************************************
	// Methods

	/**
	 *
	 */
	@Override
	public Integer getClassLabel() {
		return this.classLabel;
	}

	/**
	 *
	 */
	@Override
	public void setClassLabel(Integer classLabel) {
		this.classLabel = classLabel;
	}


}
