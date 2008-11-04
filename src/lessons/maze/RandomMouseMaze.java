package lessons.maze;

import java.awt.Color;

import universe.bugglequest.AbstractBuggle;
import universe.bugglequest.Buggle;
import universe.bugglequest.BuggleWorld;
import universe.bugglequest.Direction;
import universe.bugglequest.exception.AlreadyHaveBaggleException;
import universe.bugglequest.exception.NoBaggleUnderBuggleException;


import lessons.Lesson;
import lessons.ExerciseTemplated;

public class RandomMouseMaze extends ExerciseTemplated {

	public RandomMouseMaze(Lesson lesson) {
		super(lesson);
		name = "La souris folle";
		tabName = "RandomMouseMaze";
				
		/* Create initial situation */
		BuggleWorld myWorld = new BuggleWorld("Labyrinth",4,4);
		new Buggle(myWorld, "Thésée", 0, 3, Direction.NORTH, Color.black, Color.lightGray);
		setup(myWorld);
	}

	// Skip random solver since it might take too much time to find solution.
	@Override
	protected void computeAnswer(){
		AbstractBuggle b = (AbstractBuggle)answerWorld[0].entities().next();
		b.setPos(3, 3);
		b.turnBack();
		
		try {
			b.pickUpBaggle();
		} catch (NoBaggleUnderBuggleException e) {
			e.printStackTrace();
		} catch (AlreadyHaveBaggleException e) {
			e.printStackTrace();
		}	
	}
	
}
