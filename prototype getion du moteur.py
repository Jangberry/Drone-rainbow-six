import smbus 
import time 
# Remplacer 0 par 1 si nouveau Raspberry 
bus = smbus.SMBus(1)
address = 0x12

