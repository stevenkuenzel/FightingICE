package loader;

import aiinterface.AIController;
import aiinterface.AIInterface;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * キャラクターの設定ファイルや画像等のリソースをロードするためのシングルトンパターンクラス．
 */
public class ResourceLoader {

    public String fightingICEroot;

    public ResourceLoader(String fightingICEroot) {
        this.fightingICEroot = fightingICEroot;
    }

    /**
     * 指定されたAI名のjarファイルを読み込み、AI情報を格納したコントローラを返す．
     *
     * @param aiName 読み込みたいAIの名前
     * @return 読み込んだAIの情報を格納したコントローラ<br>
     * 読み込んだAIが無ければnullを返す．
     */
    public AIController loadAI(String aiName) {
        File file = new File(fightingICEroot + "/data/ai/" + aiName + ".jar");
//        File file = new File("./data/ai/" + aiName + ".jar");

        try {
            ClassLoader cl = URLClassLoader.newInstance(new URL[]{file.toURI().toURL()});
            Class<?> c = cl.loadClass(aiName);
            AIInterface ai = (AIInterface) c.newInstance();

            return new AIController(ai);
        } catch (MalformedURLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
