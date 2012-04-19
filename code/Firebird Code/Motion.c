/*!
 * @file
 * Drawoid - 
 * 
 * General Description: 
 *
 * @author Gaurav Choudary
 * @author Rishabh Singhal
 * @author Shikhar Paliwal
 * @author Ashish Tajane
 *
 */
 
#include <avr/io.h>
#include <avr/interrupt.h>
#include <util/delay.h>

#include "globalVar.h"

/*! 
 * Function to configure INT4 (PORTE 4) pin as input for the left position encoder
 */
void left_encoder_pin_config (void)
{
 DDRE  = DDRE & 0xEF;  //Set the direction of the PORTE 4 pin as input
 PORTE = PORTE | 0x10; //Enable internal pullup for PORTE 4 pin
}

/*!
 * Function to configure INT5 (PORTE 5) pin as input for the right position encoder
 */
void right_encoder_pin_config (void)
{
 DDRE  = DDRE & 0xDF;  //Set the direction of the PORTE 4 pin as input
 PORTE = PORTE | 0x20; //Enable internal pullup for PORTE 4 pin
}

/*!
 * This function enables interrupt 4
 */
void left_position_encoder_interrupt_init (void)
{
 //cli(); //Clears the global interrupt
 EICRB = EICRB | 0x02; // INT4 is set to trigger with falling edge
 EIMSK = EIMSK | 0x10; // Enable Interrupt INT4 for left position encoder
 //sei();   // Enables the global interrupt 
}

/*!
 * This function enables interrupt 5
 */
void right_position_encoder_interrupt_init (void)
{
 //cli(); //Clears the global interrupt
 EICRB = EICRB | 0x08; // INT5 is set to trigger with falling edge
 EIMSK = EIMSK | 0x20; // Enable Interrupt INT5 for right position encoder
 //sei();   // Enables the global interrupt 
}

/*!
 * Initialize the motor ports
 */
void motion_pin_config (void) 
{
 DDRA = DDRA | 0x0F;
 PORTA = PORTA & 0xF0;
 DDRL = DDRL | 0x18;   // Setting PL3 and PL4 pins as output for PWM generation 
 PORTL = PORTL | 0x18; // PL3 and PL4 pins are for velocity control using PWM. 
}

/*!
 * Function to initialize all the devices
 */
 
void motion_port_init()
{
 motion_pin_config(); //robot motion pins config
 left_encoder_pin_config(); //left encoder pin config
 right_encoder_pin_config(); //right encoder pin config	
 left_position_encoder_interrupt_init();
 right_position_encoder_interrupt_init();
}

/*! 
 * ISR for right position encoder
 */
ISR(INT5_vect)  
{
 ShaftCountRight++;  //increment right shaft position count
}


/*!
 * ISR for left position encoder
 */
ISR(INT4_vect)
{
 ShaftCountLeft++;  //increment left shaft position count
}


/*! 
 * This function moves the motor in the given direction
 * Direction --> 1 (forward)
 * Direction --> 0 (backward)
 */
void motion_set (unsigned char Direction)
{
 unsigned char PortARestore = 0;

 Direction &= 0x0F; 		// removing upper nibbel for the protection
 PortARestore = PORTA; 		// reading the PORTA original status
 PortARestore &= 0xF0; 		// making lower direction nibbel to 0
 PortARestore |= Direction; // adding lower nibbel for forward command and restoring the PORTA status
 PORTA = PortARestore; 		// executing the command
}

void forward (void) /*! both wheels forward */
{
  motion_set(0x06);
}

void back (void) /*! both wheels backward */
{
  motion_set(0x09);
}

void left (void) /*! Left wheel backward, Right wheel forward */
{
  motion_set(0x05);
}

void right (void) /*! Left wheel forward, Right wheel backward */
{
  motion_set(0x0A);
}

void soft_left (void) /*! Left wheel stationary, Right wheel forward */
{
 motion_set(0x04);
}

void soft_right (void) /*! Left wheel forward, Right wheel is stationary */
{
 motion_set(0x02);
}

void soft_left_2 (void) /*! Left wheel backward, right wheel stationary */
{
 motion_set(0x01);
}

void soft_right_2 (void) /*! Left wheel stationary, Right wheel backward */
{
 motion_set(0x08);
}

void stop (void)
{
  motion_set(0x00);
}


