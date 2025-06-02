package org.example.ptusa_log.utils;

import java.util.Objects;

public class SVGProcessor {
    public static String getSvgUrl(String fileName) {
        return Objects.requireNonNull(SVGProcessor.class.getResource(StringConstants.ICONS_PATH + fileName)).toExternalForm();
    }
}
