import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qitter.config.ConfigEnum;
import org.qitter.config.ConfigReader;

public class ConfigTest extends BaseTest{
    @Test
    void logTest() {
        ConfigReader configReader = ConfigEnum.LOG_CONFIG.getConfigReader();
        boolean b = configReader.getBoolean("log");
        Assertions.assertTrue(b);
    }
}
