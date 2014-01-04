package org.team3309.frc2014.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.team3309.friarlib.constants.Constant;

/**
 * Created by vmagro on 1/4/14.
 */
public class Drive extends Subsystem {

    private Constant configMecanumSolenoid = new Constant("drive.mecanum", true);
    private Constant configMecanumSolenoidPort = new Constant("solenoid.mecanum", 1);

    private Solenoid extender;

    private boolean isMecanum = false;

    private Drive instance;

    private Drive() {
        extender = new Solenoid(configMecanumSolenoidPort.getInt());
    }

    public Drive getInstance() {
        if (instance == null) {
            instance = new Drive();
        }
        return instance;
    }

    protected void initDefaultCommand() {

    }

    public void enableMecanum() {
        isMecanum = true;
        extender.set(configMecanumSolenoid.getBoolean());
    }

    public void disableMecanum() {
        isMecanum = false;
        extender.set(!configMecanumSolenoid.getBoolean());
    }

    public boolean isMecanum() {
        return isMecanum;
    }

    public void drive(double x, double y, double turn) {

    }

    public void drive(double throttle, double turn) {
        drive(0, throttle, turn);
    }
}
