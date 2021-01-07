package cilabo.main;

/**
 * メンバ変数はpublic static修飾子をつける
 * コマンドライン引数をグローバル変数化するためのインターフェース
 * 実験ごとにArgsを実装して、グローバル変数を生成できる
 *
 */
public interface Args {

	public void loadArgs(String[] args);

	public String getParamsString();
}
