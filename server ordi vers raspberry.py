import socket
import pygame
from pygame.locals import *

Continue = 1
arrow = ""
pygame.init()
window = pygame.display.set_mode((50, 50))
up = pygame.image.load("/resouces/arrowup.png").convert()
down = pygame.image.load("/resouces/arrowdown.png").convert()
left = pygame.image.load("/resouces/arrowleft.png").convert()
right = pygame.image.load("/resouces/arrowright.png").convert()
upright = pygame.image.load("/resouces/arrowupr.png").convert()
upleft = pygame.image.load("/resouces/arrowupl.png").convert()
void = pygame.image.load("/resources/void.png")

socket = socket.socket()
socket.bind(('', 4238))
socket.listen(5)
print("Veuillez connecter le client")
client, adress = socket.accept()
send = socket.send()

def effectuer(arrow):
    if arrow=="avant":
        window.blit(up, (0,0))
        send("255 255")
    if arrow=="arriere":
        window.blit(down, (0,0))
        send("0 0")
    if arrow=="gauche":
        window.blit(left, (0,0))
        send("255 125")
    if arrow=="droite":
        window.blit(right, (0,0))
        send("125 255")
    if arrow=="avantdroite":
        window.blit(upright, (0,0))
        send("175 255")
    if arrow=="avantgauche":
        window.blit(upleft, (0,0))
        send("255 175")
    if ("avantarriere" or "gauchedroite")in arrow:
        window.blit(void, (0,0))
        send("127 127")

    pygame.display.flip()

while Continue:
    for event in pygame.event.get():
        if event.type == QUIT:
            Continue=0
            send("stop")
        if event.type == KEYDOWN:
            if event.key == K_z:
                arrow = arrow+"avant"
            if event.key == K_s:
                arrow = arrow+"arriere"
            if event.key == K_q:
                arrow = arrow+"gauche"
            if event.key == K_d:
                arrow = arrow+"droite"
            effectuer(arrow)
            arrow = ""
        if event.type == KEYUP:
            arrow = ""