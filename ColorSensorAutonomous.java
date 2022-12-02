package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

//making this an autonomous code, not a TeleOP
@Autonomous(name = "ColorSensorAutonomous", group = "Robot")
public class ColorSensorAutonomous extends LinearOpMode {

    //initializing color to an object of color
    ColorSensor color;

    //initializing motors
    private DcMotor frontRight = null;
    private DcMotor frontLeft = null;
    private DcMotor backRight = null;
    private DcMotor backLeft = null;

    //runtime gives us values of how long things run
    private ElapsedTime runtime = new ElapsedTime();

    //constants for driving motors
    //for the counts per motor rev, i found 560 in the documentation for our specific motors we use
    //    (Rev Robotics HD Hex Motor UltraPlanetary Motor (Gear ratio 20:1))
    static final double     COUNTS_PER_MOTOR_REV    = 560 ;
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;
    static final double     WHEEL_DIAMETER_INCHES   = 2.95276 ;
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * Math.PI);
    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED              = 0.5;

    //runOpMode is a previously defined method, so this is overriding it
    @Override
    //the part that actually runs stuff
    public void runOpMode() {

        //mapping the color sensor
        color = hardwareMap.get(ColorSensor.class, "color");

        //mapping the motors (i don't know the actual names so that can be changed later)
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
        telemetry.addData("Status", "Ready to run");
        telemetry.update();

        //waiting for the run button to be hit
        waitForStart();


        //the exact inches are off but the overall idea here is to move forward to the cone and then
        //   strafe to the right because i was planning on putting the color sensor in the front
        //   left corner so it doesn't come close to causing any issues with the lift mechanism

        encoderDrive(DRIVE_SPEED, 24, 24, 24, 24);
        encoderDrive(TURN_SPEED, 8.5, -8.5, -8.5, 8.5);

        //shows the color values that color sensor can see
        //so in theory, this would show the color values that the sensor sees at the time it was read
        //   this can be used to see what should be in those if statements below cuz idk where 20 came from
        telemetry.addData("red", color.red());
        telemetry.addData("green", color.blue());
        telemetry.addData("blue", color.green());
        telemetry.update();

        //booleans to choose a path
        boolean r = false;
        boolean b = false;
        boolean g = false;

        //figuring out what color is in front of it and setting the correct boolean to true
        //i am completely unsure what unit it is measured in so idk what 20 means??
        if (color.red() > 20) {
            r = true;
        } else if (color.blue() > 20){
            b = true;
        } else if (color.green() > 20){
            g = true;
        }

        //from here, do a specific parking based on what color was determined to be in front (above code)
        //for right now, im just printing out in the app what path it chose to see if it got the right one
        if (r){
            telemetry.addLine("The color detected was red.");
        } else if (g) {
            telemetry.addLine("The color detected was green.");
        } else if (b) {
            telemetry.addLine("The color detected was blue.");
        }

    }

    //this is trying to code the program for moving using encoders
    //remember, this all only works in theory and has not been tested
    public void encoderDrive(double speed, double fleftInches, double bleftInches,
                             double frightInches, double brightInches) {
        //initializing variables
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

            //setting the power of the motors to get it to move
            frontRight.setPower(Math.abs(speed));
            frontLeft.setPower(Math.abs(speed));
            backRight.setPower(Math.abs(speed));
            backLeft.setPower(Math.abs(speed));

            //making sure it does stop until it reaches
            while (opModeIsActive() && (frontRight.isBusy() || frontLeft.isBusy() || backRight.isBusy() || backLeft.isBusy())){
                telemetry.addLine("Moving theoretically");
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
    public void encoderDrive(double angle) {
        //intializing variables
        int newFRTarget;
        int newFLTarget;
        int newBRTarget;
        int newBLTarget;

        //declaring speed to turn speed
        double speed = TURN_SPEED;

        //making sure this should actually be running
        if (opModeIsActive()) {
            //determining the amount the wheels need to move
            double frightInches = (Math.sin(angle)*17);
            double fleftInches = -(Math.sin(angle)*17);
            double brightInches = (Math.sin(angle)*17);
            double bleftInches = -(Math.sin(angle)*17);

            //setting target position
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

            //setting the power of the motors to get it to move
            frontRight.setPower(Math.abs(speed));
            frontLeft.setPower(Math.abs(speed));
            backRight.setPower(Math.abs(speed));
            backLeft.setPower(Math.abs(speed));

            //making sure it does stop until it reaches
            while (opModeIsActive() && (frontRight.isBusy() || frontLeft.isBusy() || backRight.isBusy() || backLeft.isBusy())){
                telemetry.addLine("Moving theoretically");
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