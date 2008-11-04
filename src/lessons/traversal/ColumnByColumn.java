package lessons.traversal;

import java.awt.Color;

import universe.bugglequest.Buggle;
import universe.bugglequest.BuggleWorld;
import universe.bugglequest.Direction;


import lessons.ExerciseTemplated;
import lessons.Lesson;

public class ColumnByColumn extends ExerciseTemplated {

	public ColumnByColumn(Lesson lesson) {
		super(lesson);
		name = "Parcours colonne par colonne";
		tabName = "ColumnByColumn";

		BuggleWorld myWorld = new BuggleWorld("Grid",7,7);
		for (int i=0; i<7;i++) {
			myWorld.getCell(i, 0).putTopWall();
			myWorld.getCell(0, i).putLeftWall();
		}
		
		new Buggle(myWorld, "Walker", 0, 0, Direction.NORTH, Color.black, Color.red);

		setup(myWorld);
	}
}
