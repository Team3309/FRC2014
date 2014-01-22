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
import org.team3309.frc2014.Vision;
import org.team3309.friarlib.constants.Constant;

/**
 * This Command gets the targets from the vision coprocessor and turns the robot to aim at the hot goal.
 * If the hot goal cannot be found, it will pick the first goal it sees.
 * If it doesn't see any goals it turns an angle set by the {@link org.team3309.friarlib.constants.Constant} "cmd.aimathot.default_turn"
 *
 * @author vmagro
 */
public class AimAtHotGoal extends Command {

    private static Constant configDefaultTurn = new Constant("cmd.aimathot.default_turn", 45);

    private Vision.VisionTarget[] targets = null;
    private Vision.VisionTarget hotGoal = null;

    private boolean finished = false;

    protected void initialize() {
        targets = Vision.getTargets();
        for (int i = 0; i < targets.length; i++) {
            if (targets[i].hot)
                hotGoal = targets[i];
        }
        if (hotGoal == null && targets.length > 0)
            hotGoal = targets[0];
    }

    protected void execute() {
        if (hotGoal != null)
            ChangeRobotAngle.create(hotGoal.azimuth).start();
        else
            ChangeRobotAngle.create(configDefaultTurn.getDouble()).start();
        finished = true;
    }

    protected boolean isFinished() {
        return finished;
    }

    protected void end() {

    }

    protected void interrupted() {

    }

}
