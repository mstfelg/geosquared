/* 
 GeoGebra - Dynamic Mathematics for Everyone
 http://www.geogebra.org

 This file is part of GeoGebra.

 This program is free software; you can redistribute it and/or modify it 
 under the terms of the GNU General Public License as published by 
 the Free Software Foundation.
 
 */

/*
 * Util.java
 *
 * Created on 17. November 2001, 18:23
 */

package org.geogebra.desktop.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Comparator;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

import org.geogebra.common.jre.util.StreamUtil;
import org.geogebra.common.util.Charsets;
import org.geogebra.common.util.debug.Log;

/**
 *
 * @author Markus Hohenwarter
 */
public class UtilD {

	/**
	 * Adds key listener recursively to all subcomponents of container.
	 * 
	 * @param listener listener
	 */
	public static void addKeyListenerToAll(Container cont, KeyListener listener) {
		cont.addKeyListener(listener);
		Component[] comps = cont.getComponents();
		for (Component comp : comps) {
			if (comp instanceof Container) {
				addKeyListenerToAll((Container) comp, listener);
			} else {
				comp.addKeyListener(listener);
			}
		}
	}

	/**
	 * Writes all contents of the given InputStream to a byte array.
	 * @return bytes from the stream
	 * @throws IOException if I/O problem occurs
	 */
	public static byte[] loadIntoMemory(InputStream is) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		copyStream(is, bos);
		bos.close();
		return bos.toByteArray();
	}

	/**
	 * @param filename filename
	 * @return file content
	 */
	public static String loadFileIntoString(String filename) {
		InputStream ios = null;
		try {
			ios = new FileInputStream(filename);
			return loadIntoString(ios);
		} catch (Exception e) {
			Log.error("problem loading " + filename);
		} finally {
			StreamUtil.closeSilent(ios);
		}
		return null;
	}

	/**
	 * @param filename filename
	 * @return file content
	 */
	public static byte[] loadFileIntoByteArray(String filename) {
		File file = new File(filename);
		byte[] buffer = new byte[(int) file.length()];
		InputStream ios = null;
		try {
			ios = new FileInputStream(file);
			if (ios.read(buffer) == -1) {
				Log.error("problem loading " + filename);
				return null;
			}
			return buffer;
		} catch (RuntimeException | IOException e) {
			Log.error("problem loading " + filename);
		} finally {
			try {
				if (ios != null) {
					ios.close();
				}
			} catch (IOException e) {
				Log.error("problem loading " + filename);
			}
		}
		return null;
	}

	/**
	 * Writes all contents of the given InputStream to a String
	 * @return stream content
	 */
	public static String loadIntoString(InputStream is) {
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(is, Charsets.getUtf8()));
		StringBuilder sb = new StringBuilder();

		String line;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	/**
	 * @param in input stream
	 * @param out output stream
	 * @throws IOException if I/O error occurs
	 */
	public static void copyStream(InputStream in, OutputStream out)
			throws IOException {
		byte[] buf = new byte[4096];
		int len;
		while ((len = in.read(buf)) > -1) {
			out.write(buf, 0, len);
		}
	}

	/**
	 * Registers dialog for disposal on escape key-press.
	 * 
	 * @param dialog
	 *            JDialog to be closed on escape
	 */
	public static void registerForDisposeOnEscape(JDialog dialog) {
		JRootPane root = dialog.getRootPane();

		root.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
				.put(KeyStroke.getKeyStroke("ESCAPE"), "dispose-on-escape");
		root.getActionMap().put("dispose-on-escape",
				new DisposeDialogAction(dialog));
	}

	/**
	 * Removes all characters that are neither letters nor digits from the
	 * filename and changes the given file accordingly.
	 * @return processed name
	 */
	public static String keepOnlyLettersAndDigits(String name) {
		int length = name != null ? name.length() : 0;

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			char c = name.charAt(i);
			if (Character.isLetterOrDigit(c) || c == '.' || c == '_') { // underscore
				sb.append(c);
			} else {
				sb.append('_');
			}
		}

		if (sb.length() == 0) {
			sb.append("geogebra");
		}

		return sb.toString();
	}

	private static Comparator<File> comparator;

	/**
	 * Returns a comparator for GeoText objects. If equal, doesn't return zero
	 * (otherwise TreeSet deletes duplicates)
	 * @return comparator
	 */
	public static Comparator<File> getFileComparator() {
		if (comparator == null) {
			comparator = Comparator.comparing(File::getName);
		}
		return comparator;
	}

	/**
	 * 
	 * Writes file as UTF-8
	 * 
	 * @param s
	 *            string to write
	 * @param filename
	 *            filename
	 */
	public static void writeStringToFile(String s, String filename) {

		Writer out;
		try {

			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(filename), Charsets.getUtf8()));

			try {
				out.write(s);
			} finally {
				out.close();
			}

		} catch (Exception e) {
			Log.error("problem writing file " + filename);
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * Writes file as UTF-8
	 * 
	 * @param s
	 *            string to write
	 * @param file
	 *            filename
	 */
	public static void writeStringToFile(String s, File file) {

		Writer out;
		try {

			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file), Charsets.getUtf8()));

			try {
				out.write(s);
			} finally {
				out.close();
			}

		} catch (Exception e) {
			Log.error("problem writing file " + file.getName());
			e.printStackTrace();
		}

	}

	/**
	 * @param bytes
	 *            to write
	 * @param filename
	 *            filename
	 */
	public static void writeByteArrayToFile(byte[] bytes, String filename) {
		try (FileOutputStream out = new FileOutputStream(filename)) {
			out.write(bytes);
		} catch (Exception e) {
			Log.error("problem writing file " + filename);
			e.printStackTrace();
		}
	}

	private static String tempDir = null;

	/**
	 * @return temporary directory (ends with a separator)
	 */
	public static String getTempDir() {
		if (tempDir == null) {
			tempDir = System.getProperty("java.io.tmpdir");

			// Mac OS doesn't add "/" at the end of directory path name
			if (!tempDir.endsWith(File.separator)) {
				tempDir += File.separator;
			}
		}
		return tempDir;
	}

	/**
	 * Creates a directory
	 * 
	 * @param prefDir
	 *            directory
	 */
	public static void mkdirs(File prefDir) {
		if (!prefDir.exists() && !prefDir.mkdirs()) {
			Log.warn("Could not create " + prefDir.getAbsolutePath());
		}
	}

	/**
	 * Deletes a file
	 * 
	 * @param dest
	 *            file
	 */
	public static void delete(File dest) {
		if (!dest.delete()) {
			Log.warn("Could not delete " + dest.getAbsolutePath());
		}
	}

}
