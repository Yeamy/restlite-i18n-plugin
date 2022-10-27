package yeamy.restlite.i18n.lang;

import java.io.BufferedReader;
import java.io.IOException;

public interface LineReader {

    static void readFile(String fn, BufferedReader reader, LineReader r) throws IOException, LangException {
        int l = 0;
        while (true) {
            l++;
            String text = reader.readLine();
            if (text == null) {
                break;
            }
            if (text.length() == 0 || text.charAt(0) == '#') {
                continue;
            }
            int split = text.indexOf('=');
            if (split <= 0) {
                throw new LangException("Read line error in file " + fn + " at line " + l);
            }
            String key = text.substring(0, split).trim();
            r.readLine(fn, l, key, text, split + 1);
        }
    }

    void readLine(String fn, int line, String key, String text, int from) throws LangException;
}
