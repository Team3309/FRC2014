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

package org.team3309.frc2014.commands.autonomous.kinect;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Command;
import org.team3309.frc2014.ControlBoard;
import org.team3309.frc2014.Sensors;
import org.team3309.frc2014.commands.SwitchMecanum;
import org.team3309.frc2014.subsystems.Catapult;
import org.team3309.frc2014.subsystems.Drive;
import org.team3309.friarlib.XboxController;

/**
 * Created by vincente on 3/27/14.
 */
public class HulaDrive extends Command {
    Kinect  kinect;

    private boolean finished;
    private final double ANGLE_THRESH   = -.4;
    private final double GYRO_THRESH    = .3;
    public HulaDrive(){
        requires(Drive.getInstance());
        requires(Catapult.getInstance());
        Drive.getInstance().enableMecanum();
        Sensors.gyro.reset();

        kinect      = Kinect.getInstance();
        finished    = false;
    }

    protected void initialize() {
    }

    protected void execute() {
        if (!Drive.getInstance().isMecanum())
            Drive.getInstance().enableMecanum();
        Skeleton driver = kinect.getSkeleton();
        Skeleton.Joint rightHand = driver.GetHandRight();
        Skeleton.Joint leftHand = driver.GetHandLeft();
        Skeleton.Joint leftWrist = driver.GetWristLeft();
        Skeleton.Joint rightWrist = driver.GetWristRight();

        double throttle = .8;
        double twist = 0;

        //If the angle is above the threshold, lets spin the bot back.
        if (Sensors.gyro.getAngle() < -GYRO_THRESH || Sensors.gyro.getAngle() > GYRO_THRESH)
            twist = -Sensors.gyro.getAngle();

        /*TESTING*/

        /*********/

        //this will require the drive to raise their hands roughly vertical to get it to shoot
        if (leftWrist.getY() < ANGLE_THRESH && rightWrist.getY() < ANGLE_THRESH) {
            Catapult.getInstance().unlatch();
            finished = true;
        }
        else if(rightHand.getX() < 0 && leftHand.getX() < 0)
            throttle = -throttle;

        else if(rightHand.getX() > 0 && leftHand.getX() > 0)
            throttle = throttle;

        //We want to stay stopped
        else {
            throttle = 0;
            /*TESTING
            //Fix gyro angle if we are off, just in case we want to shoot.
            if (Sensors.gyro.getAngle() <= -GYRO_THRESH)
                twist = .6;
            else if (Sensors.gyro.getAngle() >= GYRO_THRESH)
                twist = -.6;
            */
        }

        Drive.getInstance().driveMecanum(throttle, 0, twist);

        System.out.println("Right X: " + rightHand.getX());
        System.out.println("Left  X: " + leftHand.getX());
        System.out.println("Right Y: " + rightWrist.getY());
        System.out.println("Left  Y: " + leftWrist.getY());
        System.out.println("Gyro   : " + Sensors.gyro.getAngle());
    }

    protected boolean isFinished() {
        return finished || !DriverStation.getInstance().isAutonomous();
    }

    protected void end() {

    }

    protected void interrupted() {

    }
}
