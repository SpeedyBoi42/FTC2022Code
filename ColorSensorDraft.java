package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

//making this an autonomous code, not a TeleOP
@Autonomous(name = "ColorSensorAutonomous", group = "Robot")
public class ColorSensorDraft extends LinearOpMode {

    //prolly initializing color and ColorSensor is the type
    ColorSensor color;

    //initializing motors so i can declare them later (declaring to null just cuz example code im looking at does it)
    private DcMotor frontRight = null;
    private DcMotor frontLeft = null;
    private DcMotor backRight = null;
    private DcMotor backLeft = null;

    //im going to declare this now cuz might have to use it eventually (which actually is used!!)
    private ElapsedTime runtime = new ElapsedTime();

    //constants for driving motors
    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // No External Gearing.
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED              = 0.5;

    //idk what this is
    @Override
    //the part that actually runs stuff i think
    public void runOpMode() {

        //mapping the color sensor and declaring color to it
        color = hardwareMap.get(ColorSensor.class, "color");

        //mapping the motors (i dont know the actual names so that can be changed later)
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");

        //setting directions so its easier to turn motors
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);

        //setting the mode to drive the robot
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        //saying that it's ready to run
        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();

        //waiting for the run button to be hit
        waitForStart();

        //resetting runtime
        runtime.reset();

        //trying to move with encoders :/
        encoderDrive(DRIVE_SPEED, 24, 24, 24, 24, 5.0);
        encoderDrive(TURN_SPEED, 10, -10, -10, 10, 4.0);

        /* NOT USING THIS
        //move forward (w/o encoder)
        while (runtime.seconds() < 1.5){
            frontRight.setPower(0.7);
            frontLeft.setPower(0.7);
            backRight.setPower(0.7);
            backLeft.setPower(0.7);
        }

        //reset runtime again
        runtime.reset();
        //move right
        while (runtime.seconds() < 0.4){
            frontRight.setPower(-0.6);
            frontLeft.setPower(0.6);
            backRight.setPower(0.6);
            backLeft.setPower(-0.6);
        }

         */


        //shows the color values that color sensor can see
        telemetry.addData("red", color.red());
        telemetry.addData("green", color.blue());
        telemetry.addData("blue", color.green());
        telemetry.update();

        //booleans to choose a path
        boolean r = false;
        boolean b = false;
        boolean g = false;

        //figuring out what color is in front of it
        if (color.red() > 20) {
            r = true;
        } else if (color.blue() > 20){
            b = true;
        } else if (color.green() > 20){
            g = true;
        }

        //from here, do a specific parking based on what color was determined to be in front (above code)
        if (r){
            telemetry.addLine("The color detected was red.");
        } else if (g) {
            telemetry.addLine("The color detected was red.");
        } else if (b) {
            telemetry.addLine("The color detected was red.");
        }

    }

    //this is trying to code the program for moving using encoders
    public void encoderDrive(double speed, double fleftInches, double bleftInches,
                             double frightInches, double brightInches, double timeoutS) {
        //intializing variables
        int newFRTarget;
        int newFLTarget;
        int newBRTarget;
        int newBLTarget;

        //making sure this should actually be running
        if (opModeIsActive()) {
            //setting the target position
            newFRTarget = frontRight.getCurrentPosition() + (int)(frightInches * COUNTS_PER_INCH);
            newFLTarget = frontLeft.getCurrentPosition() + (int)(fleftInches * COUNTS_PER_INCH);
            newBRTarget = backRight.getCurrentPosition() + (int)(brightInches * COUNTS_PER_INCH);
            newBLTarget = backLeft.getCurrentPosition() + (int)(bleftInches * COUNTS_PER_INCH);

            frontRight.setTargetPosition(newFRTarget);
            frontLeft.setTargetPosition(newFLTarget);
            backRight.setTargetPosition(newBRTarget);
            backLeft.setTargetPosition(newBLTarget);

            //making the motor mode run to position
            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            //resetting runtime
            runtime.reset();
            //setting the power of the motors to get it to move
            frontRight.setPower(Math.abs(speed));
            frontLeft.setPower(Math.abs(speed));
            backRight.setPower(Math.abs(speed));
            backLeft.setPower(Math.abs(speed));

            //making sure it does stop until it reaches
            while (opModeIsActive() && (runtime.seconds() < timeoutS) &&
                    (frontRight.isBusy() || frontLeft.isBusy() || backRight.isBusy() || backLeft.isBusy())){
                telemetry.addLine("We should be moving, the motors should be turning if you see this.");
                telemetry.update();
            }

            //stopping all movement
            frontRight.setPower(0);
            frontLeft.setPower(0);
            backRight.setPower(0);
            backLeft.setPower(0);

            //changing the motor mode *again*
            frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        }

    }
}