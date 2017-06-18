import smbus                #   if it bug, verify in sudo "raspi-config" that interface > I2C is activated
import time
import socket
import RPi.GPIO as gpio

#################################
#                               #
hote = "192.168.0.17"           #       IP adress of the computer (this variable may disapear (you'll just not have to set it, it'll be automatic) in a incomming commit)
#                               #
#################################


gpio.setmode(gpio.BCM)
gpio.setup(7, gpio.OUT, initial=gpio.HIGH)
time.sleep(2)

bus = smbus.SMBus(1)
address = 0x12

port = 234
s = socket.socket()

def send(dataD, dataG):
    bus.write_byte(address, dataD)
    time.sleep(0.01)
    bus.write_byte(address, dataG)

s.connect((hote, port))
print("connected")

while 1:
    recu = s.recv(255)
    #print(str(type(recu))+str(recu))
    #recu = recu.encode("utf8")
    print(recu)
    if recu == "stop":
        gpio.output(7, gpio.LOW)
        gpio.cleanup()
        exit()
    recu = recu.split(" ")
    if len(recu) > 1:
        dataD = int(recu[0])
        dataG = int(recu[1])
        send(dataD, dataG)
