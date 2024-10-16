package vkr.aws.image.model;

public class ImageRequest {
    private String imageData;
    private String bucketName;
    private String fileName;
    private int width;
    private int height;

    public ImageRequest() {
        // Default constructor
    }

    public ImageRequest(String imageData, String bucketName, String fileName, int width, int height) {
        this.imageData = imageData;
        this.bucketName = bucketName;
        this.fileName = fileName;
        this.width = width;
        this.height = height;
    }

    // Getters and Setters
    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}

