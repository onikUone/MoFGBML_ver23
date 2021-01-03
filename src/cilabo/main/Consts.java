package cilabo.main;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 各種定数 定義クラス
 * Consts.[変数名]でアクセス可能
 *
 * 本ソース(Consts.java)の以下で指定している値はデフォルト値
 * もし、jarエクスポート後に変更したい値が出てきた場合は、
 * consts.propertiesに変更したい変数を書けば良い
 *     例: 「WINDOWS = 1」(in consts.properties)
 * .propertiesファイルは、Consts.setConsts(String source)メソッドによって読み込まれる
 *     例: Consts.setConsts("consts");  // load consts.properties
 *
 */
public class Consts {

	//OS ************************************
	public static int WINDOWS = 0;	//Windows
	public static int UNIX = 1;		//Unix or Mac

	//Fuzzy Clasifier ************************************
	/** don't careにしない条件部の数 */
	public static int ANTECEDENT_LEN = 5;
	/** don't care適応確率 */
	public static double DONT_CARE_RT = 0.8;
	/** 初期ル―ル数 */
	public static int INITIATION_RULE_NUM = 30;
	/** 1識別器あたりの最大ルール数 */
	public static int MAX_RULE_NUM = 60;
	/** 1識別器あたりの最小ルール数 */
	public static int MIN_RULE_NUM = 1;

	//FGBML ************************************
	/** Michigan適用確率 */
	public static double MICHIGAN_OPE_RT = 0.5;	//元RULE_OPE_RT
	/** ルール入れ替え割合 */
	public static double RULE_CHANGE_RT = 0.2;
	/** Michigan交叉確率 */
	public static double MICHIGAN_CROSS_RT = 0.9;	//元RULE_CROSS_RT
	/** Pittsburgh交叉確率 */
	public static double PITTSBURGH_CROSS_RT = 0.9;	//元RULESET_CROSS_RT

	//Experiment ************************************
	/** ドット表示する世代間隔 */
	public static int PER_SHOW_DOT = 100;	//元PER_SHOW_GENERATION_NUM
	/** 現世代数表示する世代間隔 */
	public static int PER_SHOW_GENERATION_DETAIL = 10;

	//Index ************************************
	/** 学習用データ */
	public static int TRAIN = 0;
	/** 評価用データ */
	public static int TEST = 1;

	//Folders' Name ************************************
	public static String ROOTFOLDER = "result";
	public static String RULESET = "ruleset";
	public static String INDIVIDUAL = "individual";
	public static String POPULATION = "population";
	public static String OFFSPRING = "offspring";
	public static String SUBDATA = "subdata";
	public static String DATA = "data";
	public static String TIMES = "times";


	/******************************************/

