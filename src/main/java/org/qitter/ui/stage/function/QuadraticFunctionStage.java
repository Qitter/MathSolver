package org.qitter.ui.stage.function;

import org.jetbrains.annotations.NotNull;
import org.qitter.Main;
import org.qitter.language.LanguageManager;
import org.qitter.math.function.QuadraticFunction;
import org.qitter.ui.StageManager;
import org.qitter.ui.stage.Stage;

public class QuadraticFunctionStage extends Stage {
    @NotNull
    private static final QuadraticFunctionStage INSTANCE = new QuadraticFunctionStage();
    @NotNull
    public static QuadraticFunctionStage getInstance() {
        return INSTANCE;
    }
    private QuadraticFunctionStage() {}
    @Override
    public void enter() {
        System.out.println(LanguageManager.getInstance().getString("function.quadratic.stage.enter"));
        System.out.print(LanguageManager.getInstance().getString("function.quadratic.stage.input"));
        while (Main.scanner.hasNext()) {
            String input = Main.scanner.nextLine();
            if(input.equals(LanguageManager.getInstance().getString("function.quadratic.stage.key.back"))) {
                StageManager.getInstance().back();
                return;
            }
            QuadraticFunction function = new QuadraticFunction(input);
            System.out.print(LanguageManager.getInstance().getString("function.quadratic.stage.result"));
            System.out.println(function.asSimpleForm().getExpression());
            System.out.print(LanguageManager.getInstance().getString("function.quadratic.stage.input"));
        }
    }
}
