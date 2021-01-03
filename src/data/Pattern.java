package data;


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
	ClassLabel trueClass;

	// ************************************************************
	// Constructor
	public Pattern(int id, InputVector inputVector, ClassLabel trueClass) {
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
	public ClassLabel getTrueClass() {
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

	public static PatternBuilder builder() {
		return new PatternBuilder();
	}

	public static class PatternBuilder {
		private int id = -1;
		private InputVector inputVector;
		private ClassLabel trueClass;

		PatternBuilder() {}

		public Pattern.PatternBuilder id(int id) {
			this.id = id;
			return this;
		}

		public Pattern.PatternBuilder inputVector(InputVector inputVector) {
			this.inputVector = inputVector;
			return this;
		}

		public Pattern.PatternBuilder trueClass(ClassLabel trueClass) {
			this.trueClass = trueClass;
			return this;
		}

		public Pattern build() {
			try {
				if(this.id == -1) throw new IllegalStateException("[id] is not initialized.");
				if(this.inputVector == null) throw new NullPointerException("[inputVector] is not initialized.");
				if(this.trueClass == null) throw new NullPointerException("[trueClass] is not initialized.");

				return new Pattern(id, inputVector, trueClass);
			}
			catch (IllegalStateException | NullPointerException e) {
				System.out.println(e);
				return null;
			}
		}
	}


}
