package Objects;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.SensorMode;

public class main{

	public static void main(String[] args) throws InterruptedException
	{

		Plotter plotter = new Plotter();
		plotter.hypo(-100, 20, 50, XYMotors);
		
	}

	

}
/*
 * 12 Zähne -> 36 Zähne 40mm Umfang 12 Zähne -> 36 Zähne
 * 
 * 1357,17mm
 */
//3*2,5```#