package cilabo.main;

public class ArgsSample extends Args {
	// ************************************************************
	// Fields

	public static int id = 0;
	public static String testString = "aaa";

	// ************************************************************
	// Methods

	@Override
	public void load(String[] args) {
		id = Integer.parseInt(args[0]);
		testString = args[1];
	}

}
