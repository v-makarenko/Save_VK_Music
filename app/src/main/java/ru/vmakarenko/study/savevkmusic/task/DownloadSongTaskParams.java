package ru.vmakarenko.study.savevkmusic.task;

import ru.vmakarenko.study.savevkmusic.model.AudioItem;

/**
 * Created by VMakarenko on 05.04.2016.
 */
public class DownloadSongTaskParams {
    private String path, name, link;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public static DownloadSongTaskParams from(AudioItem item) {
        DownloadSongTaskParams res = new DownloadSongTaskParams();
        res.setLink(item.getUrl());
        return res;
    }
}
