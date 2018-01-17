#include <Wire.h>

#define AvantD 5      //pin hacheur moteur droit : arriere
#define ArrieD 6      //pin hacheur moteur droit : avant
#define ArrieG 9      //pin hacheur moteur gauche : arriere
#define AvantG 10     //pin hacheur moteur gauche : avant
#define addrsi2c 0x12  //adresse I2C de l'arduino
int dataRecevied1(128);
int vitesseD(0);
int dataRecevied2(128);
int vitesseG(0);
int moteurD(0);
int moteurG(0);
boolean data(false);
boolean done(true);

void setup() {
  pinMode(ArrieD, OUTPUT);
  pinMode(AvantD, OUTPUT);
  pinMode(ArrieG, OUTPUT);
  pinMode(AvantG, OUTPUT);
  Wire.begin(addrsi2c);
  Wire.onReceive(receiveEvent);
  Serial.begin(9600);       
}

void loop() {
  if (done == false)
  {
    analogWrite(vitesseG, moteurG);
    analogWrite(vitesseD, moteurD);
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
      moteurD = AvantD;
    }
    else
    {
      digitalWrite(AvantD, LOW);
      moteurD = ArrieD;
      vitesseD -= vitesseD*2;
    }

    if(vitesseG >= 0)
    {
      digitalWrite(ArrieG, LOW);
      moteurG = AvantG;
    }
    else
    {
      digitalWrite(AvantG, LOW);
      moteurG = ArrieG;
      vitesseG -= vitesseG*2;
    }
    done = false;
  }
}
