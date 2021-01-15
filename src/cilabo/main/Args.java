package cilabo.main;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * メンバ変数はpublic static修飾子をつける
 * コマンドライン引数をグローバル変数化するためのインターフェース
 * 実験ごとにArgsを実装して、グローバル変数を生成できる
 *
 */
public abstract class Args {
	private static String className = null;

	public static void loadArgs(String argsName, String[] args) {
		className = argsName;
		Args a = null;
		try {
			a = (Args)Class.forName(argsName).getConstructor().newInstance();
		} catch (InstantiationException |
				 IllegalAccessException |
				 IllegalArgumentException |
				 InvocationTargetException |
				 NoSuchMethodException |
				 SecurityException |
				 ClassNotFoundException e) {
			e.printStackTrace();
		}
		a.load(args);
	}

	public abstract void load(String[] args);

	public static String getParamsString() {
		if(className == null) {
			return null;
		}

		Args args = null;
		try {
			args = (Args)Class.forName(className).getConstructor().newInstance();
		} catch (InstantiationException |
				 IllegalAccessException |
				 IllegalArgumentException |
				 InvocationTargetException |
				 NoSuchMethodException |
				 SecurityException |
				 ClassNotFoundException e) {
			e.printStackTrace();
		}

		StringBuilder sb = new StringBuilder();
		String ln = System.lineSeparator();
		sb.append("Class: " + args.getClass().getCanonicalName() + ln);
		sb.append("Parameters: " + ln);
		for(Field field : args.getClass().getDeclaredFields()) {
			try {
				field.setAccessible(true);
				sb.append( field.getName() + " = " + field.get(args) + ln );
			}
			catch(IllegalAccessException e) {
				sb.append(field.getName() + " = " + "access denied" + ln);
			}
		}
		return sb.toString();
	}
}
