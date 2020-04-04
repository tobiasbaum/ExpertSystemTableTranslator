package de.dreidberater.daten;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class PhpPrinter {

	private final Writer output;

	public PhpPrinter(Writer output) {
		this.output = output;
	}

	public void printHeader() throws IOException {
		this.output.write("<?php\n\n");
	}

	public void startQuestions() throws IOException {
		this.output.write("//die moeglichen Fragen\n" +
				"$questions = array();\n" +
				"\n");
	}

	public void addQuestion(Question q) throws IOException {
		this.output.write("$curQuestion = new EnumQuestion();\n" +
				"$curQuestion->name = \"" + escStr(escHtml(q.getTitle())) + "\";\n" +
				"$curQuestion->description = \"" + escStr(escHtml(q.getText())) + "\";\n");
		for (final Answer a : q.getAnswers()) {
			this.addAnswer(a);
		}
		this.output.write("$questions[\"" + escStr(q.getKey()) + "\"] = $curQuestion;\n\n");
	}

	private void addAnswer(Answer a) throws IOException {
		this.output.write("$curQuestion->addItem(\"" + escStr(a.getKey()) + "\", \"" + escStr(escHtml(a.getTitle())) + "\", <<<'EOD'\n" +
			escHtml(a.getText().replace("#bild", "")) + "<br/> \n");
		if (a.getText().contains("#bild")) {
			this.output.write("<img src=\"" + a.getQuestion().getKey() + "-" + a.getKey() + ".jpg\" /><br/>\n");
		}
		this.output.write("EOD\n" +
			");\n");
	}

	public void startResults() throws IOException {
		this.output.write("//moegliche Ergebnisse\n" +
				"$results = array();\n" +
				"\n" +
				"");
	}

	public void addResult(Result r) throws IOException {
		this.output.write("$curResult = new stdClass();\n" +
				"$curResult->name = \"" + escStr(escHtml(r.getTitle())) + "\";\n" +
				"$curResult->description = <<<'EOD'\n" +
				escHtml(r.getText()) +
				"\nEOD;\n");
		for (final Entry<String, String> e : r.getFurtherValues().entrySet()) {
			this.output.write("$curResult->" + e.getKey() + " = \"" + escStr(e.getValue()) + "\";\n");
		}
		this.output.write("$results[\"" + escStr(r.getKey()) + "\"] = $curResult;\n" +
				"\n");
	}

	public void startTable() throws IOException {
		this.output.write("//Tabelle mit den Entscheidungswerten (Eigenschaften der Ergebnisse)\n" +
				"$decisionTable = array();\n" +
				"$idx = 0;\n" +
				"\n" +
				"");
	}

	public void addTableRow(Row row) throws IOException {
		this.output.write("$decisionTable[$idx][\"Result\"] = \"" + escStr(row.getResult().getKey()) + "\";\n");
		for (final List<Answer> a : row.getAnswers()) {
			final String questionKey = a.get(0).getQuestion().getKey();
			final String answerKeys = a.stream().map(Answer::getKey).collect(Collectors.joining("|"));
			this.output.write("$decisionTable[$idx][\"" + escStr(questionKey) + "\"] = \"" + escStr(answerKeys) + "\";\n");
		}
		this.output.write("$idx++;\n" +
				"\n");
	}

	public void printFooter() throws IOException {
		this.output.write("?>\n");
		this.output.flush();
	}

	private static String escStr(String s) {
		return s.replace("\\", "\\\\").replace("\"", "\\\"");
	}

	private static String escHtml(String s) {
		return s.replace("ä", "&auml;").replace("ö", "&ouml;").replace("ü", "&uuml;")
				.replace("Ä", "&Auml;").replace("Ö", "&Ouml;").replace("Ü", "&Uuml;")
				.replace("ß", "&szlig;");
	}

}
