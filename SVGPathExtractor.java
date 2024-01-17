package svgExtractionTest;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class SVGPathExtractor
{

	public static void main(String[] args)
	{
		try
		{
			String svgFilePath = "Zeichnung.svg"; // Replace with the actual path to your SVG file
			File svgFile = new File(svgFilePath);

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(svgFile);

			NodeList pathElements = doc.getElementsByTagName("path");
			NodeList rectangles = doc.getElementsByTagName("rect");
			for (int i = 0; i < pathElements.getLength(); i++)
			{
				Element pathElement = (Element) pathElements.item(i);
				String pathData = pathElement.getAttribute("d");
				System.out.println("Path " + (i + 1) + ": " + pathData);
			}

			for (int i = 0; i < rectangles.getLength(); i++)
			{
				Element rectangle = (Element) rectangles.item(i);
				String width = rectangle.getAttribute("width");
				String height = rectangle.getAttribute("height");
				String x = rectangle.getAttribute("x");
				String y = rectangle.getAttribute("y");

				System.out.println(
						"Rectangle " + (i + 1) + ": width:" + width + " height: " + height + " x: " + x + " y: " + y);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	Coordinate[] pathHelper(String data)
	{
		int arrLines = getStringLines(data);
		int arrPos = 0;
		Coordinate[] Coordinates = new Coordinate[arrLines];

		int pos = 0;

		for (int i = 0; i < data.length(); i++)
		{
			if (data.charAt(pos) == 'm' || data.charAt(pos) == 'M')
			{
				pathReturn output = pathInit(pos, data);
				pos = output.getPos();
				Coordinates[arrPos] = output.getCoordinate();
				arrPos++;
			} else if (data.charAt(pos) == 'h')
			{
				pathReturn output = pathHorizontelRelative(pos, data, Coordinates[arrPos - 1]);
				pos = output.getPos();
				Coordinates[arrPos] = output.getCoordinate();
				arrPos++;
			} else if (data.charAt(pos) == 'H')
			{
				pathReturn output = pathHorizontelAbsolute(pos, data, Coordinates[arrPos - 1]);
				pos = output.getPos();
				Coordinates[arrPos] = output.getCoordinate();
				arrPos++;
			} else if (data.charAt(pos) == 'v')
			{
				pathReturn output = pathVerticalRelative(pos, data, Coordinates[arrPos - 1]);
				pos = output.getPos();
				Coordinates[arrPos] = output.getCoordinate();
				arrPos++;
			} else if (data.charAt(pos) == 'V')
			{
				pathReturn output = pathVerticalAbsolute(pos, data, Coordinates[arrPos - 1]);
				pos = output.getPos();
				Coordinates[arrPos] = output.getCoordinate();
				arrPos++;
			} else if (data.charAt(pos) == 'l')
			{
				pathReturn output = pathRelative(pos, data, Coordinates[arrPos - 1]);
				pos = output.getPos();
				Coordinates[arrPos] = output.getCoordinate();
				arrPos++;
			} else if (data.charAt(pos) == 'L')
			{
				pathReturn output = pathAbsolute(pos, data);
				pos = output.getPos();
				Coordinates[arrPos] = output.getCoordinate();
				arrPos++;
			} else if (data.charAt(pos) == 'z' || (data.charAt(pos) == 'Z'))
			{
				break;
			}

			pos++;
		}

		return Coordinates;

	}

	pathReturn pathInit(int pos, String data)
	{
		pos = pos + 2;
		String x = "";
		String y = "";

		while (data.charAt(pos) != 44)
		{
			x = x + data.charAt(pos);
			pos++;
		}

		pos++;

		while (data.charAt(pos) != 39)
		{
			y = y + data.charAt(pos);
			pos++;
		}

		pos++;

		return new pathReturn(convertStringsToCoordinate(x, y), pos);
	}

	pathReturn pathHorizontelRelative(int pos, String data, Coordinate previous)
	{
		pos = pos + 2;
		String tempX = "";

		while (data.charAt(pos) != 39)
		{
			tempX = tempX + data.charAt(pos);
			pos++;
		}

		float finishedX = Float.parseFloat(tempX) + previous.giveX();

		Coordinate finishedCoordinate = new Coordinate(finishedX, previous.giveY());

		pos++;

		return new pathReturn(finishedCoordinate, pos);

	}

	pathReturn pathVerticalRelative(int pos, String data, Coordinate previous)
	{
		pos = pos + 2;
		String tempY = "";

		while (data.charAt(pos) != 39)
		{
			tempY = tempY + data.charAt(pos);
			pos++;
		}

		float finishedY = Float.parseFloat(tempY) + previous.giveY();

		Coordinate finishedCoordinate = new Coordinate(previous.giveX(), finishedY);

		pos++;

		return new pathReturn(finishedCoordinate, pos);

	}

	pathReturn pathHorizontelAbsolute(int pos, String data, Coordinate previous)
	{
		pos = pos + 2;
		String tempX = "";

		while (data.charAt(pos) != 39)
		{
			tempX = tempX + data.charAt(pos);
			pos++;
		}

		Coordinate finishedCoordinate = new Coordinate(Float.parseFloat(tempX), previous.giveY());

		pos++;

		return new pathReturn(finishedCoordinate, pos);

	}

	pathReturn pathVerticalAbsolute(int pos, String data, Coordinate previous)
	{
		pos = pos + 2;
		String tempY = "";

		while (data.charAt(pos) != 39)
		{
			tempY = tempY + data.charAt(pos);
			pos++;
		}

		Coordinate finishedCoordinate = new Coordinate(previous.giveX(), Float.parseFloat(tempY));

		pos++;

		return new pathReturn(finishedCoordinate, pos);

	}

	pathReturn pathRelative(int pos, String data, Coordinate previous)
	{
		pos = pos + 2;
		String tempX = "";
		String tempY = "";

		while (data.charAt(pos) != 44)
		{
			tempX = tempX + data.charAt(pos);
			pos++;
		}

		while (data.charAt(pos) != 39)
		{
			tempX = tempX + data.charAt(pos);
			pos++;
		}

		float finishedX = Float.parseFloat(tempX) + previous.giveX();
		float finishedY = Float.parseFloat(tempY) + previous.giveY();

		Coordinate finishedCoordinate = new Coordinate(finishedX, finishedY);

		pos++;

		return new pathReturn(finishedCoordinate, pos);

	}

	pathReturn pathAbsolute(int pos, String data)
	{
		pos = pos + 2;
		String tempX = "";
		String tempY = "";

		while (data.charAt(pos) != 44)
		{
			tempX = tempX + data.charAt(pos);
			pos++;
		}

		while (data.charAt(pos) != 39)
		{
			tempX = tempX + data.charAt(pos);
			pos++;
		}

		Coordinate finishedCoordinate = new Coordinate(Float.parseFloat(tempX), Float.parseFloat(tempY));

		pos++;

		return new pathReturn(finishedCoordinate, pos);

	}

	Coordinate convertStringsToCoordinate(String x, String y)
	{
		return new Coordinate(Float.parseFloat(x), Float.parseFloat(y));
	}

	int getStringLines(String data)
	{
		if (data == null || data.isEmpty())
		{
			return 0;
		} else
		{
			String[] lines = data.split("\\r?\\n");
			return lines.length;
		}
	}
}
