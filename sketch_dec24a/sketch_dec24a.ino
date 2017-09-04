#include <Wire.h>

#define AvantD 4   //pin moteur droit : arriere
#define ArrieD 7    //pin moteur droit : avant
#define ArrieG 8    //pin moteur gauche : arriere
#define AvantG 11    //pin moteur gauche : avant
#define pwmAvantD 5   //pin hacheur moteur droit : arriere
#define pwmArrieD 6    //pin hacheur moteur droit : avant
#define pwmArrieG 9    //pin hacheur moteur gauche : arriere
#define pwmAvantG 10    //pin hacheur moteur gauche : avant
#define porti2c 0x12
int dataRecevied1(128);
int vitesseD(0);
int last1(128);
int dataRecevied2(128);
int vitesseG(0);
int last2(128);
int moteurD(0);
int moteurG(0);
int lastG(0);
int lastD(0); 
boolean data(false);
boolean done(true);

void setup() {
  pinMode(ArrieD, OUTPUT);
  pinMode(AvantD, OUTPUT);
  pinMode(ArrieG, OUTPUT);
  pinMode(AvantG, OUTPUT);
  pinMode(pwmArrieD, OUTPUT);
  pinMode(pwmAvantD, OUTPUT);
  pinMode(pwmArrieG, OUTPUT);
  pinMode(pwmAvantG, OUTPUT);
  Wire.begin(porti2c);
  Wire.onReceive(receiveEvent);
  Serial.begin(9600);       
}

void loop() {
  if (done == false)
  {
    int G(lastG);
    int D(lastD);
    int count(0);
    while (G != vitesseG || D != vitesseD || count < 255)
    {
      done = true;
      count += 1;
      if (G < vitesseG)
      {
        G = G + ((vitesseG*vitesseG)/255)*2;
      }
      else if (G > vitesseG)
      {
        G = G - ((vitesseG*vitesseG)/255)*2;
      }
      else
      {
        G = vitesseG;
      }
      if (D < vitesseD)
      {
        D = D + ((vitesseD*vitesseD)/255)*2;
      }
      else if (D > vitesseD)
      {
        D = D - ((vitesseD*vitesseD)/255)*2;
      }
      else
      {
        D = vitesseD;
      }
      digitalWrite(moteurD, D);
      digitalWrite(moteurG, G);
      delay(1);
    }
    if (vitesseD == 254){digitalWrite(moteurD, HIGH);}
    else {analogWrite(moteurD, vitesseD);}
    if (vitesseG == 254){digitalWrite(moteurG, HIGH);}
    else {analogWrite(moteurG, vitesseG);}
    lastD = vitesseD;
    lastG = vitesseG;
    count = 0;
    Serial.println("write :");
    Serial.println(vitesseD);
    Serial.println(vitesseG);
  }
  delay(1);
}


void receiveEvent(int howMany)
{
  if(data==false)
  {
    dataRecevied1 = Wire.read();
    data = true;
    Serial.println("Recu :");
    Serial.println(dataRecevied1);
  }
  else
  {
    dataRecevied2 = Wire.read();
    data = false;
    Serial.println(dataRecevied2);


    vitesseG = (dataRecevied2 - 128)*2;
    vitesseD = (dataRecevied1 - 128)*2;
    if(vitesseD >= 0)
    {
      digitalWrite(ArrieD, LOW);
      moteurD = pwmAvantD;
      digitalWrite(pwmArrieD, LOW);
      digitalWrite(AvantD, HIGH);
    }
    else
    {
      digitalWrite(AvantD, LOW);
      moteurD = pwmArrieD;
      digitalWrite(pwmAvantD, LOW);
      digitalWrite(ArrieD, HIGH);
      vitesseD -= vitesseD*2;
    }

    if(vitesseG >= 0)
    {
      digitalWrite(ArrieG, LOW);
      moteurG = pwmAvantG;
      digitalWrite(pwmArrieG, LOW);
      digitalWrite(AvantG, HIGH);
    }
    else
    {
      digitalWrite(AvantG, LOW);
      moteurG = pwmArrieG;
      digitalWrite(pwmAvantG, LOW);
      digitalWrite(ArrieG, HIGH);
      vitesseG -= vitesseG*2;
    }
    done = false;
  }
}
