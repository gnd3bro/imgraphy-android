package com.teamig.imgraphy.tool;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class TagParser {

    public static List<String> parse(String tags) {
        List<String> parsed = new ArrayList<String>();
        StringTokenizer stringTokenizer = new StringTokenizer(tags, ";");

        while (stringTokenizer.hasMoreTokens()) {
            parsed.add(stringTokenizer.nextToken());
        }

        return parsed;
    }
}
