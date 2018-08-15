/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * This is the soft for the arduino.                                                               *
 *                                                                                                 *
 * Created by                                                                                      *
 *        Matthieu "Jangberry" M.                                                                  *
 *        Contact:                                                                                 *
 *          contact@jangberry.ovh                                                                  *
 *                                                                                                 *
 * Tested on an arduino nano w/ATmega328P                                                          *
 * To knwo how to connect all pins, please refer to the Fritzing diagram at                        *
 *  ~/Drone-Rainbow-six/Hard/schema.fzz                                                            *
 *                                                                                                 *
 *                                                                                                 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */

#include <Wire.h>

#define RightForwardPin 10  // Pin for the forward  way of the right motor on the L298N
#define RightBackwardPin 9  //  "   "   "  backward  "   "  "  right   "    "  "    "
#define LeftForwardPin 3    //  "   "   "  forward   "   "  "  left    "    "  "    "
#define LeftBackwardPin 6   //  "   "   "  backward  "   "  "  left    "    "  "    "
#define I2CAdress 0x12      // Arduino's I2C adress

/* Leds on 8  11  12
 *        /\      /\
 *      Middle    ||
 *             Far right
 */

byte dataRecevied1 = 128;
int SpeedRight = 0;
byte dataRecevied2 = 128;
int SpeedLeft = 0;
byte RightPin = 0;
byte LeftPin = 0;
boolean connected = false; //
boolean first = true;      //

void setup() {
  // Setting motors pins as output
  pinMode(RightBackwardPin, OUTPUT);
  pinMode(RightForwardPin, OUTPUT);
  pinMode(LeftBackwardPin, OUTPUT);
  pinMode(LeftForwardPin, OUTPUT);
  
  Wire.begin(I2CAdress);              // Launching the I2C bus
  Wire.onReceive(receiveEvent);       // Setting the interupt
  //Serial.begin(9600);
  
  // Setting leds' pins as output + playing the power up animation
  pinMode(8,  OUTPUT);
  pinMode(11, OUTPUT);
  pinMode(12, OUTPUT);
  digitalWrite(8,  HIGH);
  delay(200);
  digitalWrite(11, HIGH);
  delay(200);
  digitalWrite(12, HIGH);
  delay(1000);
  digitalWrite(8,  LOW);
  digitalWrite(11, LOW);
  digitalWrite(12, LOW);
}

void loop() {
  // Running annimation only if connected
  while (connected) {
    if (first) {
      first = false;
      digitalWrite(8,  HIGH);
      delay(100);
      digitalWrite(11, HIGH);
      delay(100);
      digitalWrite(12, HIGH);
    }
    digitalWrite(8,  HIGH);
    delay(50);
    digitalWrite(11, HIGH);
    delay(50);
    digitalWrite(12, HIGH);
    delay(350);
    digitalWrite(12, LOW);
    delay(50);
    digitalWrite(11, LOW);
    delay(50);
    digitalWrite(8,  LOW);
    delay(100);
  }
  delay(100);
}


void receiveEvent(int howMany) {// Function setted before as callback for I2C events
  if (howMany == 1) {           // If there is only 1 byte waiting on the bus
    if (Wire.read() == 222) {
      connected = true;         // Start the animation (see "loop")
    } else{
      connected = false;        // Stop the animation (see "loop")
      first = true;             // Play the startup animation at the next connection (see "loop")
    }
  } else {
    // Serial.println("Recieved :");
    dataRecevied1 = Wire.read();              // Catch the 1st byte
    // Serial.println(dataRecevied1);
    dataRecevied2 = Wire.read();              // Catch the 2nd byte
    // Serial.println(dataRecevied2);
    SpeedRight = (dataRecevied1 - 128) << 1;  // Deleting the shift to have the real value within -255 and +255
    SpeedLeft = (dataRecevied2 - 128) << 1;   //
    if (SpeedRight >= 0)
    {
      digitalWrite(RightBackwardPin, LOW);        // Setting the future unused pin to 0 to prevent from short-circuit
      analogWrite(RightForwardPin, SpeedRight);   // Setting the speed value to the motor pin
    }
    else
    {
      digitalWrite(RightForwardPin, LOW);         // Setting the future unused pin to 0 to prevent from short-circuit
      analogWrite(RightBackwardPin, -SpeedRight); // Setting the speed value to the motor pin, the "-" operator is here to get a positive value
    }
    if (SpeedLeft >= 0)
    {
      digitalWrite(LeftBackwardPin, LOW);         // Setting the future unused pin to 0 to prevent from short-circuit
      analogWrite(LeftForwardPin, SpeedLeft);     // Setting the speed value to the motor pin
    }
    else
    {
      digitalWrite(LeftForwardPin, LOW);          // Setting the future unused pin to 0 to prevent from short-circuit
      analogWrite(LeftBackwardPin, -SpeedLeft);   // Setting the speed value to the motor pin, the "-" operator is here to get a positive value
    }
    // Serial.println("Data completed");
  }
}


