package org.team3309.frc2014;

import org.team3309.friarlib.XboxController;
import org.team3309.friarlib.constants.Constant;

/**
 * Created by vmagro on 1/6/14.
 */
public class ControlBoard {

    private Constant configDriverController = new Constant("js.driver", 1);
    private Constant configOperatorController = new Constant("js.operator", 2);

    public XboxController driver = new XboxController(configDriverController.getInt());
    public XboxController operator = new XboxController(configOperatorController.getInt());

    private static ControlBoard instance;

    public static ControlBoard getInstance() {
        if (instance == null)
            instance = new ControlBoard();
        return instance;
    }

    private ControlBoard() {

    }
}
