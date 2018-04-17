from Appliance import Appliance
# from Pi_Interface \
import auth

authobj = auth.Authorization()

fan = Appliance("fan", False, 0, 4)
light = Appliance("light", False, 0, 27)
tv = Appliance("tv", False, 0, 22)

applianceArray = {
    "fan": fan,
    "tv": tv,
    "light": light
}


def route_handle(status_t,route,applianceJson,clientsocket):

    if route == 'auth':
        print ('inside auth')
        status, token = authobj.auth(applianceJson.__getitem__("user"))
        print (status,token)
        if status:
            clientsocket.send(('{"token":%s}'%token).encode())
        else:
            clientsocket.send(b'{"error":"LOGIN FAILED"}')

    elif route == 'signup':
        status = authobj.signUp(applianceJson.__getitem__("user"))

        if status:
            clientsocket.send(b'{"status":"SUCCESS"}')
        else:
            clientsocket.send(b'{"status":"USERNAME ALREADY EXISTS"}')

    elif status_t and route == 'app':

        x = applianceJson.__getitem__("applName")
        requestedAppliance = applianceArray[x]
        print("Original state: %s" % requestedAppliance.__getStringObject__())
        if applianceJson["state"] == 'true':
            requestedState = True
            requestedAppliance.startLed()
        else:
            requestedState = False
            requestedAppliance.stopLed()
        requestedAppliance.__setstate__(requestedState)
        # also instruct the pi to do the turn the actuators

        clientsocket.send((requestedAppliance.__getStringObject__()).encode())
        print(requestedAppliance.__get__())

