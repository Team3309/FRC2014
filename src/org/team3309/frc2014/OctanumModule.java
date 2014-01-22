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

package org.team3309.frc2014;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import org.team3309.friarlib.constants.Constant;

/**
 * This class abstracts each OctanumModule to make the code for {@link org.team3309.frc2014.subsystems.Drive} simpler.
 *
 * @author vmagro
 */
public class OctanumModule implements Runnable {

    private static Constant configMecanumSolenoid = new Constant("drive.mecanum.on", true);

    private static final int DELTA_T = 20;

    private Constant configP = new Constant("drive.p", 1);
    private Constant configI = new Constant("drive.i", 0);
    private Constant configD = new Constant("drive.d", 0);

    private SpeedController motor;
    private Encoder encoder;
    private Solenoid solenoid;

    private boolean enabled = false;

    private double setpoint = 0;
    private double integral = 0;
    private double lastRate = 0;

    /**
     * Create a new OctanumModule with the given motor controller, solenoid and encoder
     *
     * @param motor
     * @param solenoid
     * @param encoder
     */
    public OctanumModule(SpeedController motor, Solenoid solenoid, Encoder encoder) {
        this.motor = motor;
        this.encoder = encoder;
        this.solenoid = solenoid;
    }

    public void engageMecanum() {
        solenoid.set(configMecanumSolenoid.getBoolean());
    }

    public void disengageMecanum() {
        solenoid.set(!configMecanumSolenoid.getBoolean());
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }

    public void set(double x) {
        motor.set(x);
    }

    public void run() {
        while (true) {
            if (enabled) {
                double current = encoder.getRate();
                double err = current - setpoint;
                integral += err;

                double output = err * configP.getDouble() + integral * configI.getDouble() + ((current - lastRate) / DELTA_T) * configD.getDouble();
                set(output);
            }

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
