package de.dreidberater.daten;

import java.util.List;

public class Row {

	private final List<List<Answer>> answers;
	private final Result result;

	public Row(Result result, List<List<Answer>> answers) {
		this.result = result;
		this.answers = answers;
	}

	public List<List<Answer>> getAnswers() {
		return this.answers;
	}

	public Result getResult() {
		return this.result;
	}

}
