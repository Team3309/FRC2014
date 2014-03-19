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

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.team3309.frc2014.OctanumModule;
import org.team3309.frc2014.Sensors;
import org.team3309.friarlib.FriarGyro;
import org.team3309.friarlib.constants.Constant;

/**
 * Drivetrain subsystem
 *
 * @author vmagro
 */
public class Drive extends Subsystem {

    //first number is port A
    private Constant configMecanumSolenoidPorts = new Constant("drive.solenoid", new double[]{6, 5});
    private Constant configMecanumSolenoidModule = new Constant("drive.solenoid.module", 2);

    private Constant configLeftFrontPort = new Constant("drive.left.front", 1);
    private Constant configLeftBackPort = new Constant("drive.left.back", 2);
    private Constant configFrontRightPort = new Constant("drive.right.front", 3);
    private Constant configRightBackPort = new Constant("drive.right.back", 4);

    private Constant configLeftFrontEncoderA = new Constant("drive.encoder.left.front.a", 8);
    private Constant configLeftFrontEncoderB = new Constant("drive.encoder.left.front.b", 9);
    private Constant configLeftBackEncoderA = new Constant("drive.encoder.left.back.a", 4);
    private Constant configLeftBackEncoderB = new Constant("drive.encoder.left.back.b", 5);
    private Constant configRightFrontEncoderA = new Constant("drive.encoder.right.front.a", 6);
    private Constant configRightFrontEncoderB = new Constant("drive.encoder.right.front.b", 7);
    private Constant configRightBackEncoderA = new Constant("drive.encoder.right.back.a", 2);
    private Constant configRightBackEncoderB = new Constant("drive.encoder.right.back.b", 3);

    private Constant skimGain = new Constant("drive.skim_gain", .25);

    private Constant gyroKp = new Constant("drive.gyro.kp", .01);
    private Constant maxAngularVelocity = new Constant("drive.gyro.max_angular_velocity", 720);

    private static Drive instance;
    private boolean wasInMecanumBeforeBrake = false;
    private boolean brake = false;

    /**
     * Get the singleton instance of the drivetrain
     *
     * @return
     */
    public static Drive getInstance() {
        if (instance == null) {
            instance = new Drive();
        }
        return instance;
    }

    private DoubleSolenoid extender;

    private boolean isMecanum = true;

    private OctanumModule leftFront, leftBack, rightFront, rightBack;

    private FriarGyro gyro;

    private Drive() {
        extender = new DoubleSolenoid(configMecanumSolenoidModule.getInt(), configMecanumSolenoidPorts.getIntList()[0],
                configMecanumSolenoidPorts.getIntList()[1]);

        leftFront = new OctanumModule("leftFront", new Victor(configLeftFrontPort.getInt()), extender,
                new Encoder(configLeftFrontEncoderA.getInt(), configLeftFrontEncoderB.getInt()));
        leftBack = new OctanumModule("leftBack", new Victor(configLeftBackPort.getInt()), extender,
                new Encoder(configLeftBackEncoderA.getInt(), configLeftBackEncoderB.getInt()));
        rightFront = new OctanumModule("rightFront", new Victor(configFrontRightPort.getInt()), extender,
                new Encoder(configRightFrontEncoderA.getInt(), configRightFrontEncoderB.getInt()));
        rightBack = new OctanumModule("rightBack", new Victor(configRightBackPort.getInt()), extender,
                new Encoder(configRightBackEncoderA.getInt(), configRightBackEncoderB.getInt()));

        gyro = Sensors.gyro;
    }

    protected void initDefaultCommand() {

    }

    /**
     * Engage the mecanum wheels
     */
    public void enableMecanum() {
        if (!isMecanum) {
            leftFront.engageMecanum();
            leftBack.engageMecanum();
            rightFront.engageMecanum();
            rightBack.engageMecanum();

            isMecanum = true;
        }
    }

    /**
     * Disengage the mecanum wheels
     */
    public void disableMecanum() {
        if (isMecanum) {
            leftFront.disengageMecanum();
            leftBack.disengageMecanum();
            rightFront.disengageMecanum();
            rightBack.disengageMecanum();

            isMecanum = false;
        }
    }

    /**
     * Is the drivetrain in mecanum mode?
     *
     * @return
     */
    public boolean isMecanum() {
        return isMecanum;
    }

    private void setLeft(double x) {
        leftFront.set(-x);
        leftBack.set(-x);
    }

    private void setRight(double x) {
        rightFront.set(x);
        rightBack.set(x);
    }

