import smbus 
import time
import socket 


bus = smbus.SMBus(1)
address = 0x12

hote = "192.168.0.55"
port = 4238
s = socket.socket()

def send(dataD, dataG):
    bus.write_byte(address, dataD)
    while bus.read_byte(address) != dataD:
        time.sleep(0.01)
    bus.write_byte(address, dataG)
    while bus.read_byte(address) != dataG:
        time.sleep(0.01)

s.connect((hote, port))

while 1:
    recu = s.recv(2040)
    print(recu)
    if recu == "stop":
        exit()
    recu = recu.split(" ")
    dataD = int(recu[0])
    dataG = int(recu[1])
    send(dataD, dataG)