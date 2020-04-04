package de.dreidberater.daten;

import java.util.Map;

public class Result {

	private final String key;
	private final String title;
	private final String text;
	private final Map<String, String> furtherValues;

	public Result(String key, String title, String text, Map<String, String> furtherValues) {
		this.key = key;
		this.title = title;
		this.text = text;
		this.furtherValues = furtherValues;
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

	public Map<String, String> getFurtherValues() {
		return this.furtherValues;
	}

}
