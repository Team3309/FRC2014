FRC TEAM 3309
=============

This is FRC Team 3309's code for the 2014 FRC game, Aerial Assist.

Compilation
-----------

Download Netbeans and install the FRC plugins.

Now clone this repository.

To just compile (and not download)
> ant suite

To compile and download to the robot
> ant deploy


Constants
---------

Constants are stored in Constants.txt, this file should be copied to the cRIO with FTP

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
