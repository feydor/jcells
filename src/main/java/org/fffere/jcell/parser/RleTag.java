package org.fffere.jcell.parser;

public enum RleTag {
    DEAD('b'),
    ALIVE('o'),
    EOL('$');

    final char ch;

    RleTag(char o) {
        ch = o;
    }
}