/*!
 * This function turns the robot by specified degrees
 */
void angle_rotate(unsigned int Degrees)
{
 float ReqdShaftCount = 0;
 unsigned long int ReqdShaftCountInt = 0;

 ReqdShaftCount = (float) Degrees/ 4.36; // division by resolution to get shaft count
 ReqdShaftCountInt = (unsigned int) ReqdShaftCount;
 ShaftCountRight = 0; 
 ShaftCountLeft = 0; 

 while (1)
 {
  if((ShaftCountRight >= ReqdShaftCountInt) || (ShaftCountLeft >= ReqdShaftCountInt))	//change
  break;
 }
 stop(); //Stop action
}

/*!
 * This function movs the  
 * robot forward by specified distance
 */

void linear_distance_mm(unsigned int DistanceInMM)
{
 float ReqdShaftCount = 0;
 unsigned long int ReqdShaftCountInt = 0;

 ReqdShaftCount = DistanceInMM / 13.58; // division by resolution to get shaft count
 ReqdShaftCountInt = (unsigned long int) ReqdShaftCount;
  
 ShaftCountRight = 0;
 while(1)
 {
  if(ShaftCountRight > ReqdShaftCountInt)//also adding count for left count
  {
  	break;
  }
 } 
 stop(); //Stop action
}

/*! 
 * This function moves the robot forward 
 * by DistanceInMM millimeters
 */
void forward_mm(unsigned int DistanceInMM)
{
 forward();
 linear_distance_mm(DistanceInMM);
}

/*! 
 * This function moves the robot backward 
 * by DistanceInMM millimeters
 */
void back_mm(unsigned int DistanceInMM)
{
 back();
 linear_distance_mm(DistanceInMM);
}

/*! 
 * This function turns the robot to the left
 * by DistanceInMM millimeters
 */
void left_degrees(unsigned int Degrees) 
{
// 80 pulses for 360 degrees rotation 4.510 degrees per count
 left(); //Turn left
 angle_rotate(Degrees);
}

/*! 
 * This function turns the robot to the right
 * by DistanceInMM millimeters
 */
void right_degrees(unsigned int Degrees)
{
// 80 pulses for 360 degrees rotation 4.510 degrees per count
 right(); //Turn right
 angle_rotate(Degrees);
}

/*! 
 * This function turns the robot to the left
 * softly by DistanceInMM millimeters
 */
void soft_left_degrees(unsigned int Degrees)
{
 // 160 pulses for 360 degrees rotation 2.255 degrees per count
 soft_left(); //Turn soft left
 Degrees=Degrees*2;
 angle_rotate(Degrees);
}

/*! 
 * This function turns the robot to the right
 * softly by DistanceInMM millimeters
 * Here it moves the left motor backwards
 * without moving the right motor to acheive
 * the desired movement
 */
void soft_right_degrees(unsigned int Degrees)
{
 // 160 pulses for 360 degrees rotation 2.255 degrees per count
 soft_right_2();  //Turn soft right
 Degrees=Degrees*2;
 angle_rotate(Degrees);
}

/*! 
 * This function turns the robot to the left
 * softly by DistanceInMM millimeters
 * The difference here is that is moves the right motor
 * forwards without moving the left motor
 */
void soft_left_2_degrees(unsigned int Degrees)
{
 // 160 pulses for 360 degrees rotation 2.255 degrees per count
 soft_left_2(); //Turn reverse soft left
 Degrees=Degrees*2;
 angle_rotate(Degrees);
}

/*! 
 * This function turns the robot to the left
 * softly by DistanceInMM millimeters
 * It acheives this move by moving the left motor
 * forwards without moving the left motor
 */
void soft_right_2_degrees(unsigned int Degrees)
{
 // 160 pulses for 360 degrees rotation 2.255 degrees per count
 soft_right_2();  //Turn reverse soft right
 Degrees=Degrees*2;
 angle_rotate(Degrees);
}

/*!
 * This function moves the robot by dist mm according
 * to the direction, i.e. 1 -> forwards,
 * 0 -> backwards
 */
void move_straight(unsigned int dist,int direction)  //moves the bot in the given direction
{											//1 - forward    0 - backward
	if(direction == 1)
	{
		forward_mm(dist);
	}
	else
	{
		back_mm(dist);
	}
	stop();
}

