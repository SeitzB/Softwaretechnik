package Objects;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.SensorMode;

public class Plotter
{
	public Plotter()
	{
		EV3LargeRegulatedMotor XYMotors[] = { new EV3LargeRegulatedMotor(MotorPort.A),
				new EV3LargeRegulatedMotor(MotorPort.C) };
		EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S1);
		colorSensor.setFloodlight(true); // initialisierung Farbsensor
		SensorMode redColorSensor = colorSensor.getRedMode();
		EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S2);
		SensorMode touchSensorTouch = touchSensor.getTouchMode();
		homing(XYMotors, redColorSensor, touchSensorTouch);
	}

	public static void fahre(double streckeInMM, double zeit, EV3LargeRegulatedMotor m, Port port)
	{

		double durchmesserReifen = 41;
		if (port.equals(MotorPort.A))
		{
			durchmesserReifen = 40;
		} else if (port.equals(MotorPort.C))
		{
			durchmesserReifen = 43.2;
		} else
		{
			return;
		}

		double umfangReifen = durchmesserReifen * Math.PI;
		double umdrehungenReifen = streckeInMM / umfangReifen;
		int zahnrad1 = 36;
		int zahnrad2 = 12;
		double uebersetzungsVerhaetlnis = (double) zahnrad1 / zahnrad2;
		double umdrehungenMotor = umdrehungenReifen * uebersetzungsVerhaetlnis;
		double gradMotor = umdrehungenMotor * 360;

		double gradProSekunde = gradMotor / zeit;

		m.setSpeed((float) gradProSekunde);
		m.rotate(((int) Math.round(gradMotor)));

		m.close();
	}

	public static void fahre_mmSec(double streckeInMM, double mmSec, EV3LargeRegulatedMotor m, Port port)
	{
		double zeit = streckeInMM / mmSec;
		fahre(streckeInMM, zeit, m, port);
	}

	public static void hypo(final double x, final double y, double mmSec, final EV3LargeRegulatedMotor XYMotors[])
			throws InterruptedException
	{
		double hypo = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		double SecMm = 1 / mmSec;
		final double seconds = SecMm * hypo;
		// double seconds = mmSec / hypo;
		Thread thread1 = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				fahre(x, seconds, XYMotors[0], MotorPort.A);
			}
		});
		Thread thread2 = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				fahre(y, seconds, XYMotors[1], MotorPort.C);
			}
		});
		thread1.start();
		thread2.start();
		Thread.sleep((long) (seconds * 1000));

	}

	public static boolean homing(final EV3LargeRegulatedMotor XYMotors[], final SensorMode redColorSensor,
			final SensorMode touchSensorTouch) throws InterruptedException
	{

		Thread colorSensing = new Thread(new Runnable()
		{
			@Override
			public void run()
			{

				float[] sensorVals = new float[redColorSensor.sampleSize()];

				XYMotors[1].setSpeed(500);
				XYMotors[1].backward();
				while (sensorVals[0] < 0.6)
				{
					redColorSensor.fetchSample(sensorVals, 0);
					try
					{
						Thread.sleep(1);
					} catch (InterruptedException error)
					{
						error.printStackTrace();
					}
				}
				XYMotors[1].stop();

			}
		});
		Thread touchSensing = new Thread(new Runnable()
		{
			@Override
			public void run()
			{

				float[] sensorVals = new float[touchSensorTouch.sampleSize()];

				XYMotors[0].setSpeed(500);
				XYMotors[0].forward();
				while (sensorVals[0] == 0.0f)
				{
					touchSensorTouch.fetchSample(sensorVals, 0);
					try
					{
						Thread.sleep(1);
					} catch (InterruptedException error)
					{
						error.printStackTrace();
					}
				}
				XYMotors[0].stop();

			}

		});
		touchSensing.start();
		colorSensing.start();

		return true;
	}

}
