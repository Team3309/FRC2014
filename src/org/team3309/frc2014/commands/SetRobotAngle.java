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

import edu.wpi.first.wpilibj.command.PIDCommand;
import org.team3309.friarlib.constants.Constant;

/**
 * Created by vmagro on 1/16/14.
 */
public class SetRobotAngle extends PIDCommand {

    private static Constant configKp = new Constant("pid.setrobotangle.p", 1);
    private static Constant configKi = new Constant("pid.setrobotangle.i", 0);
    private static Constant configKd = new Constant("pid.setrobotangle.d", 0);

    private double angle = 0;

    public static SetRobotAngle create(double angle) {
        return new SetRobotAngle(configKp.getDouble(), configKi.getDouble(), configKd.getDouble(), angle);
    }

    public SetRobotAngle(double p, double i, double d, double angle) {
        super(configKp.getDouble(), configKi.getDouble(), configKd.getDouble());
        this.angle = angle;
    }

    protected double returnPIDInput() {
        return 0;
    }

    protected void usePIDOutput(double v) {

    }

    protected void initialize() {

    }

    protected void execute() {

    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {

    }

    protected void interrupted() {

    }

}
