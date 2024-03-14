/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.List;

import edu.wpi.first.math.MathUtil;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
// import edu.wpi.first.wpilibj2.command.WaitCommand;

import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.RunCommand;

import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.FieldConstants;
import frc.robot.Constants.OIConstants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.Constants.UltrasonicConstants;
import frc.robot.Constants.VisionConstants.AprilTag;
//import frc.robot.subsystems.AnalogUltrasonicDistanceSubsystem;
import frc.robot.subsystems.ArmRotationSubsystem;
import frc.robot.subsystems.BlackBoxSubsystem;
import frc.robot.subsystems.NoteIntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.WinchSubsystem;
import frc.robot.subsystems.PingResponseUltrasonicSubsystem;
import frc.robot.subsystems.PoseEstimatorSubsystem;
import frc.robot.subsystems.DriveSubsystemSRX;
import frc.robot.subsystems.CameraVisionSubsystem;
import frc.robot.subsystems.ColorDetectionSubsytem;
import frc.robot.commands.ArmHomeCommand;
import frc.robot.commands.DetectColorCommand;
import frc.robot.commands.DriveOnAprilTagProjectionCommand;
import frc.robot.commands.DriveRobotCommand;
import frc.robot.commands.FindAprilTagCommand;
//import com.revrobotics.CANSparkMax;
//import com.revrobotics.CANSparkLowLevel.MotorType;
import frc.robot.commands.GotoAmpCommand;
import frc.robot.commands.GotoPoseCommand;
//import frc.robot.commands.NoteIntakeCommand;
import frc.robot.commands.RotateRobotCommand;
import frc.robot.commands.ScoreNoteInAmp;
import frc.robot.commands.ShooterCommand;
//import frc.robot.commands.ShooterCommand;
//import frc.robot.commands.StopRobotMotion;
import frc.robot.commands.SwerveToAmpCommand;
import frc.robot.commands.SwerveToPoseCommand;
import frc.robot.commands.SwerveToSourceCommand;
//import frc.robot.commands.SwerveToPoseTest;
//import frc.robot.commands.SwerveToPoseTest2;
import frc.robot.commands.SwerveToPoseTest3;
import frc.robot.commands.TakeInNoteCommand;
//import frc.robot.commands.WinchCommand;
import frc.robot.commands.GotoSourceCommand;
//import frc.robot.commands.DetectColorCommand;
//import frc.robot.TrajectoryPlans;




