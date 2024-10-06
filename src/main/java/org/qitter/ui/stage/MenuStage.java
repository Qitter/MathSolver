package org.qitter.ui.stage;

import org.qitter.Main;
import org.qitter.ui.StageManager;

public class MenuStage extends Stage{
private static final MenuStage INSTANCE = new MenuStage();
    public static MenuStage getInstance() {
        return INSTANCE;
    }
    private MenuStage() {}
    @Override
    public void enter() {
        System.out.print("""
                菜单:
                1.计算无未知数的式子
                2.代数式
                3.解方程
                按下任意键退出
                """);
        String input = Main.scanner.nextLine();
        switch (input) {
            case "1" -> StageManager.getInstance().pushStage(FormulaStage.getInstance());
            case "2" -> StageManager.getInstance().pushStage(AlgebraStage.getInstance());
            case "3" -> StageManager.getInstance().pushStage(EquationStage.getInstance());
            default -> StageManager.getInstance().back();
        }
    }

    @Override
    public void exit() {

    }
}
