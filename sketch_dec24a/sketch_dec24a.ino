#include <Wire.h>

#define AvantD 3      //pin hacheur moteur droit : arriere
#define ArrieD 6      //pin hacheur moteur droit : avant
#define ArrieG 9      //pin hacheur moteur gauche : arriere
#define AvantG 10     //pin hacheur moteur gauche : avant
#define addrsi2c 0x12  //adresse I2C de l'arduino
//0  1  2
int dataRecevied1(128);
int vitesseD(0);
int dataRecevied2(128);
int vitesseG(0);
int moteurD(0);
int moteurG(0);
boolean data(false);

void setup() {
  pinMode(ArrieD, OUTPUT);
  pinMode(AvantD, OUTPUT);
  pinMode(ArrieG, OUTPUT);
  pinMode(AvantG, OUTPUT);
  Wire.begin(addrsi2c);
  Wire.onReceive(receiveEvent);
  Serial.begin(9600);
  DDRD = DDRD | 00000111;
  PORTD = 0;
  PORTD = PORTD + 1;
  delay(50);
  PORTD = PORTD + 1;
  delay(50);
  PORTD = PORTD + 1;
  delay(50);
  PORTD = PORTD + 1;
  delay(50);
  PORTD = PORTD + 1;
  delay(50);
  PORTD = PORTD + 1;
  delay(50);
  PORTD = PORTD + 1;
  delay(400);
  PORTD = PORTD - 1;
  delay(50);
  PORTD = PORTD - 1;
  delay(50);
  PORTD = PORTD - 1;
  delay(50);
  PORTD = PORTD - 1;
  delay(50);
  PORTD = PORTD - 1;
  delay(50);
  PORTD = PORTD - 1;
  delay(50);
  PORTD = PORTD - 1;
}

void loop() {
  Serial.println("Sur moteurs");
  Serial.println(vitesseG);
  Serial.println(vitesseD);
  Serial.println(moteurG);    
  Serial.println(moteurD);
  PORTD = PORTD | 00000000;
  delay(50);
  PORTD = PORTD | 00000001;
  delay(50);
  PORTD = PORTD | 00000011;
  delay(50);
  PORTD = PORTD | 00000111;
  delay(350);
  PORTD = PORTD | 00000011;
  delay(50);
  PORTD = PORTD | 00000001;
  delay(100);
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
    
    Serial.println("donnes completes");
    
    analogWrite(moteurG, vitesseG);
    analogWrite(moteurD, vitesseD);
  }
}
