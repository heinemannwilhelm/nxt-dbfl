package bridgeFieldControl;

import nxtPyhtonBridge.Field;

public class FieldGame extends Field {

	public static int unknown; // Felder die noch nicht geändert wurden
	public static int objects; // Anzahl der gefunden Hindernisse

	public FieldGame(int x, int y, int direction, double distance,
			int[] listID, int[] stopOnID) {
		super(x, y, direction, distance, listID, stopOnID);
		unknown = Field.size_x * Field.size_y;
	}

	public static boolean isFreeFromBricks(int id, int x, int y) {
		for (int i = 0; i < BrickGame.bricks.size(); i++) {
			if (i != id) {
				if (!BrickGame.bricks.get(i).is_free(x, y)) {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean isFree(int id, int x, int y) {
		if (!inField(x, y) || !isFreeFromBricks(id, x, y) || theField[x][y] == 2) {
			return false;
		}
		return true;
	}

	public static void setField(int id, int x, int y, int my_id)
			throws Exception {
		if (id < 0 || id > 2 || !inField(x, y)) {
			return;
		}

		if (id > 0 && theField[x][y] == 0) {
			unknown--;
			System.out
					.println("main: there are " + unknown + " unknown fields");
		}
		if (id == 0 && theField[x][y] != 0) {
			unknown++;
			System.out
					.println("main: there are " + unknown + " unknown fields");
		}
		if (id != 2 && theField[x][y] == 2) {
			objects--;
			System.out.println("main: there are " + unknown
					+ " blocked fields found");
		}
		if (id == 2 && theField[x][y] != 2) {
			objects++;
			System.out.println("main: there are " + unknown
					+ " blocked fields found");
		}
		theField[x][y] = id;
	}

	public static int getMove(int ID) {

		int[] listID = new int[1];
		listID[0] = 0;
		int[] stopOnID = new int[1];
		stopOnID[0] = 0;

		calcWays(BrickGame.bricks.get(ID).pos_x,
				BrickGame.bricks.get(ID).pos_y, listID, stopOnID);

		int fieldID = getBest(ID);

		int[] element = new int[2];
		element[0] = BrickGame.bricks.get(ID).pos_x;
		element[1] = BrickGame.bricks.get(ID).pos_y;

		System.out.println(BrickGame.bricks.get(ID).name
				+ ": new destination: " + listFields.get(fieldID)[0] + "|"
				+ listFields.get(fieldID)[1]);
		return getWayTo(listFields.get(fieldID)[0], listFields.get(fieldID)[1],
				theWayField);
	}

	public static int getBest(int ID) {

		int result = listFields.size() - 1;
		double points = 0;

		for (int i = 0; i < listFields.size() - 1; i++) {
			double newPoints = BrickGame.bricks.get(ID).start_points;
			newPoints = newPoints + points_for_distance(i, ID);
			newPoints = newPoints + points_for_enemy(i, ID);
			newPoints = newPoints + punkte_blatt_sammmlung(i, ID);

			if (newPoints > points) {
				result = i;
				points = newPoints;
			}
		}

		return result;
	}

	public static double points_for_distance(int fieldID, int ID) {
		return theWayField[listFields.get(fieldID)[0]][listFields.get(fieldID)[1]].distance
				* BrickGame.bricks.get(ID).distance_from_me;
	}

	public static double points_for_enemy(int fieldID, int ID) {

		double points = 0;

		for (int i = 0; i < BrickGame.bricks.size(); i++) {
			if (i != ID) {
				points = points
						+ (BrickGame.bricks.get(ID).distance_from_enemy / (BrickGame.bricks
								.size() - 1))
						* getManhattanDistance(listFields.get(fieldID)[0],
								listFields.get(fieldID)[1],
								BrickGame.bricks.get(i).pos_x,
								BrickGame.bricks.get(i).pos_y);
			}
		}

		return points;
	}

	private static double punkte_blatt_sammmlung(int fieldID, int ID) {
		double punkte = 0;
		for (int i = 0; i < listFields.size(); i++) {
			if (fieldID != i) {
				punkte = punkte
						+ BrickGame.bricks.get(ID).distance_to_other_base
						* Math.pow(
								BrickGame.bricks.get(ID).distance_to_other_multi,
								getManhattanDistance(
										listFields.get(fieldID)[0],
										listFields.get(fieldID)[1],
										listFields.get(i)[0],
										listFields.get(i)[1]));
			}
		}
		return punkte;
	}

}
