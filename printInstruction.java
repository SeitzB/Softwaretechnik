package Objects;

public class printInstruction
{
	private float x;
	private float y;
	private boolean last;
	private boolean print;

	printInstruction(float x, float y, boolean print, boolean last)
	{
		this.x = x;
		this.y = y;
		this.last = last;
		this.print = print;
	}

	public float giveX()
	{
		return x;
	}

	public float giveY()
	{
		return y;
	}

	public boolean givePos()
	{
		return last;
	}

	public boolean givePrint()
	{
		return print;
	}
}
