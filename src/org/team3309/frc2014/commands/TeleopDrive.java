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
