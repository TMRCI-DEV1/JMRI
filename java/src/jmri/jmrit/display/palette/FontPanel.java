package jmri.jmrit.display.palette;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jmri.jmrit.display.PositionablePopupUtil;

public class FontPanel extends JPanel implements ItemListener {

    static final String[] JUSTIFICATION = {Bundle.getMessage("left"),
            Bundle.getMessage("center"),
            Bundle.getMessage("right")};

    static final String[] STYLES = {Bundle.getMessage("Plain"),
        Bundle.getMessage("Bold"),
        Bundle.getMessage("Italic"),
        Bundle.getMessage("Bold/italic")};

    static final String[] FONTSIZE = {"6", "8", "10", "11", "12", "14", "16",
            "20", "24", "28", "32", "36"};

    public static final int SIZE = 1;
    public static final int STYLE = 2;
    public static final int JUST = 3;
    public static final int FACE = 4;

    private AJComboBox<Font> _fontFaceBox;
    private AJComboBox<String> _fontSizeBox;
    private AJComboBox<String> _fontStyleBox;
    private AJComboBox<String> _fontJustBox;

    PositionablePopupUtil _util;
    ActionListener _callBack;

    static class AJComboBox<T> extends JComboBox<T> {
        int _which;

        AJComboBox(T[] items, int which) {
            super(items);
            _which = which;
        }
    }

    public FontPanel(PositionablePopupUtil util, ActionListener ca) {
        _util = util;
        _callBack = ca;
        makeFontPanels();
    }

    private JPanel makeBoxPanel(String caption, JComboBox<?> box) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel(Bundle.getMessage(caption)));
        box.addItemListener(this);
        panel.add(box);
        return panel;
    }

    private void makeFontPanels() {

        JPanel fontPanel = new JPanel();

        Font defaultFont = _util.getFont();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontFamilyNames = ge.getAvailableFontFamilyNames();
        Font[] fonts = new Font[fontFamilyNames.length];
        int k = 0;
        for (String fontFamilyName : fontFamilyNames) {
            fonts[k++] = new Font(fontFamilyName, defaultFont.getStyle(), defaultFont.getSize()) {
                @Override
                public String toString() {
                    return getFamily();
                }
            };
        }
        _fontFaceBox = new AJComboBox<>(fonts, FACE);
        fontPanel.add(makeBoxPanel("EditFont", _fontFaceBox)); // NOI18N

        _fontSizeBox = new AJComboBox<>(FONTSIZE, SIZE);
        fontPanel.add(makeBoxPanel("FontSize", _fontSizeBox)); // NOI18N

        _fontStyleBox = new AJComboBox<>(STYLES, STYLE);
        fontPanel.add(makeBoxPanel("FontStyle", _fontStyleBox)); // NOI18N

        _fontJustBox = new AJComboBox<>(JUSTIFICATION, JUST);
        fontPanel.add(makeBoxPanel("Justification", _fontJustBox)); // NOI18N
        this.add(fontPanel);
    }

    public void setFontSelections() {
        _fontFaceBox.setSelectedItem(_util.getFont());
        int row = 4;
        for (int i = 0; i < FONTSIZE.length; i++) {
            if (_util.getFontSize() == Integer.parseInt(FONTSIZE[i])) {
                row = i;
                break;
            }
        }
        _fontSizeBox.setSelectedIndex(row);
        _fontStyleBox.setSelectedIndex(_util.getFont().getStyle());
        switch (_util.getJustification()) {
            case PositionablePopupUtil.LEFT:
                row = 0;
                break;
            case PositionablePopupUtil.CENTRE:
                row = 1;
                break;
            case PositionablePopupUtil.RIGHT:
            default:
                row = 2;
        }
        _fontJustBox.setSelectedIndex(row);
        _callBack.actionPerformed(null);
    }

    @SuppressFBWarnings(value = "Raw use of parameterized class", justification="AJComboBox is checked")
    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof AJComboBox<?>) {
            AJComboBox<?> comboBox = (AJComboBox<?>) e.getSource();
            switch (comboBox._which) {
                case SIZE:
                    String size = (comboBox.getSelectedItem() != null ? (String) comboBox.getSelectedItem() : "10");
                    _util.setFontSize(Float.parseFloat(size));
                    break;
                case STYLE:
                    int style = 0;
                    switch (comboBox.getSelectedIndex()) {
                        case 0:
                            style = Font.PLAIN;
                            break;
                        case 1:
                            style = Font.BOLD;
                            break;
                        case 2:
                            style = Font.ITALIC;
                            break;
                        case 3:
                            style = (Font.BOLD | Font.ITALIC);
                            break;
                        default:
                            log.warn("Unexpected index {}  in itemStateChanged", comboBox.getSelectedIndex());
                            break;
                    }
                    _util.setFontStyle(style);
                    break;
                case JUST:
                    int just = 0;
                    switch (comboBox.getSelectedIndex()) {
                        case 0:
                            just = PositionablePopupUtil.LEFT;
                            break;
                        case 1:
                            just = PositionablePopupUtil.CENTRE;
                            break;
                        case 2:
                            just = PositionablePopupUtil.RIGHT;
                            break;
                        default:
                            log.warn("Unexpected index {}  in itemStateChanged", comboBox.getSelectedIndex());
                            break;
                    }
                    _util.setJustification(just);
                    break;
                case FACE:
                    Font font = (comboBox.getSelectedItem() != null ? (Font) comboBox.getSelectedItem() : (Font) comboBox.getItemAt(0));
                    _util.setFont(font);
                    break;
                default:
                    log.warn("Unexpected _which {}  in itemStateChanged", comboBox._which);
                    break;
            }
            _callBack.actionPerformed(null);
        }
    }

    private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FontPanel.class);
}
