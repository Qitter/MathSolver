package org.qitter.language;

import org.jetbrains.annotations.NotNull;
import org.qitter.config.ConfigReader;
import org.qitter.log.Logger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public enum Language {
    CHINESE("chinese","language/chinese.lang"),
    ENGLISH("english","language/english.lang");
    @NotNull
    private final LanguageLoader languageLoader;
    Language(@NotNull String name,@NotNull String savePathInJar) {
        this.name = name;
        this.languageLoader = new LanguageLoader(this, parseLanguageFromJar(savePathInJar));
    }

    /**
     * 解析jar里的lang文件
     * @param savePathInJar 文件路径（在jar中的）
     * @return 语言对应的 {@link Map}
     */
    @NotNull
    private static Map<String,String> parseLanguageFromJar(@NotNull String savePathInJar) {
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(
                    Objects.requireNonNull(Language.class.getClassLoader().getResourceAsStream(savePathInJar)), ConfigReader.CHARSET)
            );
        } catch (IOException e) {
            throw Logger.getLogger().error(new RuntimeException(e));
        } catch (NullPointerException e) {
            throw Logger.getLogger().error(new RuntimeException("无法找到语言文件 : " + savePathInJar));
        }
        Map<String,String> map = new HashMap<>();
        properties.forEach((k,v) -> map.put(k.toString(),v.toString()));
        Logger.getLogger().log("加载语言文件 : " + savePathInJar);
        Logger.getLogger().log("语言文件内容 : " + map);
        Logger.getLogger().log();
        return map;
    }
    @NotNull
    private final String name;
    @NotNull
    public String getName() {
        return name;
    }
    @NotNull
    public LanguageLoader getLanguageLoader() {
        return languageLoader;
    }
}
