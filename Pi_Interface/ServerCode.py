import socket
import json
from Appliance import Appliance
import RPi.GPIO as GPIO


GPIO.cleanup()
GPIO.setmode(GPIO.BCM)
GPIO.setup(6, GPIO.OUT)
GPIO.setup(4, GPIO.OUT)
GPIO.setup(27, GPIO.OUT)
GPIO.setup(22, GPIO.OUT)

fan = Appliance("fan", False, 0, 4)
light = Appliance("light", False, 0, 27)
tv = Appliance("tv", False, 0, 22)

applianceArray = {
    "fan": fan,
    "tv": tv,
    "light": light
}

serverSocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
# host = '127.0.0.1'
host = "192.168.0.112"
# host = "192.168.43.89"
# host = "192.168.1"
# host = "0.0.0.1"
# port = 9999
port = 8000

# try and catch block to bind to server
try:
    serverSocket.bind((host, port))
except socket.error as e:
    print e
    print "exception in binding to server"
    s = socket.gethostname()
    print s
    serverSocket.bind((s, port))

print ("Host name: %s" % host)

serverSocket.listen(0)

# the loop is called in main because only one client is allowed to connect to server and all the
# devices are to controlled from main thread and nothing is done without the consent of client

while True:
    try:
        print "inside Loop"
        (clientSocket, address) = serverSocket.accept()
        print ("Client with address: %s connected and address[1]= %d" % (address[0], address[1]))
        received = clientSocket.recv(1024)
        applianceJson = json.loads(received)  # received JSON
        print ("appliance request: %s" % received)

        x = applianceJson.__getitem__("applName")
        requestedAppliance = applianceArray[x]
        print ("Original state: %s" % requestedAppliance.__getStringObject__())
        if applianceJson["state"] == 'true':
            requestedState = True
            requestedAppliance.startLed()
        else:
            requestedState = False
            requestedAppliance.stopLed()
        requestedAppliance.__setstate__(requestedState)
        # also instruct the pi to do the turn the actuators

        clientSocket.send(requestedAppliance.__getStringObject__())
        print requestedAppliance.__get__()
        clientSocket.close()
    except socket.error as e:
        print "exception"
        break

print "Out of the loop"
