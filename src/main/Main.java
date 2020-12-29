package main;

public class Main {

	public static void main(String[] args) {
		System.out.println("Hello Maven!");


		System.out.println(Consts.ANTECEDENT_LEN);
		String source = args[0];
		Consts.setConsts(source);
		System.out.println(Consts.ANTECEDENT_LEN);

	}

}