    /**
     * Drive using the automatic method determined by the current mode of the drivetrain
     *
     * @param x
     * @param y
     * @param turn
     */
    public void driveAuto(double x, double y, double turn) {
        if (isMecanum()) {
            driveMecanum(x, y, turn);
        } else {
            driveTank(y, turn);
        }
    }

    /**
     * Drive using mecanum mode
     * Based on code from WPILibJ
     *
     * @param x
     * @param y
     * @param turn
     */
    public void driveMecanum(double x, double y, double turn) {
        // Compensate for gyro angle.
        /*double rotated[] = rotateVector(x, y, getGyroAngle());
        x = rotated[0];
        y = rotated[1];*/

        double[] speeds = new double[4];
        speeds[0] = x + y + turn; //left front
        speeds[1] = -x + y + turn; //left back
        speeds[2] = -x + y - turn; //right front
        speeds[3] = x + y - turn; //right back

        //invert left outputs
        speeds[0] = -speeds[0];
        speeds[1] = -speeds[1];

        normalize(speeds);

        leftFront.set(speeds[0]);
        leftBack.set(speeds[1]);
        rightFront.set(speeds[2]);
        rightBack.set(speeds[3]);
    }

    /**
     * Drive using tank drive mode (with high traction wheels)
     *
     * @param throttle
     * @param turn
     */
    public void driveTank(double throttle, double turn) {
        double desiredAngularVelocity = turn * maxAngularVelocity.getDouble();
        double angularVelocity = getAngularVelocity();

        //proportional correction
        turn = (desiredAngularVelocity - angularVelocity) * gyroKp.getDouble();

        double t_left = throttle + turn;
        double t_right = throttle - turn;

        double left = t_left + skim(t_right);
        double right = t_right + skim(t_left);

        if (left > 1)
            left = 1;
        else if (left < -1)
            left = -1;
        if (right > 1)
            right = 1;
        else if (right < -1)
            right = -1;

        setLeft(left);
        setRight(right);
    }

    public double getAngularVelocity() {
        return gyro.getAngularVelocity();
    }

    public double getGyroAngle() {
        return gyro.getAngle();
    }

    public void brake() {
        brake = true;
        if (isMecanum()) {
            disableMecanum();
            wasInMecanumBeforeBrake = true;
        }
        leftBack.brake();
        leftFront.brake();
        rightBack.brake();
        rightFront.brake();
    }

    public void releaseBrake() {
        brake = false;
        if (wasInMecanumBeforeBrake) {
            enableMecanum();
            wasInMecanumBeforeBrake = false;
        }
        leftBack.releaseBrake();
        leftFront.releaseBrake();
        rightBack.releaseBrake();
        rightFront.releaseBrake();
    }

    public boolean isBrake() {
        return brake;
    }

    /**
     * Normalize a set of input values between -1.0 and 1.0, with the largest value being 1.0
     *
     * @param vals
     */
    private static void normalize(double[] vals) {
        double max = 0;
        for (int i = 0; i < vals.length; i++) {
            if (Math.abs(vals[i]) > max)
                max = Math.abs(vals[i]);
        }
        if (max > 1) {
            for (int i = 0; i < vals.length; i++) {
                vals[i] = vals[i] / max;
            }
        }
    }

    /**
     * Rotate a vector in Cartesian space.
     */
    private static double[] rotateVector(double x, double y, double angle) {
        double cosA = Math.cos(angle * (3.14159 / 180.0));
        double sinA = Math.sin(angle * (3.14159 / 180.0));
        double out[] = new double[2];
        out[0] = x * cosA - y * sinA;
        out[1] = x * sinA + y * cosA;
        return out;
    }

    /**
     * This is used internally to make it easier to turn when driving at high
     * speeds
     *
     * @param v
     * @return
     */
    private double skim(double v) {
        // gain determines how much to skim off the top
        if (v > 1.0) {
            return -((v - 1.0) * skimGain.getDouble());
        } else if (v < -1.0) {
            return -((v + 1.0) * skimGain.getDouble());
        }
        return 0;
    }

    public void printEncoders() {
        System.out.println("fl:" + leftFront.getEncoder().get() + "\tfr:" + rightFront.getEncoder().get()
                + "\tbl:" + leftBack
                .getEncoder().get() + "\tbr:" + rightBack.getEncoder().get());
    }

    public int leftFrontCount() {
        return leftFront.getEncoder().get();
    }

    public int rightFrontCount() {
        return rightFront.getEncoder().get();
    }

    public int leftBackCount() {
        return leftBack.getEncoder().get();
    }

    public int rightBackCount() {
        return rightBack.getEncoder().get();
    }
}
