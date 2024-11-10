package org.qitter.ui.stage;

import org.jetbrains.annotations.NotNull;
import org.qitter.Main;
import org.qitter.language.LanguageManager;
import org.qitter.math.problems.NumericalProblem;
import org.qitter.ui.StageManager;

public class NumericalStage extends Stage{
    @NotNull
    private static final NumericalStage INSTANCE = new NumericalStage();
    @NotNull
    public static NumericalStage getInstance() {
        return INSTANCE;
    }
    private NumericalStage() {}
    @Override
    public void enter() {
        System.out.println(LanguageManager.getInstance().getString("numeral.stage.enter"));
        System.out.print(LanguageManager.getInstance().getString("numeral.stage.input"));
        while (Main.scanner.hasNext()) {
            String expression = Main.scanner.nextLine();
            if (expression.equals(LanguageManager.getInstance().getString("numeral.stage.key.back"))) {
                StageManager.getInstance().back();
                return;
            }
            System.out.println(LanguageManager.getInstance().getString("numeral.result") + new NumericalProblem(expression).solve());
            System.out.print(LanguageManager.getInstance().getString("numeral.stage.input"));
        }
    }
}
