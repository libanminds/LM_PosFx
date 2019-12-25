package com.libanminds.utils;

import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Locale;

public class HelperFunctions {

    public static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf('.');
        if (lastIndexOf == -1) {
            return "jpg";
        }
        return name.substring(lastIndexOf + 1);
    }

    public static DecimalFormat getDecimalFormatter() {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setCurrencySymbol("");
        formatter.setDecimalFormatSymbols(symbols);

        return formatter;
    }

    public static TextFormatter<String> getUnsignedIntegerFilter() {
        return new TextFormatter<>(change -> {
            String text = change.getText();
            if (text.matches("[0-9]*")) {
                return change;
            }
            return null;
        });
    }

    public static TextFormatter<String> getUnsignedNumberFilter() {
        return new TextFormatter<>(c -> {
            if (c.isContentChange()) {
                if (c.getControlNewText().length() == 0) {
                    return c;
                }
                try {
                    double val = Double.parseDouble(c.getControlNewText());
                    char lastChar = c.getControlNewText().charAt(c.getControlNewText().length() - 1);

                    // f and d won't through an exception if they are at the end of a number (f means float, d means double)
                    if (val < 0 || Character.toLowerCase(lastChar) == 'f' || Character.toLowerCase(lastChar) == 'd')
                        return null;
                    return c;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                return null;
            }
            return c;
        });
    }

    public static void highlightTextfieldError(TextField tf) {
        ObservableList<String> styleClass = tf.getStyleClass();

        if (!styleClass.contains(Constants.CSS_ERROR_HIGHLIGHT)) {
            styleClass.add(Constants.CSS_ERROR_HIGHLIGHT);
        }
    }

    public static void removeHighlightedTextfieldError(TextField tf) {
        ObservableList<String> styleClass = tf.getStyleClass();
        styleClass.removeAll(Collections.singleton(Constants.CSS_ERROR_HIGHLIGHT));
    }

    private HelperFunctions() {
        throw new AssertionError("You are not allowed to instantiate this class");
    }
}
