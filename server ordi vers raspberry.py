#-*- coding: utf-8 -*-
import socket
import pygame
from pygame.locals import *

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






s = socket.socket()
s.bind(('', 234))
s.listen(5)
print("Veuillez connecter le client")
client, adress = s.accept()
print("client connecté :"+str(adress))

def send(mess="127 127"):
    client.send(mess)

def effectuer(arrow):
    if arrow=="avant":
        window.blit(up, (0,0))
        send(u"255 255")
    if arrow=="arriere":
        window.blit(down, (0,0))
        send("0 0")
    if arrow=="gauche":
        window.blit(left, (0,0))
        send(u"255 125")
    if arrow==u"droite":
        window.blit(right, (0,0))
        send(u"125 255")
    if arrow=="avantdroite":
        window.blit(upright, (0,0))
        send(u"175 255")
    if arrow=="avantgauche":
        window.blit(upleft, (0,0))
        send(u"255 175")
    if ("avantarriere" == arrow or "gauchedroite" == arrow):
        window.blit(void, (0,0))
        send(u"127 127")

    pygame.display.flip()

while Continue:
    for event in pygame.event.get():
        if event.type == QUIT:
            Continue=0
            send(u"stop")
        if event.type == KEYUP:
            arrow = ""
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

