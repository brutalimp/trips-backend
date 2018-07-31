package com.zshuyin.trips.bean;

import java.util.List;

import com.zshuyin.trips.utils.Position;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.*;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document

public class Trip {
    @Id
    private String id;

    private String userId;

    private String description;

    private List<String> images;

    private String timestamp;

    private Position position;

    private int likeCount;

    private List<String> likeIds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public void addLikeCount(String userId) {
        this.likeCount ++;
        this.addLikeId(userId);
    }

    public void removeLikeCount(String userId) {
        this.likeCount --;
    }

    public List<String> getLikeIds() {
        return this.likeIds;
    }

    public boolean addLikeId(String userId) {
        for(String likeId: this.likeIds) {
            if(likeId.equals(userId)) {
                return false;
            }
        }
        this.likeIds.add(userId);
        this.likeCount ++;
        return true;
    }

    public boolean removeLikeId(String userId) {
        for(String likeId: this.likeIds) {
            if(likeId.equals(userId)) {
               this.likeIds.remove(likeId);
               this.likeCount --;
               return true;
            }
        }
        return false;
    }

}