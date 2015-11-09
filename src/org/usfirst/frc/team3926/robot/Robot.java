
package org.usfirst.frc.team3926.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;

public class Robot extends IterativeRobot {
	CANTalon talonSRX_FR; //Front Right
	CANTalon talonSRX_FL; //Front Left
	CANTalon talonSRX_BR; //Back Right
	CANTalon talonSRX_BL; //Back Left
	CANTalon talonLift; //Lift Motor
	RobotDrive driveSystem;
	Joystick leftStick;
	Joystick rightStick;
	Joystick XBox;
	DoubleSolenoid armSolenoid;
	
	int speedSetR = 0;
	int speedSetL = 0;
	
	double lift = 0;
	double leftInput;
	double rightInput;
	
	boolean aClicked = false;
	boolean bClicked = false;
	
    public void robotInit() {
    	talonSRX_FR = new CANTalon(1); //CAN ID (not position in loop)
    	talonSRX_FL = new CANTalon(2);
    	talonSRX_BR = new CANTalon(3);
    	talonSRX_BL = new CANTalon(4);
    	talonLift = new CANTalon(5);
    	
    	driveSystem = new RobotDrive(talonSRX_FL, talonSRX_BL, talonSRX_FR, talonSRX_BR);
    	
    	leftStick = new Joystick(0); //USB 0
    	rightStick = new Joystick(1); //USB 1
    	XBox = new Joystick(2); //USB 2
    	
    	armSolenoid = new DoubleSolenoid(5, 7, 6);
    } //End robotInit()

    public void autonomousPeriodic() {

    } //End autonomousPeriodic()
    
    public void teleopPeriodic() {
    	leftInput = leftStick.getY(); //leftInput = left Y
    	rightInput = rightStick.getY(); //rightInput = right Y
    	lift = XBox.getY(); //lift = XBox's main (left) Y axis
    	
    	if (leftInput > 0) {
    		if ((leftInput - speedSetL) > .1) speedSetL += .1;
    	}
    	else if (leftInput < 0) {
    		double absolute = leftInput * -1;
    		if ((absolute - speedSetL) > .1) speedSetL -= .1;
    	}
    	
    	if (rightInput > 0) {
    		if ((rightInput - speedSetR) > .1) speedSetR += .1;
    	}
    	else if (rightInput < 0) {
    		double absolute = rightInput * -1;
    		if ((absolute - speedSetR) > .1) speedSetR -= .1;
    	}
    	
    	if (XBox.getRawButton(1) && !aClicked) aClicked = true; //If the a button is pressed and it hasn't already been
    	if (XBox.getRawButton(2) && !bClicked) bClicked = true; //If the b button is pressed and it hasn't already been
    	if (XBox.getRawButton(4) || (bClicked && aClicked)) { //If the y button is pressed or aClicked and bClicked are true (That's optional) stop cylander
    		aClicked = false;
    		bClicked = false;
    	}
    		
        if (leftStick.getRawButton(1)) { //Saftey mode
        	speedSetL /= 2;
        	speedSetR /= 2;
        }
        if (rightStick.getRawButton(1)) speedSetL = speedSetR; //Forward mode
        
        driveSystem.tankDrive(speedSetL, speedSetR);
        
        if (lift != 0) talonLift.set(lift); //Left XBox Y
        else talonLift.set(0);
        
        if (aClicked) armSolenoid.set(DoubleSolenoid.Value.kForward); //A button open
        else if (bClicked) armSolenoid.set(DoubleSolenoid.Value.kReverse); //B button close
        else armSolenoid.set(DoubleSolenoid.Value.kOff);
    } //End teleopPeriodic()
} //End Robot
