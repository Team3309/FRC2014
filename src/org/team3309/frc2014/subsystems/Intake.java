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

package org.team3309.frc2014.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.buttons.Trigger;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.team3309.friarlib.constants.Constant;
import org.team3309.friarlib.motors.MultiSpeedController;

/**
 * Created by vmagro on 1/21/14.
 */
public class Intake extends Subsystem {

    private static Intake instance = null;

    public static Intake getInstance() {
        if (instance == null)
            instance = new Intake();
        return instance;
    }

    private static final Constant configIntakeMotors = new Constant("intake.motors", new double[]{7, 8, 9, 10});
    private static final Constant configBallSensor = new Constant("intake.sensor", 8);
    private static final Constant configSolenoid = new Constant("intake.solenoid", 4);
    private static final Constant configSolenoidOn = new Constant("intake.solenoid.on", true);

    private MultiSpeedController motors;
    private DigitalInput ballSensor;
    private IntakeTrigger trigger;
    private DoubleSolenoid solenoid;

    private Intake() {
        SpeedController[] motorArr = new SpeedController[configIntakeMotors.getList().length];
        for (int i = 0; i < configIntakeMotors.getList().length; i++) {
            motorArr[i] = new Victor((int) configIntakeMotors.getList()[i]);
        }

        motors = new MultiSpeedController.Builder()
                .motors(motorArr)
                .reverse(2)
                .reverse(0)
                .build();

        //ballSensor = new DigitalInput(configBallSensor.getInt());

        trigger = new IntakeTrigger();
        solenoid = new DoubleSolenoid(2, 3, configSolenoid.getInt());
    }

    protected void initDefaultCommand() {

    }

    public void whenBallActive(Command cmd) {
        trigger.whenActive(cmd);
    }

    public void whileBackActive(Command cmd) {
        trigger.whileActive(cmd);
    }

    public void whenBallInactive(Command cmd) {
        trigger.whenInactive(cmd);
    }

    public void set(double val) {
        motors.set(val);
    }

    public void extend() {
        if (configSolenoidOn.getBoolean())
            solenoid.set(DoubleSolenoid.Value.kForward);
        else
            solenoid.set(DoubleSolenoid.Value.kReverse);
    }

    public void retract() {
        if (configSolenoidOn.getBoolean())
            solenoid.set(DoubleSolenoid.Value.kReverse);
        else
            solenoid.set(DoubleSolenoid.Value.kForward);
    }

    public boolean isExtended() {
        if (configSolenoidOn.getBoolean())
            return solenoid.get().equals(DoubleSolenoid.Value.kForward);
        else
            return solenoid.get().equals(DoubleSolenoid.Value.kReverse);
    }

    private class IntakeTrigger extends Trigger {

        public boolean get() {
            return ballSensor.get();
        }
    }
}
