class Appliance:
    def __init__(self, applName, state, powerLevel, pinNo):
        self.applName = applName
        self.state = state
        self.powerLevel = powerLevel
        self.pinNo = pinNo

    def __setstate__(self, state):
        self.state = state

    def __getstate__(self):
        return self.state

    def __setpowerLevel__(self, powerLevel):
        self.powerLevel = powerLevel

    def __getpowerLevel__(self):
        return self.powerLevel

    def __get__(self):
        return {"name": self.applName, "state": self.state, "powerLevel": self.powerLevel}

    def __getStringObject__(self):
        if self.state:
            strState = "true"
        else:
            strState = "false"
        return "{name: " + self.applName + ", state: " + strState + ", powerLevel: " + str(self.powerLevel) + "}\n"

    def startLed(self):
        GPIO.output(self.pinNo, True)

    def stopLed(self):
        GPIO.output(self.pinNo, False)
