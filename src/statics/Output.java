package statics;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Output {
	/**
	 * Making new directory "dirName" into "path" by "mkdirs()".<br>
	 * If parent directory does not exist, this method makes parent directory simultaneously.<br>
	 * @param path
	 * @param dirName
	 */
	public static void makeDir(String path, String dirName) {
		String sep = File.separator;
		mkdirs(path + sep + dirName);
	}

	public static void mkdirs(String dirName) {
		File newdir = new File(dirName);
		newdir.mkdirs();
	}

	/**
	 * String用
	 * @param fileName
	 * @param str : String
	 */
	public static void writeln(String fileName, String str) {
		String[] array = new String[] {str};
		writeln(fileName, array);
	}

	/**
	 * ArrayList用
	 * @param fileName
	 * @param strs : ArrayList{@literal <String>}
	 */
	public static void writeln(String fileName, ArrayList<String> strs) {
		String[] array = (String[]) strs.toArray(new String[0]);
		writeln(fileName, array);
	}

	/**
	 * 配列用
	 * @param fileName
	 * @param array : String[]
	 */
	public static void writeln(String fileName, String[] array){

		try {
//			FileWriter fw = new FileWriter(fileName, true);
			FileWriter fw = new FileWriter(fileName, false);
			PrintWriter pw = new PrintWriter( new BufferedWriter(fw) );
			for(int i=0; i<array.length; i++){
				 pw.println(array[i]);
			}
			pw.close();
	    }
		catch (IOException ex){
			ex.printStackTrace();
	    }
	}
}
