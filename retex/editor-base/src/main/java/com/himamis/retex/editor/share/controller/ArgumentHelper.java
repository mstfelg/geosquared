package com.himamis.retex.editor.share.controller;

import java.util.function.Predicate;

import com.himamis.retex.editor.share.meta.MetaModel;
import com.himamis.retex.editor.share.meta.Tag;
import com.himamis.retex.editor.share.model.MathArray;
import com.himamis.retex.editor.share.model.MathCharacter;
import com.himamis.retex.editor.share.model.MathContainer;
import com.himamis.retex.editor.share.model.MathFunction;
import com.himamis.retex.editor.share.model.MathSequence;

/**
 * Library class for processing function arguments
 */
public class ArgumentHelper {

	/**
	 * Moves content from current editor field into a function argument
	 * 
	 * @param editorState
	 *            editor state
	 * @param container
	 *            function
	 */
	public static void passArgument(EditorState editorState, MathContainer container) {
		MathSequence currentField = editorState.getCurrentField();
		int currentOffset = editorState.getCurrentOffset();
		// get pass to argument
		MathSequence field = (MathSequence) container
				.getArgument(container.getInsertIndex());
		while (currentOffset > 0
				&& currentField.getArgument(currentOffset - 1) instanceof MathCharacter
				&& " ".equals(currentField
						.getArgument(currentOffset - 1).toString())) {
			currentField.delArgument(currentOffset - 1);
			currentOffset--;
		}
		// pass scripts first
		while (currentOffset > 0 && currentField.isScript(currentOffset - 1)) {
			MathFunction script = (MathFunction) currentField
					.getArgument(currentOffset - 1);
			currentField.delArgument(currentOffset - 1);
			currentOffset--;
			field.addArgument(0, script);
		}
		editorState.setCurrentOffset(currentOffset);

		if (currentOffset > 0) {
			// if previous sequence argument are braces pass their content
			if (currentField
					.getArgument(currentOffset - 1) instanceof MathArray) {

				MathArray array = (MathArray) currentField
						.getArgument(currentOffset - 1);
				currentField.delArgument(currentOffset - 1);
				currentOffset--;
				if (field.size() == 0) {
					// here we already have sequence, just set it
					if (array.size() > 1 || array.getOpenKey() != '(') {
						MathSequence wrap = new MathSequence();
						wrap.addArgument(array);
						container.setArgument(container.getInsertIndex(), wrap);
					} else {
						container.setArgument(container.getInsertIndex(),
								array.getArgument(0));
					}
				} else {
					field.addArgument(0, array);
				}

				// if previous sequence argument is, function pass it
			} else if (currentField
					.getArgument(currentOffset - 1) instanceof MathFunction) {

				MathFunction function = (MathFunction) currentField
						.getArgument(currentOffset - 1);
				currentField.delArgument(currentOffset - 1);
				currentOffset--;
				field.addArgument(0, function);

				// otherwise pass character sequence
			} else {

				passCharacters(editorState, container);
				currentOffset = editorState.getCurrentOffset();
			}
		}
		editorState.setCurrentOffset(currentOffset);
	}

	private static void passCharacters(EditorState editorState, MathContainer container) {
		int currentOffset = editorState.getCurrentOffset();
		MathSequence currentField = editorState.getCurrentField();
		// get pass to argument
		MathSequence field = (MathSequence) container
				.getArgument(container.getInsertIndex());

		int offset = passCharacters(currentField, currentOffset, field, MathCharacter::isWordBreak);
		editorState.setCurrentOffset(offset);
	}

	private static int passCharacters(MathSequence currentField, int initialOffset,
			MathSequence field, Predicate<MathCharacter> condition) {
		int currentOffset = initialOffset;
		while (currentOffset > 0 && currentField
				.getArgument(currentOffset - 1) instanceof MathCharacter) {

			MathCharacter character = (MathCharacter) currentField
					.getArgument(currentOffset - 1);
			if (condition.test(character)) {
				break;
			}
			currentField.delArgument(currentOffset - 1);
			currentOffset--;
			field.addArgument(0, character);
		}
		return currentOffset;
	}

	/**
	 * Reads all characters to the right of the cursor until it encounters a
	 * symbol
	 * 
	 * @param editorState
	 *            current editor state
	 * @return last string of characters
	 */
	public static String readCharacters(EditorState editorState,
			int initialOffset) {
		StringBuilder stringBuilder = new StringBuilder();
		int offset = initialOffset;
		MathSequence currentField = editorState.getCurrentField();
		while (offset > 0 && currentField
				.getArgument(offset - 1) instanceof MathCharacter) {

			MathCharacter character = (MathCharacter) currentField
					.getArgument(offset - 1);
			if (character.isWordBreak()) {
				break;
			}
			offset--;
			stringBuilder.insert(0, character.getUnicodeString());
		}
		return stringBuilder.toString();
	}

	/**
	 * Removes the whole number part from parent container and attaches it to the mixed number
	 * @param parent parent container
	 * @param fraction fractional part of the mixed number
	 * @param model model
	 */
	public static void addFraction(MathContainer parent, MathFunction fraction, MetaModel model) {
		if (parent instanceof MathSequence && parent.size() > 0
				&& "\u2064".equals(parent.getArgument(parent.size() - 1).toString())) {
			MathFunction mixed = new MathFunction(model.getGeneral(Tag.MIXED_NUMBER));
			MathSequence whole = new MathSequence();
			parent.delArgument(parent.size() - 1);
			passCharacters((MathSequence) parent, parent.size() - 1, whole,
					c -> !java.lang.Character.isDigit(c.getUnicode()));
			mixed.setArgument(0, whole);
			mixed.setArgument(1, fraction.getArgument(0));
			mixed.setArgument(2, fraction.getArgument(1));
			parent.addArgument(mixed);
		} else {
			parent.addArgument(fraction);
		}
	}
}
