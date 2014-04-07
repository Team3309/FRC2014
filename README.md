FRC TEAM 3309
=============

This is FRC Team 3309's code for the 2014 FRC game, Aerial Assist.

Compilation
-----------

Download Netbeans and install the FRC plugins.

Now clone this repository.

To just compile (and not download)
```ant suite```

To compile and download to the robot
```ant deploy```

To download constants file and auto modes
```ant deploy-files```

Constants
---------

Constants are stored in res/Constants.txt, this file should be copied to the cRIO with FTP

Constants should be in this format:
```
name=value
#Comments start with a # symbol
//Comments can also start with double-slashes
```

Supported values are integers, doubles, booleans and double/integer arrays

Examples
```
drive.encoder.fl.a=1
#arrays are separated by comments
drive.tank_gyro.pid=.001,0,0
#this is for the value to set the solenoid to to engage mecanum
drive.mecanum.on=false
```

To use a Constant in code, instantiate a Constant object
```
Constant configMecanumOn = new Constant("drive.mecanum.on", false);
```

The first argument is the name of the Constant (will be used to look up in the file) and the second argument is a default value in case a constant with that name wasn't found


Autonomous Scripts
==================

Autonomous scripts can be written with our simple auto scripting language described below.
Scripts should be stored in the res/ directory because it can be FTP'ed to the cRIO by using the ant target ```ant deploy-files```

The script directory should be stored in res/auto_scripts.txt with the following format:
```
<Chooser Number> <file name> <name>
1 mobility_bonus.txt Mobility Bonus
```

Available commands are:
```
extend intake OR intake extend
retract intake OR intake retract
run intake OR intake run

extend pocket OR pocket extend
retract pocket OR pocket retract

drive throttle,turn

wait <time in seconds>
wait for kinect #this waits until the driver raises his hands above his head
wait for kinect <timeout in seconds>
#this waits until the driver raises his hands above his head or the timeout is reached
#in all of the wait commands, "wait" can be replaced with "delay" and functionality is same

shoot
winch

print <message>
```

Comments can either be on their own line prefixed by // OR # or can be at the end of a line when prefixed by // OR #.
There are currently no block comments implemented
