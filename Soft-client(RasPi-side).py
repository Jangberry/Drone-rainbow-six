import smbus 
import time
import socket 

bus = smbus.SMBus(1)
address = 0x12

hote = "192.168.0.08"
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
        exit()
    recu = recu.split(" ")
    if len(recu) > 1:
        dataD = int(recu[0])
        dataG = int(recu[1])
        send(dataD, dataG)