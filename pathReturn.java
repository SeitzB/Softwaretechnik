package svgExtractionTest;

public class pathReturn
{
	Coordinate Coordinate;
	int pos;

	public pathReturn(Coordinate Coordinate, int pos)
	{
		this.Coordinate = Coordinate;
		this.pos = pos;
	}

	public Coordinate getCoordinate()
	{
		return Coordinate;
	}

	public int getPos()
	{
		return pos;
	}

}
