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
import edu.wpi.first.wpilibj.command.Command;
import org.team3309.frc2014.subsystems.Drive;
import org.team3309.friarlib.filter.MovingAverageFilter;

/**
 * Created by vmagro on 4/23/14.
 */
public class GoalieMode extends Command {

    private CheesyVisionServer cheesyVision;
    private Drive drive;
    private MovingAverageFilter speedFilter = new MovingAverageFilter(10);

    public GoalieMode() {
        cheesyVision = CheesyVisionServer.getInstance();
        drive = Drive.getInstance();
        requires(drive);
    }

    protected void initialize() {
        drive.disableMecanum();
    }

    protected void execute() {
        if (!cheesyVision.getRightStatus())
            speedFilter.update(-.75);
        else if (!cheesyVision.getLeftStatus())
            speedFilter.update(.75);
        else
            speedFilter.update(0);

        drive.driveTank(speedFilter.get(), 0);
    }

    protected boolean isFinished() {
        return !DriverStation.getInstance().isAutonomous();
    }

    protected void end() {
        drive.driveTank(0, 0);
    }

    protected void interrupted() {

    }
}
