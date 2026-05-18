package vista_controlador;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class IPFilter extends DocumentFilter {
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
            throws BadLocationException {
        if (isValidIP(fb.getDocument().getText(0, fb.getDocument().getLength()) + string)) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
            throws BadLocationException {
        if (isValidIP(fb.getDocument().getText(0, fb.getDocument().getLength() - length) + text)) {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    private boolean isValidIP(String ip) {
        if (ip.isEmpty()) return true;
        if (ip.chars().filter(ch -> ch == '.').count() > 3) return false;

        String[] partes = ip.split("\\.");
        for (String parte : partes) {
            if (parte.isEmpty()) continue;
            try {
                int val = Integer.parseInt(parte);
                if (val < 0 || val > 255) return false;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return ip.matches("[0-9.]*");
    }
}