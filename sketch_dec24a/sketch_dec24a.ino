#include <Wire.h>

#define sensD 8
#define sensG 7
#define moteurD 6
#define moteurG 5
#define porti2c 0x12
int dataRecevied1(127);
int vitesseD(0);
int dataRecevied2(127);
int vitesseG(0);
boolean data(false);
boolean done(true);

void setup() {
  pinMode(moteurD, OUTPUT);
  pinMode(moteurG, OUTPUT);
  Wire.begin(porti2c);
  Wire.onReceive(receiveEvent);
  Wire.onRequest(sendEvent);
  Serial.begin(9600);       
}

void loop() {
  delay(1);
  if(done=false)
  {
    vitesseD = dataRecevied1 - 127;
    vitesseG = dataRecevied2 - 127;

    if(vitesseD >= 0)
    {
      digitalWrite(sensD, LOW);
    }
    else
    {
      digitalWrite(sensG, HIGH);
      vitesseD -= vitesseD*2;
    }

    if(vitesseG >= 0)
    {
      digitalWrite(sensG, LOW);
    }
    else
    {
      digitalWrite(sensG, HIGH);
      vitesseG -= vitesseG*2;
    }


    for(int i(0),o(0),count(0); i<vitesseD || o<vitesseG && count != 11; count += 1)
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
    done = true;
    Wire.write(0);
  }
}


void receiveEvent() 
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
