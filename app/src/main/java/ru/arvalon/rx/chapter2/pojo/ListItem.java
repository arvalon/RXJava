package ru.arvalon.rx.chapter2.pojo;

public class ListItem {
    private final String title;

    public ListItem(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title;
    }
}
