package org.team3309.frc2014.commands;

import org.team3309.frc2014.ControlBoard;
import org.team3309.friarlib.constants.Constant;

/**
 * Created by vmagro on 1/6/14.
 */
public class TeleopDrive extends CommandBase {

    private Constant configLeftStickDeadband = new Constant("control.left_deadband", .1);

    private static TeleopDrive instance;

    public static TeleopDrive getInstance() {
        if (instance == null)
            instance = new TeleopDrive();
        return instance;
    }

    private ControlBoard controls;

    private TeleopDrive() {
        controls = ControlBoard.getInstance();
        requires(drive);
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
        double leftX = controls.driver.getLeftX();
        double leftY = controls.driver.getLeftY();

        double rightX = controls.driver.getRightX();
        double rightY = controls.driver.getRightY();

        if (controls.driver.getLeftBumper())
            drive.enableMecanum();
        else if (controls.driver.getRightBumper())
            drive.disableMecanum();

        // the mecanum wheels are engaged
        if (drive.isMecanum()) {
            // not using the left stick, switch to "tank" mode
            if (Math.abs(leftX) <= configLeftStickDeadband.getDouble() && Math.abs(leftY) <= configLeftStickDeadband.getDouble()) {
                drive.driveTank(rightY, rightX);
            } else {
                drive.driveMecanum(leftX, leftY, rightX);
            }
        }
        // high traction wheels engaged
        else {
            drive.driveTank(leftY, rightX);
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {

    }

    @Override
    protected void interrupted() {

    }
}
