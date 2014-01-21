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

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import org.team3309.frc2014.Vision;

/**
 * Created by vmagro on 1/20/14.
 */
public class WaitForHot extends Command {

    private Side side;

    private boolean isHot = false;

    public WaitForHot(Side side) {
        this.side = side;
    }

    protected void initialize() {
        Vision.VisionTarget[] targets = Vision.getTargets();
        for (int i = 0; i < targets.length; i++) {
            if (side.equals(Side.LEFT) && targets[i].left)
                isHot = targets[i].hot;
            else if (side.equals(Side.RIGHT) && targets[i].right)
                isHot = targets[i].hot;
        }
    }

    protected void execute() {
        if (!isHot) {
            Timer.delay(5);
            isHot = true;
        }
    }

    protected boolean isFinished() {
        return isHot;
    }

    protected void end() {

    }

    protected void interrupted() {

    }

    public static class Side {
        private static final int valLeft = -1;
        private static final int valRight = 1;

        private int val;

        private Side(int val) {
            this.val = val;
        }

        public static final Side LEFT = new Side(valLeft);
        public static final Side RIGHT = new Side(valRight);

        public boolean equals(Object another) {
            if (another instanceof Side)
                return ((Side) another).val == this.val;
            return false;
        }
    }
}
