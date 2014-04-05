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

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Kinect;
import edu.wpi.first.wpilibj.Skeleton;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import org.team3309.frc2014.commands.catapult.ShootAndRetract;
import org.team3309.frc2014.subsystems.Drive;
import org.team3309.frc2014.subsystems.Intake;

/**
 * Created by vmagro on 4/1/14.
 */
public class KinectLayup extends Command {

    private static final int STATE_DRIVING = 0;
    private static final int STATE_DONE_DRIVING = 1;
    private static final int STATE_WAITING_FOR_SHOT = 2;
    private static final int STATE_DONE = 3;

    private Drive drive;
    private Kinect kinect;

    private int state = 0;
    private Timer stateTimer = new Timer();

    public KinectLayup() {
        drive = Drive.getInstance();
        requires(drive);

        kinect = Kinect.getInstance();
    }

    protected void initialize() {
        stateTimer.start();
    }

    protected void execute() {
        if (state == STATE_DRIVING) {
            drive.enableMecanum();
            drive.driveTank(.7, 0);
            Timer.delay(2.25);
            state = STATE_DONE_DRIVING;
            stateTimer.reset();
        } else if (state == STATE_DONE_DRIVING) {
            Intake.getInstance().extend();
            Timer.delay(1);
            Intake.getInstance().extendPocket();
            Timer.delay(1);
            state = STATE_WAITING_FOR_SHOT;
            stateTimer.reset();
        } else if (state == STATE_WAITING_FOR_SHOT) {
            drive.driveTank(.3, 0);
            Skeleton skeleton = kinect.getSkeleton();
            Skeleton.Joint leftHand = skeleton.GetHandLeft();
            Skeleton.Joint rightHand = skeleton.GetHandRight();
            Skeleton.Joint head = skeleton.GetHead();

            //this will require the drive to raise their hands roughly vertical to get it to shoot or timeout after 5 seconds
            if ((leftHand.getY() > head.getY() && rightHand.getY() > head.getY()) || stateTimer.get() > 5000000) {
                new ShootAndRetract().start();
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