	public static void setConsts(String source) {
		String dir = "./";
		URLClassLoader urlLoader = null;
		ResourceBundle bundle = null;
		try {
			urlLoader = new URLClassLoader(new URL[] {new File(dir).toURI().toURL()});
			bundle = ResourceBundle.getBundle(source, Locale.getDefault(), urlLoader);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		if(bundle.containsKey("WINDOWS")) { WINDOWS = Integer.parseInt(bundle.getString("WINDOWS")); }
		if(bundle.containsKey("UNIX")) { UNIX = Integer.parseInt(bundle.getString("UNIX")); }
		if(bundle.containsKey("ANTECEDENT_LEN")) { ANTECEDENT_LEN = Integer.parseInt(bundle.getString("ANTECEDENT_LEN")); }
		if(bundle.containsKey("DONT_CARE_RT")) { DONT_CARE_RT = Double.parseDouble(bundle.getString("DONT_CARE_RT")); }
		if(bundle.containsKey("INITIATION_RULE_NUM")) { INITIATION_RULE_NUM = Integer.parseInt(bundle.getString("INITIATION_RULE_NUM")); }
		if(bundle.containsKey("MAX_RULE_NUM")) { MAX_RULE_NUM = Integer.parseInt(bundle.getString("MAX_RULE_NUM")); }
		if(bundle.containsKey("MIN_RULE_NUM")) { MIN_RULE_NUM = Integer.parseInt(bundle.getString("MIN_RULE_NUM")); }
		if(bundle.containsKey("MICHIGAN_OPE_RT")) { MICHIGAN_OPE_RT = Double.parseDouble(bundle.getString("MICHIGAN_OPE_RT")); }
		if(bundle.containsKey("RULE_CHANGE_RT")) { RULE_CHANGE_RT = Double.parseDouble(bundle.getString("RULE_CHANGE_RT")); }
		if(bundle.containsKey("MICHIGAN_CROSS_RT")) { MICHIGAN_CROSS_RT = Double.parseDouble(bundle.getString("MICHIGAN_CROSS_RT")); }
		if(bundle.containsKey("PITTSBURGH_CROSS_RT")) { PITTSBURGH_CROSS_RT = Double.parseDouble(bundle.getString("PITTSBURGH_CROSS_RT")); }
		if(bundle.containsKey("PER_SHOW_DOT")) { PER_SHOW_DOT = Integer.parseInt(bundle.getString("PER_SHOW_DOT")); }
		if(bundle.containsKey("PER_SHOW_GENERATION_DETAIL")) { PER_SHOW_GENERATION_DETAIL = Integer.parseInt(bundle.getString("PER_SHOW_GENERATION_DETAIL")); }
		if(bundle.containsKey("TRAIN")) { TRAIN = Integer.parseInt(bundle.getString("TRAIN")); }
		if(bundle.containsKey("TEST")) { TEST = Integer.parseInt(bundle.getString("TEST")); }
		if(bundle.containsKey("ROOTFOLDER")) { ROOTFOLDER = bundle.getString("ROOTFOLDER"); }
		if(bundle.containsKey("RULESET")) { RULESET = bundle.getString("RULESET"); }
		if(bundle.containsKey("INDIVIDUAL")) { INDIVIDUAL = bundle.getString("INDIVIDUAL"); }
		if(bundle.containsKey("POPULATION")) { POPULATION = bundle.getString("POPULATION"); }
		if(bundle.containsKey("OFFSPRING")) { OFFSPRING = bundle.getString("OFFSPRING"); }
		if(bundle.containsKey("SUBDATA")) { SUBDATA = bundle.getString("SUBDATA"); }
		if(bundle.containsKey("DATA")) { DATA = bundle.getString("DATA"); }
		if(bundle.containsKey("TIMES")) { TIMES = bundle.getString("TIMES"); }

		bundle = null;
	}

	public static String getString() {
		StringBuilder sb = new StringBuilder();
		String ln = System.lineSeparator();
		sb.append("Consts: " + ln);

		sb.append("WINDOWS" + " = " + String.valueOf(WINDOWS));
		sb.append("UNIX" + " = " + String.valueOf(UNIX));
		sb.append("ANTECEDENT_LEN" + " = " + String.valueOf(ANTECEDENT_LEN));
		sb.append("DONT_CARE_RT" + " = " + String.valueOf(DONT_CARE_RT));
		sb.append("INITIATION_RULE_NUM" + " = " + String.valueOf(INITIATION_RULE_NUM));
		sb.append("MAX_RULE_NUM" + " = " + String.valueOf(MAX_RULE_NUM));
		sb.append("MIN_RULE_NUM" + " = " + String.valueOf(MIN_RULE_NUM));
		sb.append("MICHIGAN_OPE_RT" + " = " + String.valueOf(MICHIGAN_OPE_RT));
		sb.append("RULE_CHANGE_RT" + " = " + String.valueOf(RULE_CHANGE_RT));
		sb.append("MICHIGAN_CROSS_RT" + " = " + String.valueOf(MICHIGAN_CROSS_RT));
		sb.append("PITTSBURGH_CROSS_RT" + " = " + String.valueOf(PITTSBURGH_CROSS_RT));
		sb.append("PER_SHOW_DOT" + " = " + String.valueOf(PER_SHOW_DOT));
		sb.append("PER_SHOW_GENERATION_DETAIL" + " = " + String.valueOf(PER_SHOW_GENERATION_DETAIL));
		sb.append("TRAIN" + " = " + String.valueOf(TRAIN));
		sb.append("TEST" + " = " + String.valueOf(TEST));
		sb.append("ROOTFOLDER" + " = " + String.valueOf(ROOTFOLDER));
		sb.append("RULESET" + " = " + String.valueOf(RULESET));
		sb.append("INDIVIDUAL" + " = " + String.valueOf(INDIVIDUAL));
		sb.append("POPULATION" + " = " + String.valueOf(POPULATION));
		sb.append("OFFSPRING" + " = " + String.valueOf(OFFSPRING));
		sb.append("SUBDATA" + " = " + String.valueOf(SUBDATA));
		sb.append("DATA" + " = " + String.valueOf(DATA));
		sb.append("TIMES" + " = " + String.valueOf(TIMES));

		return sb.toString();
	}


}
