package lessons.maze;

import java.awt.Color;

import jlm.bugglequest.AbstractBuggle;
import jlm.bugglequest.Buggle;
import jlm.bugglequest.Direction;
import jlm.bugglequest.World;
import jlm.exception.AlreadyHaveBaggleException;
import jlm.exception.NoBaggleUnderBuggleException;

import lessons.Lesson;
import lessons.ExerciseTemplated;

public class PledgeMaze extends ExerciseTemplated {

	public PledgeMaze(Lesson lesson) {
		super(lesson);
		name = "Perdu au milieu des îlots";
		tabName = "PledgeMaze";
				
		/* Create initial situation */
		World myWorld = new World("Labyrinth",1,1);

		new Buggle(myWorld, "Thésée", 4, 10, Direction.NORTH, Color.black, Color.lightGray);
		setup(myWorld);
	}

	// to shorten loading time	
	@Override
	protected void computeAnswer(){
		AbstractBuggle b = answerWorld[0].buggles().next();
		b.setPos(11, 5);
		try {
			b.pickUpBaggle();
		} catch (NoBaggleUnderBuggleException e) {
			e.printStackTrace();
		} catch (AlreadyHaveBaggleException e) {
			e.printStackTrace();
		}		
	}
}
