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

import edu.wpi.first.wpilibj.*;
import org.team3309.friarlib.constants.Constant;

/**
 * This class abstracts each OctanumModule to make the code for {@link org.team3309.frc2014.subsystems.Drive} simpler.
 *
 * @author vmagro
 */
public class OctanumModule implements PIDOutput, PIDSource {

    private static Constant configMecanumSolenoid = new Constant("drive.mecanum.on", true);

    private static Constant configP = new Constant("drive.p", 1);
    private static Constant configI = new Constant("drive.i", 0);
    private static Constant configD = new Constant("drive.d", 0);

    private static Constant configCountsPerRev = new Constant("drive.counts_per_rev", 250);
    private static Constant configDistancePerPulse = new Constant("drive.distance_per_pulse", 1 / 300);

    private SpeedController motor;
    private Solenoid solenoid;
    private Encoder encoder;

    private PIDController pidController;

    /**
     * Create a new OctanumModule with the given motor controller, solenoid and encoder
     *
     * @param motor    the {@link edu.wpi.first.wpilibj.SpeedController} that runs the wheel
     * @param solenoid the {@link edu.wpi.first.wpilibj.Solenoid} to engage/disengage the mecanum wheel
     * @param encoder  the {@link edu.wpi.first.wpilibj.Encoder} to count revolutions
     */
    public OctanumModule(SpeedController motor, Solenoid solenoid, Encoder encoder) {
        this.motor = motor;
        this.encoder = encoder;
        this.solenoid = solenoid;

        encoder.start();

        pidController = new PIDController(configP.getDouble(), configI.getDouble(), configD.getDouble(), this, this);
    }

    public void enable() {
        pidController.enable();
    }

    public void disable() {
        pidController.disable();
    }

    public void set(double x) {
        motor.set(x);
    }

    public void setPositionSetpoint(double inches) {
        pidController.setSetpoint(inches);
    }

    public void pidWrite(double v) {
        motor.set(v);
    }

    public double pidGet() {
        return encoder.getRate();
    }

    public void engageMecanum() {
        solenoid.set(configMecanumSolenoid.getBoolean());
    }

    public void disengageMecanum() {
        solenoid.set(!configMecanumSolenoid.getBoolean());
    }

}
