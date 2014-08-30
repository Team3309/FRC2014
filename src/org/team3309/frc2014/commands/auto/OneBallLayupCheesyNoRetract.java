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

import com.team254.lib.CheesyVisionServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import org.team3309.frc2014.Sensors;
import org.team3309.frc2014.commands.catapult.Shoot;
import org.team3309.frc2014.subsystems.Drive;
import org.team3309.frc2014.subsystems.Intake;

/**
 * Created by vmagro on 4/23/14.
 */
public class OneBallLayupCheesyNoRetract extends Command {

    private static final int STATE_DRIVING = 0;
    private static final int STATE_DONE_DRIVING = 1;
    private static final int STATE_WAITING_FOR_SHOT = 2;
    private static final int STATE_DONE = 3;

    private Drive drive;

    private int state = 0;
    private Timer stateTimer = new Timer();
    private CheesyVisionServer cv;

    public OneBallLayupCheesyNoRetract() {
        drive = Drive.getInstance();
        requires(drive);
        cv = CheesyVisionServer.getInstance();
    }

    protected void initialize() {
        state = STATE_DRIVING;
        stateTimer.start();
    }

    protected void execute() {
        if (state == STATE_DRIVING) {
            drive.enableMecanum();
            double turn = -.01 * Sensors.gyro.getAngle();
            double right = .01 * (Sensors.leftSonar.getInches() - 8);
            drive.driveMecanum(right, .7, turn);
            System.out.println("timer: " + stateTimer.get());
            if (stateTimer.get() > 2.25) { //2.25 seconds
                state = STATE_DONE_DRIVING;
                stateTimer.reset();
            }
        } else if (state == STATE_DONE_DRIVING) {
            double turn = -.01 * Sensors.gyro.getAngle();
            double right = .01 * (Sensors.leftSonar.getInches() - 8);
            drive.driveMecanum(right, .5, turn);
            Intake.getInstance().extend();
            Timer.delay(1);
            Intake.getInstance().extendPocket();
            Timer.delay(1);
            state = STATE_WAITING_FOR_SHOT;
            stateTimer.reset();
        } else if (state == STATE_WAITING_FOR_SHOT) {
            double turn = -.01 * Sensors.gyro.getAngle();
            drive.driveMecanum(0, .5, turn);
            if ((!cv.getLeftStatus() || !cv.getRightStatus()) || stateTimer.get() > 3) {
                new Shoot().start();
                state = STATE_DONE;
            }
        } else if (state == STATE_DONE) {
            drive.driveTank(0, 0);
        }
    }

    protected boolean isFinished() {
        return !DriverStation.getInstance().isAutonomous();
    }

    protected void end() {

    }

    protected void interrupted() {

    }
}
