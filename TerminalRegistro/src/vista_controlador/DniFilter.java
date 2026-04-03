package vista_controlador;

import javax.swing.text.DocumentFilter;
import javax.swing.text.*;

public class DniFilter extends DocumentFilter {

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
            throws BadLocationException {

        replace(fb, offset, 0, string, attr);
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
            throws BadLocationException {

        Document doc = fb.getDocument();
        String currentText = doc.getText(0, doc.getLength());
        String newText = currentText.substring(0, offset) + text + currentText.substring(offset + length);

        // Validaciones:
        if (newText.matches("\\d*") && newText.length() <= 8) {

            // Evitar números >= 100000000
            if (newText.isEmpty() || Long.parseLong(newText) < 100000000) {
                fb.replace(offset, length, text, attrs);
            }
        }
    }

}
