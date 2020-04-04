package de.dreidberater.daten;

import java.util.ArrayList;
import java.util.List;

public class Question {

	private final String key;
	private final String title;
	private final String text;
	private final List<Answer> answers;

	public Question(String key, String title, String text) {
		this.key = key;
		this.title = title;
		this.text = text;
		this.answers = new ArrayList<>();
	}

	public String getTitle() {
		return this.title;
	}

	public String getText() {
		return this.text;
	}

	public String getKey() {
		return this.key;
	}

	void addAnswer(Answer a) {
		a.setQuestionHelper(this);
		this.answers.add(a);
	}

	public List<Answer> getAnswers() {
		return this.answers;
	}

	public Answer getAnswer(String answerKey) {
		for (final Answer a : this.answers) {
			if (a.getKey().equals(answerKey)) {
				return a;
			}
		}
		throw new RuntimeException("Antwort mit Schlüssel " + answerKey + " in Frage " + this.key + " nicht gefunden");
	}

}
