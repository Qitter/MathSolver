import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.qitter.language.Language;
import org.qitter.language.LanguageManager;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LanguageLoaderTest extends BaseTest {

    @BeforeAll
    static void deleteLanguageFile() {
        File langFile = new File("language/" + LanguageManager.getInstance().getCurrentLanguage().getName() + ".lang");
        if (langFile.exists()) {
            langFile.delete();
        }
    }

    @Test
    void testSetLanguage() {
        assertEquals(LanguageManager.getInstance().getCurrentLanguage(), LanguageManager.DEFAULT_LANGUAGE);

        LanguageManager.getInstance().setLanguage(Language.CHINESE);
        assertEquals(LanguageManager.getInstance().getCurrentLanguage(), Language.CHINESE);

        LanguageManager.getInstance().setLanguage(Language.ENGLISH);
        assertEquals(LanguageManager.getInstance().getCurrentLanguage(), Language.ENGLISH);

        LanguageManager.getInstance().setLanguage("chinese");
        assertEquals(LanguageManager.getInstance().getCurrentLanguage(), Language.CHINESE);

        LanguageManager.getInstance().setLanguage("english");
        assertEquals(LanguageManager.getInstance().getCurrentLanguage(), Language.ENGLISH);

        LanguageManager.getInstance().setLanguage(LanguageManager.DEFAULT_LANGUAGE);
    }

    @Test
    void testGetString() {
        LanguageManager.getInstance().setLanguage(Language.CHINESE);
        assertEquals(LanguageManager.getInstance().getString("menu.main"), "菜单:请选择一个选项");

        LanguageManager.getInstance().setLanguage(Language.ENGLISH);
        assertEquals(LanguageManager.getInstance().getString("menu.main"), "Menu : Please select a category");
    }
}
