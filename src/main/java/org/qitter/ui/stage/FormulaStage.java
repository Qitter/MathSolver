package org.qitter.ui.stage;

import org.qitter.FormulaProblem;
import org.qitter.Main;
import org.qitter.ui.StageManager;

public class FormulaStage extends Stage{
    private static final FormulaStage INSTANCE = new FormulaStage();
    public static FormulaStage getInstance() {
        return INSTANCE;
    }
    private FormulaStage() {}
    @Override
    public void enter() {
        System.out.println("输入\"退出\"即可返回");
        System.out.print("请输入表达式: ");
        while (Main.scanner.hasNext()) {
            String expression = Main.scanner.nextLine();
            if (expression.equals("退出")) {
                StageManager.getInstance().back();
                return;
            }
            System.out.println("结果是:" + new FormulaProblem(expression).solve());
            System.out.print("请输入表达式: ");
        }
    }

    @Override
    public void exit() {

    }
}
