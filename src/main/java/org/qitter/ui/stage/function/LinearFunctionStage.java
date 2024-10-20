package org.qitter.ui.stage.function;

import org.jetbrains.annotations.NotNull;
import org.qitter.Main;
import org.qitter.language.LanguageManager;
import org.qitter.math.function.LinearFunction;
import org.qitter.ui.StageManager;
import org.qitter.ui.stage.Stage;

public class LinearFunctionStage extends Stage {
    @NotNull
    private static final LinearFunctionStage INSTANCE = new LinearFunctionStage();
    @NotNull
    public static LinearFunctionStage getInstance() {
        return INSTANCE;
    }
    private LinearFunctionStage() {}
    @Override
    public void enter() {
        System.out.println(LanguageManager.getInstance().getString("function.linear.stage.enter"));
        System.out.print(LanguageManager.getInstance().getString("function.linear.stage.input"));
        while (Main.scanner.hasNext()) {
            String input = Main.scanner.nextLine();
            if(input.equals(LanguageManager.getInstance().getString("function.linear.stage.key.back"))) {
                StageManager.getInstance().back();
                return;
            }
            LinearFunction function = new LinearFunction(input);
            System.out.print(LanguageManager.getInstance().getString("function.linear.stage.result"));
            System.out.println(function.asSimpleForm().getExpression());
            System.out.print(LanguageManager.getInstance().getString("function.linear.stage.input"));
        }
    }
}
