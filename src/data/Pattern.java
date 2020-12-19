package data;


@SuppressWarnings("rawtypes")
public class Pattern {
	// ************************************************************
	// Fields

	/**
	 *  Identification number
	 */
	int id;

	/**
	 * Input vector
	 */
	InputVector inputVector;

	/**
	 * Class label
	 */
	Classes trueClass;

	// ************************************************************
	// Constructor
	public Pattern(int id, InputVector inputVector, Classes trueClass) {
		this.id = id;
		this.inputVector = inputVector;
		this.trueClass = trueClass;
	}

	// ************************************************************
	// Methods

	/**
	 *
	 */
	public double getDimValue(int index) {
		return this.inputVector.getDimValue(index);
	}

	/**
	 *
	 */
	public int getID() {
		return this.id;
	}

	/**
	 *
	 */
	public InputVector getInputVector() {
		return this.inputVector;
	}

	/**
	 *
	 */
	public Classes getTrueClass() {
		return this.trueClass;
	}

	@Override
	public String toString() {
		if(this.inputVector == null || this.trueClass == null) {
			return null;
		}

		String str = "id:" + String.valueOf(this.id);
		str += "," + "input:[" + this.inputVector.toString() + "]";
		str += "," + "class:[" + this.trueClass.toString() + "]";
		return str;
	}

}
