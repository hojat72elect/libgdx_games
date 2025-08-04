

package com.esotericsoftware.spine.attachments;

abstract public class Attachment {
    final String name;

    public Attachment(String name) {
        if (name == null) throw new IllegalArgumentException("name cannot be null.");
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }
}
