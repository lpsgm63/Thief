#include <SoftwareSerial.h>


#define TX 7  
#define RX 8
#define button1 6
#define button2 10
#define cds A0
#define max 100
#define second 1000
#define benchmark 75
#define fail '0'
#define success '1'
#define start '2'


int buttonStatus1, buttonStatus2;
int cds_val;
int min_val;
int flag;

SoftwareSerial bluetooth(RX, TX);

void setup() {
  Serial.begin(9600);
  bluetooth.begin(9600);
  //버튼설정
  pinMode(button1, INPUT);
  pinMode(button2, INPUT);
  //초기화
  cds_val = 0;
  min_val = max;
  flag = 0;

}

void loop() {
  // put your main code here, to run repeatedly:
  buttonStatus1 = digitalRead(button1);  
    if (buttonStatus1 == HIGH) {
    while(1){
      //Serial.println("hello");
      //한번만 bluetooth 메시지 전송
      if(!flag){
          bluetooth.write(start);
          Serial.println("Start");
      }
      flag=1;
      cds_val = map(analogRead(A0), 0, 1023, 0, 100);
      cds_val = constrain(cds_val, 0, 100);
      Serial.print("CDS=");
      Serial.println(cds_val);
      if (cds_val < min_val) {
          min_val = cds_val;
      }
      delay(second/2);
      buttonStatus2 = digitalRead(button2);
      if (buttonStatus2 == HIGH) {
          Serial.println("End");
          //조도 최소값 반환
          Serial.print("Mininum CDS Value:");
          Serial.println(min_val);

          //변경여부확인
          if (min_val < benchmark) {
            Serial.println("FAIL");
            bluetooth.write(fail);
          }
          else {
            Serial.println("SUCCESS");
            bluetooth.write(success);
          }
          buttonStatus1 = LOW;
          buttonStatus2 = LOW;
          cds_val = 0;
          min_val = max;
          flag = 0;
          break;
      }
    }
  }
}
