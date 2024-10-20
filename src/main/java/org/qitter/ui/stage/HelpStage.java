package org.qitter.ui.stage;

import org.jetbrains.annotations.NotNull;
import org.qitter.language.LanguageManager;
import org.qitter.ui.StageManager;

public class HelpStage extends Stage {
    private static final HelpStage INSTANCE = new HelpStage();
    public static @NotNull Stage getInstance() {
        return INSTANCE;
    }

    @Override
    public void enter() {
        System.out.println(LanguageManager.getInstance().getString("help.numeral"));
        System.out.println(LanguageManager.getInstance().getString("help.algebra"));
        System.out.println(LanguageManager.getInstance().getString("help.equation"));
        System.out.println(LanguageManager.getInstance().getString("help.function"));
        System.out.println(LanguageManager.getInstance().getString("help.operator.add"));
        System.out.println(LanguageManager.getInstance().getString("help.operator.sub"));
        System.out.println(LanguageManager.getInstance().getString("help.operator.mul"));
        System.out.println(LanguageManager.getInstance().getString("help.operator.div"));
        System.out.println(LanguageManager.getInstance().getString("help.operator.pow"));
        System.out.println(LanguageManager.getInstance().getString("help.operator.root"));
        System.out.println();
        StageManager.getInstance().back();
    }
}
