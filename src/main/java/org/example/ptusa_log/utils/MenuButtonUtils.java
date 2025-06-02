package org.example.ptusa_log.utils;

import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class MenuButtonUtils {

    public static void adjustMenuButtonWidthToLongestItem(MenuButton menuButton) {
        Font font = menuButton.getFont();
        if (font == null) {
            font = Font.getDefault();
        }

        double maxTextWidth = 0;
        Text tempText = new Text();
        tempText.setFont(font);

        for (MenuItem item : menuButton.getItems()) {
            String text = item.getText();
            if (text != null) {
                tempText.setText(text);
                double width = tempText.getLayoutBounds().getWidth();
                if (width > maxTextWidth) {
                    maxTextWidth = width;
                }
            }
        }

        double padding = 64;
        double finalWidth = maxTextWidth + padding;

        menuButton.setPrefWidth(finalWidth);
        menuButton.setMinWidth(Region.USE_PREF_SIZE);
        menuButton.setMaxWidth(Region.USE_PREF_SIZE);
    }
}

