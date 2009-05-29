package lessons.meta;

import jlm.lesson.ExerciseTemplated;
import jlm.lesson.Lesson;

public class MetaExercise extends ExerciseTemplated {
	boolean error = false;

	public MetaExercise(Lesson lesson) {
		super(lesson);
	}
	@Override
	public void reset() {
		error = false;
		super.reset();
	}
	
	@Override
	public boolean check() {
		return !error;
	}
}
