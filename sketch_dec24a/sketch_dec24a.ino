#include <Wire.h>

#define ArrieD 5   //pin acheur moteur droit : arriere
#define AvantD 6    //pin acheur moteur droit : avant
#define ArrieG 9    //pin acheur moteur gauche : arriere
#define AvantG 10    //pin acheur moteur gauche : avant
#define porti2c 0x12
int dataRecevied1(127);
int vitesseD(0);
int last1(127);
int dataRecevied2(127);
int vitesseG(0);
int last2(127);
int moteurD(0);
int moteurG(0);
boolean data(false);
boolean done(true);

void setup() {
  pinMode(ArrieD, OUTPUT);
  pinMode(AvantD, OUTPUT);
  pinMode(ArrieG, OUTPUT);
  pinMode(AvantG, OUTPUT);
  Wire.begin(porti2c);
  Wire.onReceive(receiveEvent);
  Wire.onRequest(sendEvent);
  Serial.begin(9600);       
}

void loop() {
  delay(1);
  if(done==false && (last2 != dataRecevied2 || last1 != dataRecevied1))
  {
    done=true;
    last1 = dataRecevied1;
    last2 = dataRecevied2;
    vitesseD = dataRecevied1 - 127;
    vitesseG = dataRecevied2 - 127;

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


    for(int i(0),o(0),count(0); ((i<vitesseD || o<vitesseG) && count != 11); count += 1)
    {
      delay(9);
      if(i < vitesseD)
      {
        i += vitesseD / 10;
      }
      else
      {
        i = vitesseD;
      }
      if(o < vitesseG)
      {
        o += vitesseG / 10;
      }
      else
      {
        o = vitesseG;
      }
      analogWrite(moteurD, i);
      analogWrite(moteurG, o);
    }
    Wire.write(0);
  }
}


void receiveEvent(int howMany)
{
  if(data==false)
  {
    dataRecevied1 = Wire.read();
    data = true;
  }
  else
  {
    dataRecevied2 = Wire.read();
    data = false;
    done = false;
  }
}

void sendEvent()
{
  if(data == true)
  {
    Wire.write(dataRecevied1);
  }
  else
  {
    Wire.write(dataRecevied2);
  }
}
