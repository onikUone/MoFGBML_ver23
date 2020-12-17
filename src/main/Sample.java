package main;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;

public class Sample {
	public static void main(String[] args) {
		String dir = "./";
		String source = args[0];
		URLClassLoader urlLoader = null;
		ResourceBundle bundle = null;
		try {
			urlLoader = new URLClassLoader(new URL[] {new File(dir).toURI().toURL()});
			bundle = ResourceBundle.getBundle(source, Locale.getDefault(), urlLoader);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}


		int a = -1;
		String s = "NG";
		if(bundle.containsKey("a")) { a = Integer.parseInt(bundle.getString("a")); }
		if(bundle.containsKey("s")) { s = bundle.getString("s"); }

		System.out.println(a);
		System.out.println(s);


	}

	public static int sum(int a, int b) {
		return a + b;
	}
}
