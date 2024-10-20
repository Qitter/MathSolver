package org.qitter.ui.stage;

import org.qitter.Main;
import org.qitter.language.LanguageManager;
import org.qitter.ui.StageManager;
import org.qitter.ui.stage.function.FunctionMenuStage;

import java.util.Map;

public class MenuStage extends SwitchStage {
private static final MenuStage INSTANCE = new MenuStage();
    public static MenuStage getInstance() {
        return INSTANCE;
    }
    public static final String NUMERICAL_STAGE = "1",
            ALGEBRA_STAGE = "2",
            EQUATION_STAGE = "3",
            FUNCTION_MENU = "4",
            HELP_STAGE = "5";
    private MenuStage() {
        super(Map.of(
                NUMERICAL_STAGE,()-> StageManager.getInstance().pushStage(NumericalStage.getInstance()),
                ALGEBRA_STAGE,()-> StageManager.getInstance().pushStage(AlgebraStage.getInstance()),
                EQUATION_STAGE,()-> StageManager.getInstance().pushStage(EquationStage.getInstance()),
                FUNCTION_MENU,()-> StageManager.getInstance().pushStage(FunctionMenuStage.getInstance()),
                HELP_STAGE,()-> StageManager.getInstance().pushStage(HelpStage.getInstance())
        ),()-> StageManager.getInstance().back());
    }
    @Override
    public void enter() {
        System.out.println(LanguageManager.getInstance().getString("menu.main"));
        System.out.println(NUMERICAL_STAGE + ":" + LanguageManager.getInstance().getString("menu.math.numeral"));
        System.out.println(ALGEBRA_STAGE + ":" + LanguageManager.getInstance().getString("menu.math.algebra"));
        System.out.println(EQUATION_STAGE + ":" + LanguageManager.getInstance().getString("menu.math.equation"));
        System.out.println(FUNCTION_MENU + ":" + LanguageManager.getInstance().getString("menu.math.function"));
        System.out.println(HELP_STAGE + ":" + LanguageManager.getInstance().getString("menu.help"));
        System.out.println(LanguageManager.getInstance().getString("menu.exit"));
        String input = Main.scanner.nextLine();
        super.applyRunnable(input);
    }
}
