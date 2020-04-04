package de.dreidberater.daten;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Table {

	private final String[][] data;

	private Table(String[][] d) {
		this.data = d;
	}

	public static Table load(File f) throws IOException {
		final List<String[]> list = new ArrayList<>();
		try (FileInputStream fi = new FileInputStream(f)) {
			final BufferedReader r = new BufferedReader(new InputStreamReader(fi, "UTF-8"));
			String line;
			while ((line = r.readLine()) != null) {
				list.add(handleQuotes(line.split(";")));
			}
		}
		return new Table(list.toArray(new String[list.size()][]));
	}

	private static String[] handleQuotes(String[] split) {
		for (int i = 0; i < split.length; i++) {
			final String s = split[i];
			if (s.startsWith("\"") && s.endsWith("\"")) {
				split[i] = s.substring(1, s.length() - 1).replace("\"\"", "\"");
			}
		}
		return split;
	}

	public String get(int row, int col) {
		if (row >= this.data.length) {
			return "";
		}
		if (col >= this.data[row].length) {
			return "";
		}
		return this.data[row][col];
	}

}
