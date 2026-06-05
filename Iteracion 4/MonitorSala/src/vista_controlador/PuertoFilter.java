package vista_controlador;

import javax.swing.text.*;

public class PuertoFilter extends DocumentFilter {
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
            throws BadLocationException {
        if (isNumeric(fb.getDocument().getText(0, fb.getDocument().getLength()) + string)) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
            throws BadLocationException {
        if (isNumeric(fb.getDocument().getText(0, fb.getDocument().getLength()) + text)) {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    private boolean isNumeric(String str) {
        if (str.isEmpty()) return true;
        try {
            int val = Integer.parseInt(str);
            return val >= 1 && val <= 65535;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}