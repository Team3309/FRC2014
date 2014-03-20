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

import edu.wpi.first.wpilibj.command.Command;
import org.team3309.frc2014.subsystems.Drive;

/**
 * This command allows the robot to translate using the mecanum method of control.
 *
 * @author vmagro
 */
public class MecTranslate extends Command {

    private static final double kP = 0.0005;

    private Drive drive;
    private int xCounts, yCounts;
    private boolean finishedX, finishedY;
    private boolean onXLeg = false;

    public MecTranslate(int xCounts, int yCounts) {
        drive = Drive.getInstance();
        requires(drive);
        this.xCounts = xCounts;
        this.yCounts = yCounts;

        drive.resetEncoders();
    }

    protected void initialize() {
        drive.enableMecanum();
        //x is the shorter leg
        if (xCounts < yCounts) {
            onXLeg = true;
        }
    }

    protected void execute() {
        if (onXLeg) {
            //average all of the encoder counts
            double actualCounts = (double) (Math.abs(drive.leftBackCount()) + Math.abs(drive.leftFrontCount())
                    + Math.abs(drive.rightBackCount()) + Math.abs(drive.rightFrontCount())) / 4d;
            double err = xCounts - actualCounts;
            drive.driveMecanum(kP * err, 0, 0);
            if (err < 100) {
                System.out.println("Finished X leg");
                finishedX = true;
                onXLeg = false;
                drive.driveMecanum(0, 0, 0);
            }
        } else {
            //average all of the encoder counts
            double actualCounts = (double) (Math.abs(drive.leftBackCount()) + Math.abs(drive.leftFrontCount())
                    + Math.abs(drive.rightBackCount()) + Math.abs(drive.rightFrontCount())) / 4d;
            double err = yCounts - actualCounts;
            drive.driveMecanum(0, kP * err, 0);
            if (err < 100) {
                System.out.println("Finished Y leg");
                finishedY = true;
                drive.driveMecanum(0, 0, 0);
            }
        }
    }

    protected boolean isFinished() {
        return finishedX && finishedY;
    }

    protected void end() {
        drive.driveMecanum(0, 0, 0);
    }

    protected void interrupted() {

    }
}
