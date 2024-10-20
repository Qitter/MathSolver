package org.qitter.ui.stage;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.qitter.Main;
import org.qitter.language.LanguageManager;
import org.qitter.math.MathExpression;
import org.qitter.ui.StageManager;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class AlgebraStage extends Stage {
    @NotNull
    public static final AlgebraStage INSTANCE = new AlgebraStage();

    @Contract(pure = true)
    @NotNull
    public static AlgebraStage getInstance() {
        return INSTANCE;
    }
    private AlgebraStage() {}
    @Override
    public void enter() {
        System.out.println(LanguageManager.getInstance().getString("algebra.stage.enter"));
        System.out.print(LanguageManager.getInstance().getString("algebra.stage.input"));
        while (Main.scanner.hasNext()) {
            String input = Main.scanner.nextLine();
            if(input.equals(LanguageManager.getInstance().getString("algebra.stage.key.back"))) {
                StageManager.getInstance().back();
                return;
            }
            MathExpression expression = new MathExpression(input);
            Map<Character, BigDecimal> variables = new HashMap<>();
            for (Character variable : expression.getVariables()) {
                System.out.print(variable + " = ");
                String value = Main.scanner.nextLine();
                variables.put(variable, new BigDecimal(value));
                System.out.println();
            }
            System.out.print(LanguageManager.getInstance().getString("algebra.stage.result"));
            System.out.println(expression.substituteAndWorkOut(variables).toMathResult().getValue());
            System.out.print(LanguageManager.getInstance().getString("algebra.stage.input"));
        }
    }
}
