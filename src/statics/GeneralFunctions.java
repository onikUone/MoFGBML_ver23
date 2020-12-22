package statics;

import java.util.ArrayList;

import random.MersenneTwisterFast;


public class GeneralFunctions {

	/**
	 * <h1>Distance between 2 vectors.</h1>
	 * @param vector1 : double[] :
	 * @param vector2 : double[] :
	 * @return double : distance between vector1 and vector2.
	 */
	public static double distanceVectors(double[] vector1, double[] vector2) {
		if(vector1.length != vector2.length) {
			return -1;
		}
		double sum = 0.0;
		for(int n = 0; n < vector1.length; n++) {
			sum += (vector1[n] - vector2[n]) * (vector1[n] - vector2[n]);
		}
		return Math.sqrt(sum);
	}

	/**
	 * <h1>Calculation norm</h1>
	 * @param vector : double[]
	 * @return double : norm of vector
	 */
	public static double vectorNorm(double[] vector) {
		double norm = 0.0;
		double sum = 0.0;
		for(int i = 0; i < vector.length; i++) {
			sum += vector[i] * vector[i];
		}
		norm = Math.sqrt(sum);
		return norm;
	}

	/**
	 * <h1>Calculate Inner Product of a and b.</h1>
	 * @param a : double[]
	 * @param b : double[]
	 * @return double : Inner Product
	 */
	public static double innerProduct(double[] a, double[] b) {
		if(a.length != b.length) {
			return -1;
		}
		double[] ab = new double[a.length];
		for(int i = 0; i < a.length; i++) {
			ab[i] = a[i] * b[i];
		}
		return GeneralFunctions.vectorNorm(ab);
	}

	/**
	 * <h1>Calculate Recall for Multi-Label Classification</h1>
	 * @param classified : double[]
	 * @param answer : double[]
	 * @return double : Recall
	 */
	public static double RecallMetric(int[] classified, int[] answer) {
		double correctAssociate = 0.0;
		double answerAssociate = 0.0;
		for(int i = 0; i < classified.length; i++) {
			if(classified[i] == 1 && answer[i] == 1) {
				correctAssociate++;
			}
			if(answer[i] == 1) {
				answerAssociate++;
			}
		}
		if(answerAssociate == 0) {
			return 0;
		}
		return correctAssociate / answerAssociate;
	}

	/**
	 * <h1>Calculate Precision for Multi-Label Classification</h1>
	 * @param classified : double[]
	 * @param answer : double[]
	 * @return double : Precision
	 */
	public static double PrecisionMetric(int[] classified, int[] answer) {
		double correctAssociate = 0.0;
		double classifiedAssociate = 0.0;
		for(int i = 0; i < classified.length; i++) {
			if(classified[i] == 1 && answer[i] == 1) {
				correctAssociate++;
			}
			if(classified[i] == 1) {
				classifiedAssociate++;
			}
		}
		if(classifiedAssociate == 0) {
			return 0;
		}
		return correctAssociate / classifiedAssociate;
	}

	/**
	 * <h1>非復元抽出</h1><br/>
	 * <h1>Sampling without replacement</h1><br/>
	 * @param box : int : 元のデータサイズ
	 * @param want : int : 抽出したいindexの数
	 * @param rnd
	 * @return Integer[] : 非復元抽出したwant個のindex
	 */
	public static Integer[] samplingWithout(int box, int want, MersenneTwisterFast rnd) {
		MersenneTwisterFast uniqueRnd = new MersenneTwisterFast(rnd.nextInt());
		Integer[] answer = new Integer[want];
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i = 0; i < box; i++) {
			list.add(i);
		}
		for(int i = 0; i < want; i++) {
			int index = uniqueRnd.nextInt(list.size());
			answer[i] = list.get(index);
			list.remove(index);
		}
		return answer;
	}

	/**
	 * <h1>log関数 底の変換公式</h1>
	 * @param a : double : 引数
	 * @param b : double : 底
	 * @return double : log_b (a)
	 */
	public static double log(double a, double b) {
		return (Math.log(a) / Math.log(b));
	}

	/**
	 * <h1>組合せの総数, nCr</h1>
	 * @param n
	 * @param r
	 * @return int : nCr
	 */
	public static int combination(int n, int r) {
		int ans = 1;
		for(int i = 1; i <= r; i++) {
			ans = ans * (n-i + 1) / i;
		}
		return ans;
	}

}
