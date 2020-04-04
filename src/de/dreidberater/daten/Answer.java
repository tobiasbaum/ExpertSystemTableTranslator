package de.dreidberater.daten;

public class Answer {

	private final String key;
	private final String title;
	private final String text;
	private Question question;

	public Answer(String key, String title, String text) {
		this.key = key;
		this.title = title;
		this.text = text;
	}

	public String getKey() {
		return this.key;
	}

	public String getTitle() {
		return this.title;
	}

	public String getText() {
		return this.text;
	}

	public String getImg() {
		// TODO Auto-generated method stub
		return "img";
	}

	void setQuestionHelper(Question q) {
		this.question = q;
	}

	public Question getQuestion() {
		return this.question;
	}

}
