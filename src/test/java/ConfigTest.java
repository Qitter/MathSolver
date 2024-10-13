import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qitter.config.ConfigReader;
import org.qitter.config.ConfigEnum;

public class ConfigTest extends BaseTest{
    @Test
    void logTest() {
        ConfigReader configReader = ConfigEnum.LOG_CONFIG.getConfigReader();
        boolean b = configReader.getBoolean("log");
        Assertions.assertTrue(b);
    }
}
