#-*- coding: utf-8 -*-
import socket
import pygame                   #   sudo apt-get install python-pygame
from pygame.locals import *

#################################
#                               #
hote = "192.168.0.6"            #       IP adress of the Raspi
#                               #
#################################

port = 234
s = socket.socket()
s.connect((hote, port))
print("connected")

Continue = 1
arrow = ""
pygame.init()
window = pygame.display.set_mode((50, 50))
up = pygame.image.load("arrowup.png").convert()
down = pygame.image.load("arrowdown.png").convert()
left = pygame.image.load("arrowleft.png").convert()
right = pygame.image.load("arrowright.png").convert()
upright = pygame.image.load("arrowupr.png").convert()
upleft = pygame.image.load("arrowupl.png").convert()
void = pygame.image.load("void.png")
lastarrow=""

def send(mess=u"128 128"):
    client.send(mess)

def effectuer(arrow):
    if arrow=="avant":
        window.blit(up, (0,0))
        send(u"255 255")
    if arrow=="arriere":
        window.blit(down, (0,0))
        send("1 1")
    if arrow=="gauche":
        window.blit(left, (0,0))
        send(u"255 1")
    if arrow==u"droite":
        window.blit(right, (0,0))
        send(u"1 255")
    if arrow=="avantdroite" or arrow=="droiteavant":
        window.blit(upright, (0,0))
        send(u"128 255")
    if arrow=="avantgauche" or arrow=="gaucheavant":
        window.blit(upleft, (0,0))
        send(u"255 128")
    if "avantarriere" == arrow or "gauchedroite" == arrow or "arriereavant" == arrow or "droitegauche" == arrow or arrow == "":
        window.blit(void, (0,0))
        send(u"128 128")

    pygame.display.flip()

while Continue:
    for event in pygame.event.get():
        if event.type == QUIT:
            Continue=0
            send(u"stop")
        if event.type == KEYUP:
            if event.key == K_z:
                arrow = "".join(arrow.split("avant"))
            if event.key == K_s:
                arrow = "".join(arrow.split("arriere"))
            if event.key == K_q:
                arrow = "".join(arrow.split("gauche"))
            if event.key == K_d:
                arrow = "".join(arrow.split("droite"))
        if event.type == KEYDOWN:
            if event.key == K_z:
                arrow = arrow+"avant"
            if event.key == K_s:
                arrow = arrow+"arriere"
            if event.key == K_q:
                arrow = arrow+"gauche"
            if event.key == K_d:
                arrow = arrow+"droite"
    if arrow != lastarrow:
        effectuer(arrow)
        lastarrow=arrow