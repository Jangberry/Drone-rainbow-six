#-*- coding: utf-8 -*-
import smbus                #   if it bug, verify in sudo "raspi-config" that interface > I2C is activated
import time
import socket
import RPi.GPIO as gpio		#	if it bug, verify that RPi.GPIO python lib is installed, if not, Google is your friend, if it is, retry with root privileges

s = socket.socket()
s.bind(('', 234))
s.listen(5)
print("Waiting for client...")
client, adress = s.accept()
print("Client connected :"+str(adress))

def send(dataD, dataG):
    bus.write_byte(address, dataD)
    time.sleep(0.01)
    bus.write_byte(address, dataG)
    


gpio.setmode(gpio.BCM)
gpio.setup(7, gpio.OUT, initial=gpio.HIGH)
tme.sleep(2)

bus = smbus.SMBus(1)
address = 0x12

while True:
    recu = client.recv(255)
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
