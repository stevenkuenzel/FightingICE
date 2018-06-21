from py4j.java_gateway import get_field

class PMSampleAI(object):
    def __init__(self, gateway):
        self.gateway = gateway

    def close(self):
        pass
    
    def getInformation(self, frameData):
        # Getting the frame data of the current frame
        self.frameData = frameData
        self.cc.setFrameData(self.frameData)

    # please define this method when you use FightingICE version 3.20 or later
    def roundEnd(self, x, y, z):
        print(x)
        print(y)
        print(z)
    	
    # please define this method when you use FightingICE version 4.00 or later
    def getScreenData(self, sd):
    	pass
        
    def initialize(self, gameData):
        # Initializng the command center, the simulator and some other things
        self.inputKeys = self.gateway.new_array(self.gateway.jvm.struct.Key,2)
        self.inputKeys[0] = self.gateway.jvm.struct.Key()
        self.inputKeys[1] = self.gateway.jvm.struct.Key()
        self.cc = self.gateway.jvm.aiinterface.PMCommandCenter()
        self.frameData = self.gateway.jvm.struct.FrameData()
        self.gameData = gameData
        self.simulator = self.gameData.getSimulator()
        return 0
        
    def input(self):
        # Return the input for the current frame
        return self.inputKeys
        
    def processing(self):
        # Just compute the input for the current frame
        if self.frameData.getEmptyFlag() or self.frameData.getRemainingFramesNumber() <= 0:
            self.isGameJustStarted = True
            return
        for i in range(2):
            if self.cc.getSkillFlag(i):
                self.inputKeys[i] = self.cc.getSkillKey(i)
            else:
                self.inputKeys[i].empty()
                self.cc.skillCancel(i)
		# Just spam kick
                self.cc.commandCall("B",i)
                
		
    # This part is mandatory
    class Java:
        implements = ["aiinterface.PMAIInterface"]
        
