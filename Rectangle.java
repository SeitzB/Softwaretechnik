
class Rectangle extends Object {
	Coordinate startPoint;
	float width;
	float height;

	Rectangle(Coordinate startPoint, float width, float height) {
		super(configureCoordinates(startPoint, width, height));
	}

	private static printInstruction[] configureCoordinates(Coordinate startPoint, float width, float height) {
		printInstruction processedData[] = new printInstruction[5];

		processedData[0] = new printInstruction(startPoint.giveX(), startPoint.giveY(), false, false);
		processedData[1] = new printInstruction(startPoint.giveX() + width, startPoint.giveY(), true, false);
		processedData[2] = new printInstruction(startPoint.giveX() + width, startPoint.giveY() - height, true, false);
		processedData[3] = new printInstruction(startPoint.giveX(), startPoint.giveY() - height, true, false);
		processedData[3] = new printInstruction(startPoint.giveX(), startPoint.giveY(), true, true);

		return processedData;

	}
}
