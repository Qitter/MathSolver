package org.qitter.ui.stage.function;

import org.jetbrains.annotations.NotNull;
import org.qitter.Main;
import org.qitter.language.LanguageManager;
import org.qitter.ui.StageManager;
import org.qitter.ui.stage.SwitchStage;

import java.util.Map;

public class FunctionMenuStage extends SwitchStage {
    @NotNull
    private static final FunctionMenuStage INSTANCE = new FunctionMenuStage();
    @NotNull
    public static FunctionMenuStage getInstance() {
        return INSTANCE;
    }
    public static final String LINEAR_FUNCTION = "1";
    public static final String QUADRATIC_FUNCTION = "2";
    protected FunctionMenuStage() {
        super(Map.of(
                LINEAR_FUNCTION,()-> StageManager.getInstance().pushStage(LinearFunctionStage.getInstance()),
                QUADRATIC_FUNCTION,()-> StageManager.getInstance().pushStage(QuadraticFunctionStage.getInstance())
        ),()-> StageManager.getInstance().back());
    }

    @Override
    public void enter() {
        System.out.println(LanguageManager.getInstance().getString("function.menu.stage.enter"));
        System.out.println(LINEAR_FUNCTION + " : " + LanguageManager.getInstance().getString("function.menu.stage.linear"));
        System.out.println(QUADRATIC_FUNCTION + " : " + LanguageManager.getInstance().getString("function.menu.stage.quadratic"));
        System.out.print(LanguageManager.getInstance().getString("function.menu.select"));
        applyRunnable(Main.scanner.nextLine());
    }
}
