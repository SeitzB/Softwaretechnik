package Objects;

public class Polygon extends Object
{
	Polygon(Coordinate data[])
	{
		super(configureCoordinates(data));
	}

	private static printInstruction[] configureCoordinates(Coordinate data[])
	{
		printInstruction processedData[] = new printInstruction[5];

		processedData[0] = new printInstruction(data[0].giveX(), data[0].giveY(), false, false);

		for (int i = 1; i < data.length - 1; i++)
		{
			processedData[i] = new printInstruction(data[i].giveX(), data[i].giveY(), true, false);
		}

		int maxLength = data.length - 1;

		processedData[maxLength] = new printInstruction(data[maxLength].giveX(), data[maxLength].giveY(), true, true);

		return processedData;

	}

}
