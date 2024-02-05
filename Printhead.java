import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;

public class Printhead {

	private boolean penUp;
	private float coords[];
	EV3LargeRegulatedMotor Motor = new EV3LargeRegulatedMotor(MotorPort.B);

	public Printhead() {
		setPenUp(true);
		setCoords(new float[] { 0, 0 });

	}

	/**
	 * @return the penUp
	 */
	public boolean getPenUp() {
		return penUp;
	}

	/**
	 * @param penUp the penUp to set
	 */
	public void setPenUp(boolean penUp) {
		this.penUp = penUp;
	}

	/**
	 * @return the coords
	 */
	public float[] getCoords() {
		return coords;
	}

	/**
	 * @param coords the coords to set
	 */
	public void setCoords(float coords[]) {
		this.coords = coords;
	}

	public void togglePen() {
		if (penUp) {
			// TODO Stift Runter Code
		} else {
			// TODO Stift Hoch Code
		}
	}

}
