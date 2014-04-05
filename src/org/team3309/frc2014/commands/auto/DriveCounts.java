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

package org.team3309.frc2014.commands.auto;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.PIDCommand;
import org.team3309.frc2014.subsystems.Drive;

/**
 * Created by vmagro on 4/4/14.
 */
public class DriveCounts extends PIDCommand {

    private Drive drive;
    private Timer doneTimer = new Timer();

    public DriveCounts(int counts) {
        super(.002, 0, 0);
        drive = Drive.getInstance();

        requires(drive);

        drive.resetEncoders();
        setSetpoint(counts);

        setTimeout(5);
    }

    protected void initialize() {
        drive.resetEncoders();
    }

    protected void execute() {
        System.out.println("Encoder error: " + Math.abs(drive.getAverageCount() - getSetpoint()));
        drive.printEncoders();
        if (Math.abs(drive.getAverageCount() - getSetpoint()) < 10) {
            doneTimer.start();
        } else {
            doneTimer.stop();
            doneTimer.reset();
        }
    }

    protected boolean isFinished() {
        return isTimedOut() || (doneTimer.get() > 500000); //.5 seconds
    }

    protected void end() {

    }

    protected void interrupted() {

    }

    protected double returnPIDInput() {
        return drive.getAverageCount();
    }

    protected void usePIDOutput(double v) {
        drive.driveMecanum(0, v, 0);
    }


    public void setTimeouts(double seconds) {
        super.setTimeout(seconds);
    }
}
