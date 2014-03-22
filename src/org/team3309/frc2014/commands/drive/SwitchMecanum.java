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

package org.team3309.frc2014.commands.drive;

import edu.wpi.first.wpilibj.command.Command;
import org.team3309.frc2014.subsystems.Drive;

/**
 * This Command switches to/from mecanum mode. The constructor determines whether or not the Command will enable or
 * disable mecanum.
 *
 * @author vmagro
 */
public class SwitchMecanum extends Command {

    private boolean enableMecanum;
    private boolean finished = false;

    /**
     * Create a new SwitchMecanum command
     *
     * @param enable true to engage mecanum when run, false to engage high-traction when run
     */
    public SwitchMecanum(boolean enable) {
        this.enableMecanum = enable;
    }

    protected void initialize() {

    }

    protected void execute() {
        if (enableMecanum)
            Drive.getInstance().enableMecanum();
        else
            Drive.getInstance().disableMecanum();

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
