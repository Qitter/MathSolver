package org.qitter.ui.stage;

import org.jetbrains.annotations.NotNull;
import org.qitter.Main;
import org.qitter.language.LanguageManager;
import org.qitter.math.problems.equation.LinearEquation;
import org.qitter.ui.StageManager;

public class EquationStage extends Stage {
    private static final EquationStage INSTANCE = new EquationStage();
    public static @NotNull EquationStage getInstance() {
        return INSTANCE;
    }

    @Override
    public void enter() {
        System.out.println(LanguageManager.getInstance().getString("equation.stage.enter"));
        System.out.print(LanguageManager.getInstance().getString("equation.stage.input"));
        while (Main.scanner.hasNext()) {
            String input = Main.scanner.nextLine();
            if (input.equals(LanguageManager.getInstance().getString("equation.stage.key.back"))) {
                StageManager.getInstance().back();
                return;
            }
            LinearEquation equation = new LinearEquation(input);
            System.out.print(LanguageManager.getInstance().getString("equation.stage.result"));
            System.out.println(equation.solve());
            System.out.print(LanguageManager.getInstance().getString("equation.stage.input"));
        }
    }
}
