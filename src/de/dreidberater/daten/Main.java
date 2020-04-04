package de.dreidberater.daten;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {

	private static final int INITIAL_COLUMN_COUNT = 6;

	public static void main(String[] args) throws Exception {
		System.out.println("Loading " + args[0]);
		final Table t = Table.load(new File(args[0]));

		final File output = new File(new File(args[0]).getParentFile(), "beraterdaten.php");
		try (FileOutputStream out = new FileOutputStream(output)) {
			final List<Question> questions = loadQuestions(t);
			final List<Row> rows = loadRows(t, questions);
			final Set<Result> results = extractResults(rows);

			final PhpPrinter p = new PhpPrinter(new OutputStreamWriter(out, "UTF-8"));

			p.printHeader();
			p.startQuestions();
			for (final Question q : questions) {
				p.addQuestion(q);
			}
			p.startResults();
			for (final Result r : results) {
				p.addResult(r);
			}
			p.startTable();
			for (final Row r : rows) {
				p.addTableRow(r);
			}
			p.printFooter();
		}
		System.out.println("Written to " + output);
	}

	private static List<Question> loadQuestions(Table t) {
		final Map<String, Question> ret = new LinkedHashMap<>();
		final int maxAnswerCount = determineMaxAnswerCount(t);
		int i = INITIAL_COLUMN_COUNT;
		while (!t.get(0, i).isEmpty()) {
			final String title = t.get(1, i);
			final Question q = new Question(
					makeKey(title),
					title,
					t.get(0, i));
			for (int j = 0; j < maxAnswerCount; j++) {
				if (!t.get(2 + j, i).isEmpty()) {
					q.addAnswer(parseAnswer(t, 2 + j, i));
				}
			}
			if (ret.containsKey(q.getKey())) {
				throw new RuntimeException("Doppelter Schlüssel: " + q.getKey());
			}
			ret.put(q.getKey(), q);
			i += 2;
		}
		return new ArrayList<>(ret.values());
	}

	private static Answer parseAnswer(Table t, int row, int col) {
		return new Answer(
				makeKey(t.get(row, col)),
				t.get(row, col),
				t.get(row, col + 1));
	}

	private static String makeKey(String title) {
		return title.toLowerCase().replace(" ", "").replace("\"", "").replace("\'", "").replace("\\", "");
	}

	private static int determineMaxAnswerCount(Table t) {
		int count = 0;
		while (t.get(2 + count, 0).startsWith("Antwort ")) {
			count++;
		}
		return count;
	}

	private static List<Row> loadRows(Table t, List<Question> questions) {
		final List<Row> rows = new ArrayList<>();
		int row = 7;
		while (!t.get(row, 0).isEmpty()) {
			rows.add(new Row(loadResult(t, row), loadAnswers(t, row, questions)));
			row++;
		}
		return rows;
	}

	private static Result loadResult(Table t, int row) {
		final String title = t.get(row, 0);
		final String text = t.get(row, 1);
		final Map<String, String> furtherValues = new LinkedHashMap<>();
		furtherValues.put("price", t.get(row, 3));
		furtherValues.put("surface", t.get(row, 4));
		furtherValues.put("hardness", t.get(row, 5));
		return new Result(makeKey(title), title, text, furtherValues);
	}

	private static List<List<Answer>> loadAnswers(Table t, int row, List<Question> questions) {
		final List<List<Answer>> ret = new ArrayList<>();
		for (int questionNumber = 0; questionNumber < questions.size(); questionNumber++) {
			final Question q = questions.get(questionNumber);
			final String answerString = t.get(row, INITIAL_COLUMN_COUNT + questionNumber * 2);
			if (!answerString.isEmpty()) {
				final List<Answer> answers = new ArrayList<>();
				final String[] parts = answerString.split("\\|");
				for (final String part : parts) {
					answers.add(q.getAnswer(makeKey(part)));
				}
				ret.add(answers);
			}
		}
		return ret;
	}

	private static Set<Result> extractResults(List<Row> rows) {
		final LinkedHashSet<Result> ret = new LinkedHashSet<Result>();
		for (final Row row : rows) {
			ret.add(row.getResult());
		}
		return ret;
	}

}
