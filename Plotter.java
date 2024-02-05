
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.SensorMode;

public class Plotter
{
	EV3LargeRegulatedMotor XYMotors[] = { new EV3LargeRegulatedMotor(MotorPort.D),
			new EV3LargeRegulatedMotor(MotorPort.C) };
	Coordinate coords;
	EV3ColorSensor colorSensor;
	SensorMode redColorSensor;
	EV3TouchSensor touchSensor;
	SensorMode touchSensorTouch;
	boolean initialised = false;
	Printhead printhead = new Printhead();

	public Plotter()
	{

	}

	public void init()
	{

		colorSensor = new EV3ColorSensor(SensorPort.S1);
		colorSensor.setFloodlight(true); // initialisierung Farbsensor
		redColorSensor = colorSensor.getRedMode();
		touchSensor = new EV3TouchSensor(SensorPort.S2);
		touchSensorTouch = touchSensor.getTouchMode();
		try
		{
			homing();
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initialised = true;
	}

	public void close()
	{
		XYMotors[0].close();
		XYMotors[1].close();

	}

	private static void fahre(double streckeInMM, double zeit, EV3LargeRegulatedMotor m, Port port)
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

	}

	private static void fahre_mmSec(double streckeInMM, double mmSec, EV3LargeRegulatedMotor m, Port port)
	{
		double zeit = streckeInMM / mmSec;
		fahre(streckeInMM, zeit, m, port);
	}

	private static void hypo(final double x, final double y, double mmSec, final EV3LargeRegulatedMotor XYMotors[])
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

	/**
	 * Method that zeros the position of the Plotter
	 */
	public boolean homing() throws InterruptedException
	{
		final EV3LargeRegulatedMotor XYMotors[] = this.XYMotors;
		final SensorMode redColorSensor = this.redColorSensor;
		final SensorMode touchSensorTouch = this.touchSensorTouch;

		LCD.clear();
		LCD.drawString("homing in progress", 0, 0);
		LCD.refresh();

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
				XYMotors[1].resetTachoCount();

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
				XYMotors[0].resetTachoCount();

			}

		});
		touchSensing.start();
		colorSensing.start();
		coords = new Coordinate(0, 0);
		printhead.setCoords(new float[] { 0, 0 });
		return true;
	}

	/**
	 * Draws a rectangle needs some work, but should be deprecated
	 * 
	 * @param coordinate 1
	 * @param coordinate 2
	 */
	public void dRect(Coordinate c1, Coordinate c2)
	{
		if (!initialised)
		{
			return;
		}
	}

	/**
	 * Draws all kinds of Paths TODO needs Parameters and Code
	 */
	public void dPath()
	{
		if (!initialised)
		{
			return;
		}
	}

	/**
	 * Draws a hypotenuse with xy coords and speed, does this relative always ONLY
	 * USE for Testing TODO should be private also
	 * 
	 * @param x
	 * @param y
	 * @param mmSec
	 */
	public void dHypo(double x, double y, double mmSec)
	{
		if (!initialised)
		{
			return;
		}
		try
		{
			this.hypo(x, y, mmSec, XYMotors);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void logCoords()
	{

	}

}
