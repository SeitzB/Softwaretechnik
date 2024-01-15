

public class Object
{
	protected printInstruction Coordinates[];
	protected int counter;

	Object(printInstruction data[])
	{
		counter = -1;
		Coordinates = data;
	}

	public printInstruction giveNextInstruction()
	{
		counter++;
		return Coordinates[counter];
	}

}
