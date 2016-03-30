package ru.vmakarenko.study.savevkmusic.list;

/**
 * Created by VMakarenko on 30.03.2016.
 */
public class AudioItem {
    private String author, title;
    private boolean saved;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }
}
