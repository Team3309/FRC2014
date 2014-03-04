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
import org.team3309.frc2014.subsystems.Catapult;

/**
 * Command to prepare a shot, not actually shoot
 *
 * @author vmagro
 */
public class PrepShot extends Command {

    private long startTime = 0;

    public PrepShot() {
        requires(Catapult.getInstance());
    }

    protected void initialize() {
        Catapult.getInstance().latch();
        startTime = System.currentTimeMillis();
        Catapult.getInstance().engageWinch();
        Catapult.getInstance().set(.5);
    }

    protected void execute() {
        Catapult.getInstance().set(.5);
    }

    protected boolean isFinished() {
        //return (System.currentTimeMillis() - startTime) > 1000 || Catapult.getInstance().isFullBack();
        return Catapult.getInstance().isFullBack();
    }

    protected void end() {
        Catapult.getInstance().set(0);
        Catapult.getInstance().disengageWinch();
    }

    protected void interrupted() {
        Catapult.getInstance().set(0);
    }
}
