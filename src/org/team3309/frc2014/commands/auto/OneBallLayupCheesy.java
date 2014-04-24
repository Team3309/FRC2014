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

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import org.team3309.frc2014.commands.catapult.ShootAndRetract;
import org.team3309.frc2014.commands.drive.MecDriveForwardTime;
import org.team3309.frc2014.commands.drive.SwitchMecanum;
import org.team3309.frc2014.commands.intake.ExtendIntake;
import org.team3309.frc2014.commands.intake.ExtendPocketPiston;
import org.team3309.frc2014.subsystems.Drive;

/**
 * Created by vmagro on 4/23/14.
 */
public class OneBallLayupCheesy extends CommandGroup {

    public OneBallLayupCheesy() {
        addSequential(new SwitchMecanum(true));
        addSequential(new MecDriveForwardTime(2.25));
        addParallel(new Command() {
            private boolean finished = false;

            protected void initialize() {

            }

            protected void execute() {
                Drive.getInstance().driveTank(.25, 0);
                finished = true;
            }

            protected boolean isFinished() {
                return finished;
            }

            protected void end() {

            }

            protected void interrupted() {

            }
        });
        addSequential(new ExtendIntake());
        addSequential(new WaitCommand(1));
        addSequential(new ExtendPocketPiston());
        addSequential(new WaitForCheesy(4000));
        addSequential(new ShootAndRetract());
    }
}
