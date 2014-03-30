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
import edu.wpi.first.wpilibj.command.Command;
import org.team3309.frc2014.commands.catapult.Shoot;
import org.team3309.frc2014.subsystems.Catapult;

/**
 * Created by vmagro on 3/24/14.
 */
public class ShootWithKinect extends Command {

    private static final double ANGLE_THRESH = .7;

    private boolean finished = false;

    private Kinect kinect = null;

    public ShootWithKinect() {
        requires(Catapult.getInstance());

        kinect = Kinect.getInstance();
    }

    protected void initialize() {

    }

    protected void execute() {
        Skeleton skeleton = kinect.getSkeleton();
        Skeleton.Joint leftHand = skeleton.GetHandLeft();
        Skeleton.Joint rightHand = skeleton.GetHandRight();
        Skeleton.Joint center = skeleton.GetHipCenter();

        double left = (leftHand.getY() + 1) - (center.getY() + 1);
        double right = (rightHand.getY() + 1) - (center.getY() + 1);

        System.out.println("left:" + left + "\tright:" + right);

        //this will require the drive to raise their hands roughly vertical to get it to shoot
        if (left > ANGLE_THRESH && right > ANGLE_THRESH) {
            new Shoot().start();
            finished = true;
        }
    }

    protected boolean isFinished() {
        return finished || !DriverStation.getInstance().isAutonomous();
    }

    protected void end() {

    }

    protected void interrupted() {

    }
}
