/********************************************************************************
 *!
 * Drawoid.c
 * 
 * General Description: 
 * 
 *
 * @author Gaurav Choudary
 * @author Ashish Tajane
 * @author Shikhar Paliwal
 * @author Rishabh Singhal
 *
********************************************************************************/

#ifndef F_CPU
#define F_CPU 11059200UL
#endif

#include <avr/io.h>
#include <avr/interrupt.h>
#include <util/delay.h>
#include <stdio.h>
#include <stdlib.h>
#include "Motion.c"
#include "LCD.c"
#include "servo.c"
#include "globalVar.h"
#include "bluetooth.c"

//Initialize the devices and thier ports

void init_devices(void){
	cli(); //disable all interrupts
	timer1_init();
	lcd_init();
	uart3_init();
	servo2_pin_config(); 
	motion_port_init();
	sei(); //re-enable interrupts
}

void turn_right(int degree){
	pen_up();
	_delay_ms(100);
	move_straight(245,1);
	right_degrees(degree);
	move_straight(255,0);
	
	pen_down();
}

void turn_left(int degree){
	pen_up();
	_delay_ms(100);
	move_straight(245,1);
	left_degrees(degree);
	move_straight(245,0);
	_delay_ms(100);
	pen_down();
}

//Main function
int main(void)
{
	//Initialize value array with 0
	int i;
	for(i=0;i<1000;i++){
		val_array[i] = 0;
	}
	
	data_pos = 0;
	bot_pos = 0;
	
	init_devices();

	//LCD_Reset_4bit();
	lcd_cursor(2,1);
	lcd_string("DRAWOID");
	
	while(1){
		if (bot_pos < data_pos){
			lcd_print(1,9,val_array[bot_pos],3);
			lcd_cursor(1,1);
			if (mov_array[bot_pos] == 'F'){
				lcd_string("F");
				move_straight(val_array[bot_pos],1);
			}
			else if (mov_array[bot_pos] == 'B'){
				lcd_string("B");
				move_straight(val_array[bot_pos],0);
			}
			else if (mov_array[bot_pos] == 'R'){
				lcd_string("R");
				turn_right(val_array[bot_pos]);
			}
			else if (mov_array[bot_pos] == 'L'){
				lcd_string("L");
				turn_left(val_array[bot_pos]);
			}
			else if (mov_array[bot_pos] == 'U'){
				lcd_string("U");
				pen_up();
			}
			else if (mov_array[bot_pos] == 'D'){
				lcd_string("D");
				pen_down();
			}
			bot_pos++;
			_delay_ms(1000);
		}
	}

	return 0;
}
