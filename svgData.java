

public class svgData
{
	private int pageWidth;
	private int pageLength;
	private Object Objects[];
	int counter;

	svgData(int pageWidth, int pageLength, Object Objects[])
	{
		this.pageWidth = pageWidth;
		this.pageLength = pageLength;
		this.Objects = Objects;

		counter = -1;
	}

	public int givePageWidth()
	{
		return pageWidth;
	}

	public int givePageLength()
	{
		return pageLength;
	}

	public int giveAmountOfObjects()
	{
		return Objects.length;
	}

	public Object giveNextObject()
	{
		counter++;
		return Objects[counter];
	}

}
