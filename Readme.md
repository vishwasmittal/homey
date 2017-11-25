Homey
=====
Your personal assistant with sole purpose to make your home smarter and your life easy than ever before. It gives you the power to control your house with just a single touch or just a command of your voice. If your are feeling lazy enough to switch off the lights before going to sleep, worry not, homey will do that for you on your single command.


Contents
--------
This Readme is divided into several parts
1. [Structure](#structure)
2. [Technology](#technology)
3. [Future Planning](#future_planning)


<a name="structure">Structure</a>
---------
Homey can be broken down in 2 separate modules viz.
1. Raspberry Pi Server
2. Android App

### Raspberry Pi Server
Raspberry Pi is used as the controlling using for all the appliances in the house. There can be various ways with which you can connect your appliances with Pi like connecting a Relay Board using wires and then using that board to control the switch or adding a wireless layer between Pi and Relay or making your own custom device to control the appliances.

### Android App
This is the real remote control that the user uses to control all the appliances. User can control the appliances using one of the three methods viz. touch or voice or text command.


<a name="technology">Technology</a>
---------
This section enlists various technologies used in the project.

### Raspberry Pi Server
The server is a raspberry Pi setup that is connected to a router and running the server code. For the app to connect to server, both of them should be on same network.

The code is simple Python socket program that takes the command from Android app, analyze it and then provides the relevant output from GPIO pins towards the relay where it can switch on/off the appliance based on the output.

### Android App
The app is simple Java based android application that acts as a medium between the server and user.

It opens a socket connection between server and send the command given by the user. As discussed earlier, It can take touch, voice or text command.

Touch command is directly analyzed converted to the server identified form.

Text command is analyzed using NLP based on [Microsoft Luis](https://www.luis.ai/home) and the results are then serialized and sent to server.

Voice commands are first converted to text using Speech to text API provided by Android and then analyzed further.


<a name="future_planning">Future Planning</a>
---------
This is just a basic version of what I had in mind when its development began. This app has certain design and implementation flaws. Some of the knows bugs are listed below
1. Supports only fixed no. of appliances and is hardcoded (both server and app)
2. No authentication (both server and app)
3. Server code is simple python without any standard protocol to follow (server)
4. Minor design flaws in Android like unnecessary Toasts (app)
5. No control over appliances/ server outside the home networking

There is always scope of improvement in anything we do. Some of the improvements/ fixes are suggested below
1. Make the addition of appliances dynamic and use a secured Database to store it.
2. Use a standard framework for standardization of protocol and enabling authentication. Major candidates include Django and Flask. Can also use Twisted for event driven programming.
3. Enable port forwarding on server side.
4. Improve the UI/UX.


### Some Glimpses
<img src = "https://github.com/vishwas78/homey/blob/master/images/screenshot_app_home.png" width = "300">
<img src = "https://github.com/vishwas78/homey/blob/master/images/screenshot_app_voice.png" width = "300">
