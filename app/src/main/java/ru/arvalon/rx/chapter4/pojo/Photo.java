package ru.arvalon.rx.chapter4.pojo;

import java.util.List;

import ru.arvalon.rx.chapter4.network.FlickrPhotoInfoResponse;
import ru.arvalon.rx.chapter4.network.FlickrPhotosGetSizesResponse;

public class Photo {
    final private String id;
    final private String title;
    final private String username;
    final private String thumbnailUrl;

    private Photo(String id, String title, String username, String thumbnailUrl) {
        this.id = id;
        this.title = title;
        this.username = username;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUsername() {
        return username;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public static Photo createPhoto(FlickrPhotoInfoResponse.PhotoInfo photoInfo,
                                    List<FlickrPhotosGetSizesResponse.Size> sizes) {
        String thumbnailUrl = null;
        for (FlickrPhotosGetSizesResponse.Size size : sizes) {
            if (size.getLabel().equals("Square")) {
                thumbnailUrl = size.getSource();
                break;
            }
        }
        return new Photo(photoInfo.getId(), photoInfo.getTitle(), photoInfo.getUsername(), thumbnailUrl);
    }
}
