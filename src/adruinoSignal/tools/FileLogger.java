package adruinoSignal.tools;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Initialisiert den Logger so, dass er ein eine im Konstruktor uebergebenes
 * File-Path in eine Datei schreibt
 *
 * @author Klaus
 */
public class FileLogger {

    private static FileHandler fileTxt;
    private static SimpleFormatter formatterTxt;

    /**
     * Initialisiert den Logger und laesst ihn in eine txt Datei schreiben.
     *
     * @param level logLevel
     * @param path Pfad an dem die Log-Datei erstellt wird
     * @return erzeugter Logger
     * @throws IOException wenn der angegebene Pfad ungueltig ist
     */
    static public Logger setup(Level level, String path) throws IOException {
        Logger logger = Logger.getLogger("");
        logger.setLevel(level);

        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }
        fileTxt = new FileHandler(path);

        formatterTxt = new SimpleFormatter();

        fileTxt.setFormatter(formatterTxt);
        logger.addHandler(fileTxt);

        return logger;
    }
}
