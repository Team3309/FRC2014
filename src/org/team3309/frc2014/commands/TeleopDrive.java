/*
 * Copyright (c) 2014. FRC Team 3309 All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.team3309.frc2014.commands;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.command.Command;
import org.team3309.frc2014.ControlBoard;
import org.team3309.frc2014.Sensors;
import org.team3309.frc2014.subsystems.Drive;
import org.team3309.friarlib.constants.Constant;

/**
 * This is the Command that allows the driver to drive using an {@link org.team3309.friarlib.XboxController}
 * <p/>
 * It implements a unique style of control where if the driver is only using the right stick,
 * it runs using a tank drive style where the right stick controls throttle and turn amount.
 * If the driver is using both sticks, the left stick is strafing and the right stick is turn amount
 *
 * @author vmagro
 */
public class TeleopDrive extends Command {

    private Constant configLeftStickDeadband = new Constant("control.left_deadband", .1);
    private Constant configTriggerDeadband = new Constant("control.trigger_deadband", .1);
    private Constant configAutoRotateP = new Constant("control.ar.p", .01);

    private static TeleopDrive instance;

    public static TeleopDrive getInstance() {
        if (instance == null)
            instance = new TeleopDrive();
        return instance;
    }

    private Drive drive;
    private ControlBoard controls;

    private TeleopDrive() {
        drive = Drive.getInstance();
        controls = ControlBoard.getInstance();
        requires(drive);
    }

    protected void initialize() {
    }

    protected void execute() {
        double leftX = controls.driver.getLeftX();
        double leftY = controls.driver.getLeftY();
        double rightX = controls.driver.getRightX();
        double rightY = controls.driver.getRightY();

        if (controls.driver.getAButton())
            Sensors.gyro.reset();

        if (controls.driver.getXButton())
            drive.brake();
        else if (controls.driver.getYButton())
            drive.releaseBrake();

        if (controls.driver.getLeftBumper())
            drive.enableMecanum();
        else if (controls.driver.getRightBumper())
            drive.disableMecanum();

        // the mecanum wheels are engaged
        if (drive.isMecanum()) {
            System.out.println("Mecanum");
            // not using the left stick, switch to "tank" mode
            if (Math.abs(leftX) <= configLeftStickDeadband.getDouble() && Math.abs(leftY) <= configLeftStickDeadband
                    .getDouble() && (rightX > .1 || rightY > .1)) {
                System.out.println("mecanum but using as tank");
                drive.driveTank(rightY, rightX);
            } else {
                //if the driver is holding down the trigger, turn off the auto-rotate feature and use strict translation
                if (controls.driver.getRightTrigger() > configTriggerDeadband.getDouble()) {
                    drive.driveMecanum(leftX, leftY, rightX);
                }
                //use the "Halo-AR" drive scheme described by Ether at http://www.chiefdelphi.com/media/papers/2390 and http://www.chiefdelphi.com/forums/showpost.php?p=1021821&postcount=8
                //this will automatically rotate the drive base to match the commanded angle as it translates
                else {
                    double commandAngle = MathUtils.atan2(leftX, leftY) * (180 / Math.PI);
                    double angleError = commandAngle - drive.getGyroAngle();
                    angleError %= 180;
                    double turnOutput = configAutoRotateP.getDouble() * angleError;
                    drive.driveMecanum(leftX, leftY, turnOutput);

                    //drive.driveMecanum(leftX, leftY, rightX);
                }
            }
        }
        // high traction wheels engaged
        else {
            System.out.println("Tank");
            drive.driveTank(leftY, rightX);
        }
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {

    }

    protected void interrupted() {

    }
}