/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot
 * (including subsystems, commands, and button mappings) should be declared
 * here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  // private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();
  // private final ExampleCommand m_autoCommand = new
  // ExampleCommand(m_exampleSubsystem);
  //private final DriveTrain m_DriveTrain = new DriveTrain();
  // The robot's subsystems
  private static boolean debug = false;
  public final static DriveSubsystemSRX m_robotDrive = new DriveSubsystemSRX();

  public static BlackBoxSubsystem m_BlackBox = new BlackBoxSubsystem();
  public static CameraVisionSubsystem m_CameraVisionSubsystem = new CameraVisionSubsystem();
  //public static EncoderSubsystem m_EncoderSubsystem = new EncoderSubsystem();
  public static PoseEstimatorSubsystem m_PoseEstimatorSubsystem = new PoseEstimatorSubsystem( m_CameraVisionSubsystem.camera, m_robotDrive);
  public static ArmRotationSubsystem m_ArmRotationSubsystem = new ArmRotationSubsystem();
  public static ShooterSubsystem m_ShooterSubsystem = new ShooterSubsystem();
  public static NoteIntakeSubsystem m_NoteIntakeSubsystem = new NoteIntakeSubsystem();
  public static WinchSubsystem m_WinchSubsystem = new WinchSubsystem();
  public static PowerDistribution m_PowerDistribution = new PowerDistribution(0, ModuleType.kCTRE); // TODO Enable this and add SmartDashboard for Winch.
  public static ColorDetectionSubsytem m_ColorDetectionSubsystem = new ColorDetectionSubsytem();
  //public static AnalogUltrasonicDistanceSubsystem m_UltrasonicDistanceSubsystem = 
  //          new AnalogUltrasonicDistanceSubsystem();
  public static PingResponseUltrasonicSubsystem m_PingResponseUltrasonicSubsystem =
    new PingResponseUltrasonicSubsystem(
      UltrasonicConstants.kPingChannel,
      UltrasonicConstants.kEchoChannel,
      UltrasonicConstants.kOffsetToBumper
    );

  //public static DigitalIOSubsystem m_DigitalIOSubsystem = new DigitalIOSubsystem();

  // Controller definitions
  private final Joystick leftJoystick = new Joystick(OIConstants.kLeftJoystick);
  private final Joystick rightJoystick = new Joystick(OIConstants.kRightJoystick);
  XboxController m_driverController = new XboxController(OIConstants.kDriverControllerPort);
  
  //public static CANcoder m_enctest = new CANcoder(38);

  // private final Joystick xboxController = new
  // Joystick(OIConstants.kXboxController);
  double POV_to_double(int pov) {
    double result;
    if (pov == -1) {
      result = 0.0;
    } else if (pov == 0) {
      result = 0.5;
    } else if (pov == 180) {
      result = -0.5;
    } else {
      result = 0.0;
    }
    // SmartDashboard.putNumber("POV", pov);
    // SmartDashboard.putNumber("POV_Out", result);
    return result;
  }
  
  /**
   * Convenience function to create xbox-direction pad rotate buttons.
   * Comprehends alliance to turn to driver's field perspective.
   * @param heading
   * @return xbox-dPad button for turning to the specified heading.
   */
  POVButton dPadButton(int heading) {
    POVButton button = new POVButton(m_driverController, heading);
    button.onTrue(
      new RotateRobotCommand(
          m_robotDrive, 
          Units.degreesToRadians(-heading),
          false
        )

    ).debounce(0.2);
    return button;
  }

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {

    // Configure the button bindings
    configureButtonBindings();

    // SmartDashboard.putData("HomeArmRotation", new ArmHomeCommand(m_ArmRotationSubsystem));

    // Configure default commands
    m_robotDrive.setDefaultCommand(
      // The left stick controls translation of the robot.
      // Turning is controlled by the X axis of the right stick.
      new RunCommand(
        () -> m_robotDrive.allianceRelativeDrive(
            -MathUtil.applyDeadband(m_driverController.getLeftY(), OIConstants.kDriveDeadband),
            -MathUtil.applyDeadband(m_driverController.getLeftX(), OIConstants.kDriveDeadband),
            -MathUtil.applyDeadband(m_driverController.getRightX(), OIConstants.kDriveDeadband),
            true,  true),
        m_robotDrive)
    );

    m_ArmRotationSubsystem.setDefaultCommand(
      new RunCommand(() -> m_ArmRotationSubsystem.rotate(-rightJoystick.getY()), m_ArmRotationSubsystem)
    );
    
    m_WinchSubsystem.setDefaultCommand(
      new RunCommand(() -> m_WinchSubsystem.runMotor(-leftJoystick.getY()), m_WinchSubsystem)
    );

    Ultrasonic.setAutomaticMode(true);
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing
   * it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {


  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by
   * instantiating a {@link edu.wpi.first.wpilibj.GenericHID} or one of its
   * subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then calling
   * passing it to a
   * {@link JoystickButton}.
   */
  
   
    // This next command is just for testing and should be removed or disabled for game play.
    //new JoystickButton(m_driverController, Button.kBack.value)
    //  .onTrue(new InstantCommand(() -> m_robotDrive.zeroHeading(), m_robotDrive));

    new JoystickButton(m_driverController, Button.kRightBumper.value)
      .onTrue(new ScoreNoteInAmp(m_ArmRotationSubsystem, m_ShooterSubsystem));

    new JoystickButton(m_driverController, Button.kLeftBumper.value)
      .onTrue(new TakeInNoteCommand(m_NoteIntakeSubsystem, m_ShooterSubsystem, m_ColorDetectionSubsystem));

    new JoystickButton(m_driverController, Button.kB.value) // TODO test
      .whileTrue(new SwerveToSourceCommand(m_robotDrive, m_PoseEstimatorSubsystem));

    new JoystickButton(m_driverController, Button.kX.value) // TODO test.
      .whileTrue(new SequentialCommandGroup(
        new SwerveToAmpCommand( m_robotDrive, m_PoseEstimatorSubsystem), // Gets close to AMP
        new GotoAmpCommand(m_PoseEstimatorSubsystem, m_robotDrive) // Go the rest of the way to the AMP.
      )
    );

    new JoystickButton(m_driverController, Button.kY.value)
      .onTrue(new InstantCommand(()->m_ArmRotationSubsystem.setPosition(0.0)));

    new JoystickButton(m_driverController, Button.kStart.value) //For testing only, can be reasigned later. Reasign to 
      .onTrue(new ArmHomeCommand(m_ArmRotationSubsystem));

    // This command resets the drive train's pose to the current pose from the pose estimator.  It is also for debug
    // although it might be useful during game play to initialize the robot's coordinate system.  That is TBD.
    // This might be better calling m_PoseEstimatorSubsystem.setCurrentPose() instead of resetOdometry
    new JoystickButton(m_driverController, Button.kBack.value)
            .onTrue(new InstantCommand(
               // () -> m_robotDrive.setAngleDegrees(m_PoseEstimatorSubsystem.getCurrentPose().getRotation().getDegrees()),
               () -> alignDriveTrainToPoseEstimator(),
                m_robotDrive));

    // POV buttons to point robot to a given heading where 0 is
    // straight downfield from the driver's perspective.
    // These change depending on blue or red alliance.
    POVButton   dPad0 = dPadButton(0);
    POVButton  dPad45 = dPadButton(45);
    POVButton  dPad90 = dPadButton(90);
    POVButton dPad135 = dPadButton(135);
    POVButton dPad180 = dPadButton(180);
    POVButton dPad225 = dPadButton(225);
    POVButton dPad270 = dPadButton(270);
    POVButton dPad315 = dPadButton(315);
    
    /**
     * This section defines buttons for the left joystick, joystick 0, which is not intended for use during game play
     * The buttons defined are for debug.
     * Currently these are in order to step through the Autonomous plan
     */
    Pose2d targetPose = Utilities.backToPose(m_PoseEstimatorSubsystem.getAprilTagPose(AprilTag.BLUE_AMP.id()),0.0); // TODO Tune distance
    Pose2d finalPose = new Pose2d(targetPose.getX() + 2.0, targetPose.getY() - 1.0, new Rotation2d(0));
    Pose2d firstMove = new Pose2d(1.0,0.0,new Rotation2d(-Math.PI/2.0));
    if (debug) Utilities.toSmartDashboard("debugPose",targetPose);
    // Autonomous steps part II
    new JoystickButton(leftJoystick, 7).onTrue(
      new InstantCommand(()->setGyroAngleToStartMatch())
    );
    new JoystickButton(leftJoystick, 8).onTrue(
      new ArmHomeCommand(m_ArmRotationSubsystem)
    );
    // Feb 28, 2024 - changed from whileTrue to onTrue.
    new JoystickButton(leftJoystick, 9).onTrue(
      new DriveRobotCommand(RobotContainer.m_robotDrive, firstMove, false)
    );
    new JoystickButton(leftJoystick, 10).onTrue(
      new RotateRobotCommand(m_robotDrive, -Math.PI/2.0, false)
    );
    if (debug) SmartDashboard.putData("FirstMove", new DriveRobotCommand(RobotContainer.m_robotDrive, firstMove, false));
    new JoystickButton(leftJoystick, 11).onTrue(
      new FindAprilTagCommand(m_robotDrive, m_PoseEstimatorSubsystem, 0.05) // 0.1 was too fast
    );
    new JoystickButton(leftJoystick, 12).whileTrue(
      new InstantCommand(()->alignDriveTrainToPoseEstimator())
    );
    
    // Autonomous steps part II
    new JoystickButton(rightJoystick, 7).whileTrue(
      new SwerveToPoseCommand(m_robotDrive, m_PoseEstimatorSubsystem, AprilTag.BLUE_AMP)
    );
    if (debug) SmartDashboard.putData("SWcmd", new SwerveToPoseCommand(m_robotDrive, m_PoseEstimatorSubsystem, AprilTag.BLUE_AMP));
    if (debug) SmartDashboard.putData("SWcmdR", new SwerveToPoseCommand(m_robotDrive, m_PoseEstimatorSubsystem, AprilTag.RED_AMP));

    new JoystickButton(rightJoystick, 8).onTrue(
      new GotoPoseCommand(m_PoseEstimatorSubsystem, m_robotDrive, targetPose)
    );
    new JoystickButton(rightJoystick, 9).onTrue(
      new ScoreNoteInAmp(m_ArmRotationSubsystem, m_ShooterSubsystem)
    );
    new JoystickButton(rightJoystick, 10).onTrue(
      new GotoPoseCommand(m_PoseEstimatorSubsystem, m_robotDrive, finalPose)
    );

    new JoystickButton(rightJoystick, 11).onTrue(
      new ShooterCommand(m_ShooterSubsystem, ShooterConstants.kShootSpeed).withTimeout(ShooterConstants.kShootTimeout)
    );

    new JoystickButton(leftJoystick, 1).onTrue(
      new ScoreNoteInAmp(m_ArmRotationSubsystem, m_ShooterSubsystem)
    );

    new JoystickButton(leftJoystick, 4).whileTrue(
      new DetectColorCommand(m_ColorDetectionSubsystem)
    );
    new JoystickButton(leftJoystick, 2).whileTrue(
      new InstantCommand(() -> m_robotDrive.setX(), m_robotDrive)
    );
    // The next 2 buttons did not work with InstantCommand().onTrue().
    //  They are not needed for game play.
    // Nevertheless, I still want to understand how to perform these commands.
    // Tring with RunCommand().whileTrue()...
    new JoystickButton(leftJoystick, 5).whileTrue(
      new RunCommand(() -> m_robotDrive.wheelsStraightAhead(), m_robotDrive)
    );
    new JoystickButton(leftJoystick, 6).whileTrue(
      new RunCommand(() -> m_robotDrive.wheels45(), m_robotDrive)
    );

    new JoystickButton(rightJoystick, 3).onTrue(
      new InstantCommand(() -> m_NoteIntakeSubsystem.expelNote()).withTimeout(1.0)
    );
    
    
    /* Debugging below */
    if (debug) {

      // Try all possible plans to makesure there are now obvious bad moves in the plans.
      for (int i = 1; i <= 16; i+= 2) {
        for (int j = 1; j <= 8; j+= 2) {
          List<Translation2d> blueAmpPlan = TrajectoryPlans.planTrajectory(TrajectoryPlans.BlueAmpPlan, new Pose2d(16,0,new Rotation2d(0)));
          List<Translation2d> blueSourcePlan = TrajectoryPlans.planTrajectory(TrajectoryPlans.BlueSourcePlan, new Pose2d(16,0,new Rotation2d(0)));
        }
      }
      // Try all possible plans to makesure there are now obvious bad moves in the plans.
      for (int i = 1; i <= 16; i+= 2) {
        for (int j = 1; j <= 8; j+= 2) {
          List<Translation2d> redAmpPlan = TrajectoryPlans.planTrajectory(TrajectoryPlans.RedAmpPlan, new Pose2d(16,0,new Rotation2d(0)));
          List<Translation2d> redSourcePlan = TrajectoryPlans.planTrajectory(TrajectoryPlans.RedSourcePlan, new Pose2d(16,0,new Rotation2d(0)));
        }
      }
      Pose2d testPose1 = new Pose2d(0,0,new Rotation2d(0));
      Pose2d redTestPose1 = testPose1.transformBy(FieldConstants.AllianceTransformation[FieldConstants.RedAlliance]);
      if (debug) Utilities.toSmartDashboard("RedTestPose1", redTestPose1);

      /**
       * Create smart dash button that cycles through the various paths
       * from each 2x2 meter square on the field to the amp and source
       * for both red and blue alliances.
       */
      
      if (debug) {
        SmartDashboard.putData("TTcmd", new SwerveToPoseTest3(m_robotDrive, m_PoseEstimatorSubsystem));
        SmartDashboard.putData("RRcmd", new RotateRobotCommand(m_robotDrive, 0.0, false));
        SmartDashboard.putData("DRcmd", new DriveRobotCommand(m_robotDrive, new Pose2d(1.0,0.0, new Rotation2d()), false));
        SmartDashboard.putData("DAcmd", new DriveOnAprilTagProjectionCommand(m_PoseEstimatorSubsystem, m_robotDrive, m_CameraVisionSubsystem.camera, m_driverController));
      }
    } // (debug)
  } // (configureButtonBindings)

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return new Autonomous(this);
  }
   
  // Function to align the PoseEstimator pose and the DriveTrain pose.
  // This assumes that the PoseEstimator has a really good estimate.
  // In other words, that it has a recent, accurate view of an Apriltag.
  public void alignDriveTrainToPoseEstimator() {
    // Set the gyro angle to match the pose estimator 
    // compensating for the placement of the camera on the robot.
    /*
    m_robotDrive.setAngleDegrees( 
      MathUtil.inputModulus(
        m_PoseEstimatorSubsystem.getCurrentPose().getRotation().getDegrees()+VisionConstants.CAMERA_TO_ROBOT.getRotation().toRotation2d().getDegrees()
        ,-180
        , 180
      )
    );
    */
    // Update the drive trains X, Y, and robot orientation to match the pose estimator.
    m_robotDrive.resetOdometry(m_PoseEstimatorSubsystem.getCurrentPose());
  }

  /**
   *  This function sets the gyro angle based on the alliance (blue or red)
   * and the assumed starting position of the robot on the field.
   * The current assumption is that the robot will be placed with it's back to the
   * alliance wall.  The "Y" coordinates of the robot will be determined by the
   * PoseEstimator once an april tag is captured by the vision system.
   */
   public void setGyroAngleToStartMatch() {
    boolean isBlueAlliance = Utilities.isBlueAlliance(); // From the Field Management system.
    double startingHeading; // degrees.
    if (isBlueAlliance) {
      startingHeading = 0.0;
      m_robotDrive.setAngleDegrees(startingHeading);
      m_robotDrive.resetOdometry(
        new Pose2d(
           DriveConstants.kBackToCenterDistance // Assume robot is back to the starting wall
          ,FieldConstants.yCenter               // Pick center of the field since we dont know where we will start.
          ,new Rotation2d(Units.degreesToRadians(startingHeading)) // Facing toward the field.
        )
      );
    } else {
      startingHeading = 180.0;
      m_robotDrive.setAngleDegrees(startingHeading);
      m_robotDrive.resetOdometry(
        new Pose2d(
           FieldConstants.xMax - DriveConstants.kBackToCenterDistance // Assume robot is back to the starting wall
          ,FieldConstants.yCenter                                     // Pick center of the field since we dont know where we will start.
          ,new Rotation2d(Units.degreesToRadians(startingHeading))    // Facing toward the field.
        )
      );
    }
   }
}