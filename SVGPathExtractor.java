import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class SVGPathExtractor
{
	public SVGPathExtractor()
	{

	}

	static public List<List<Coordinate>> getData(String path)
	{
		try
		{
			File svgFile = new File(path);

			List<List<Coordinate>> paths = new ArrayList();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(svgFile);

			NodeList pathElements = doc.getElementsByTagName("path");
			for (int i = 0; i < pathElements.getLength(); i++)
			{
				Element pathElement = (Element) pathElements.item(i);
				String pathData = pathElement.getAttribute("d");
				pathData = pathData + 'z';
				paths.add(pathHelper(pathData));
				System.out.println("Path " + (i + 1) + ": " + pathData);
			}
			return paths;
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}

	}

	static List<Coordinate> pathHelper(String data)
	{
		int arrLines = getStringLines(data);
		int arrPos = 0;
		List<Coordinate> Coordinates = new ArrayList();

		char mode = 'm';
		boolean modY = false;

		String number = "";

		int pos = 0;

		while (data.length() - 1 > pos)
		{
			switch (data.charAt(pos))
			{
			case 'm':
			{
				mode = 'm';
				pathReturn output = pathInit(pos, data);
				pos = output.getPos();
				Coordinates.add(output.getCoordinate());
				mode = 'l';
				break;
			}

			case 'M':
			{
				mode = 'M';
				pathReturn output = pathInit(pos, data);
				pos = output.getPos();
				Coordinates.add(output.getCoordinate());
				mode = 'L';
				break;
			}

			case 'h':
				mode = 'h';
				pos++;
				break;

			case 'H':
				mode = 'H';
				pos++;
				break;

			case 'v':
				mode = 'v';
				pos++;
				break;

			case 'V':
				mode = 'V';
				pos++;
				break;

			case 'l':
				mode = 'l';
				pos++;
				break;

			case 'L':
				mode = 'L';
				pos++;
				break;

			case 'z':
				mode = 'z';
				pos++;
				break;

			case 'Z':
				mode = 'Z';
				pos++;
				break;

			case ' ':
				pos++;
				modY = false;
				break;

			default:
				if (mode == 'h')
				{
					pathReturn output = pathHorizontelRelative(pos, data, Coordinates.get(Coordinates.size() - 1));
					pos = output.getPos();
					Coordinates.add(output.getCoordinate());
				} else if (mode == 'H')
				{
					pathReturn output = pathHorizontelAbsolute(pos, data, Coordinates.get(Coordinates.size() - 1));
					pos = output.getPos();
					Coordinates.add(output.getCoordinate());
				} else if (mode == 'v')
				{
					pathReturn output = pathVerticalRelative(pos, data, Coordinates.get(Coordinates.size() - 1));
					pos = output.getPos();
					Coordinates.add(output.getCoordinate());
				} else if (mode == 'V')
				{
					pathReturn output = pathVerticalAbsolute(pos, data, Coordinates.get(Coordinates.size() - 1));
					pos = output.getPos();
					Coordinates.add(output.getCoordinate());
				} else if (mode == 'l')
				{
					pathReturn output = pathRelative(pos, data, Coordinates.get(Coordinates.size() - 1));
					pos = output.getPos();
					Coordinates.add(output.getCoordinate());
				} else if (mode == 'L')
				{
					pathReturn output = pathAbsolute(pos, data);
					pos = output.getPos();
					Coordinates.add(output.getCoordinate());
				}
				pos++;
			}

		}

		return Coordinates;

	}

	static pathReturn pathInit(int pos, String data)
	{
		pos = pos + 2;
		String x = "";
		String y = "";

		while (data.charAt(pos) != ',')
		{
			x = x + data.charAt(pos);
			pos++;
		}

		pos++;

		while (data.charAt(pos) != ' ' && data.charAt(pos) != 'z')
		{
			y = y + data.charAt(pos);
			pos++;
		}

		return new pathReturn(convertStringsToCoordinate(x, y), pos);
	}

	static pathReturn pathHorizontelRelative(int pos, String data, Coordinate previous)
	{
		String tempX = "";

		while (data.charAt(pos) != ' ' && data.charAt(pos) != 'z')
		{
			tempX = tempX + data.charAt(pos);
			pos++;
		}

		float finishedX = Float.parseFloat(tempX) + previous.giveX();

		Coordinate finishedCoordinate = new Coordinate(finishedX, previous.giveY());

		return new pathReturn(finishedCoordinate, pos);

	}

	static pathReturn pathVerticalRelative(int pos, String data, Coordinate previous)
	{
		String tempY = "";

		while (data.charAt(pos) != ' ' && data.charAt(pos) != 'z')
		{
			tempY = tempY + data.charAt(pos);
			pos++;
		}

		float finishedY = Float.parseFloat(tempY) + previous.giveY();

		Coordinate finishedCoordinate = new Coordinate(previous.giveX(), finishedY);

		return new pathReturn(finishedCoordinate, pos);

	}

	static pathReturn pathHorizontelAbsolute(int pos, String data, Coordinate previous)
	{
		String tempX = "";

		while (data.charAt(pos) != ' ' && data.charAt(pos) != 'z')
		{
			tempX = tempX + data.charAt(pos);
			pos++;
		}

		Coordinate finishedCoordinate = new Coordinate(Float.parseFloat(tempX), previous.giveY());

		return new pathReturn(finishedCoordinate, pos);

	}

	static pathReturn pathVerticalAbsolute(int pos, String data, Coordinate previous)
	{
		String tempY = "";

		while (data.charAt(pos) != ' ' && data.charAt(pos) != 'z')
		{
			tempY = tempY + data.charAt(pos);
			pos++;
		}

		Coordinate finishedCoordinate = new Coordinate(previous.giveX(), Float.parseFloat(tempY));

		return new pathReturn(finishedCoordinate, pos);

	}

	static pathReturn pathRelative(int pos, String data, Coordinate previous)
	{
		String tempX = "";
		String tempY = "";

		while (data.charAt(pos) != ',')
		{
			tempX = tempX + data.charAt(pos);
			pos++;
		}

		pos++;

		while (data.charAt(pos) != ' ' && data.charAt(pos) != 'z')
		{
			tempY = tempY + data.charAt(pos);
			pos++;
		}

		float finishedX = Float.parseFloat(tempX) + previous.giveX();
		float finishedY = Float.parseFloat(tempY) + previous.giveY();

		Coordinate finishedCoordinate = new Coordinate(finishedX, finishedY);

		return new pathReturn(finishedCoordinate, pos);

	}

	static pathReturn pathAbsolute(int pos, String data)
	{
		String tempX = "";
		String tempY = "";

		while (data.charAt(pos) != ',')
		{
			tempX = tempX + data.charAt(pos);
			pos++;
		}

		pos++;

		while (data.charAt(pos) != ' ' && data.charAt(pos) != 'z')
		{
			tempY = tempY + data.charAt(pos);
			pos++;
		}

		Coordinate finishedCoordinate = new Coordinate(Float.parseFloat(tempX), Float.parseFloat(tempY));

		return new pathReturn(finishedCoordinate, pos);

	}

	static Coordinate convertStringsToCoordinate(String x, String y)
	{
		return new Coordinate(Float.parseFloat(x), Float.parseFloat(y));
	}

	static int getStringLines(String data)
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
