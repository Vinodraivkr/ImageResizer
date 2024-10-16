package vkr.aws.image.processor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.coobird.thumbnailator.Thumbnails;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

public class ImageResizerHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final S3Client s3Client;
    public ImageResizerHandler() {
        this.s3Client = S3Client.builder().build();
    }

    // Constructor for dependency injection (used for testing)
    public ImageResizerHandler(S3Client s3Client) {
        this.s3Client = s3Client;
    }
    
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        try {
            // Parse the request
            Map<String, String> requestBody = objectMapper.readValue(request.getBody(), Map.class);
            String imageData = requestBody.get("imageData");
            String bucketName = requestBody.get("bucketName");
            String fileName = requestBody.get("fileName");

            if (imageData == null || bucketName == null || fileName == null) {
                response.setStatusCode(400);
                response.setBody("Missing required parameters.");
                return response;
            }

            // Decode image data
            byte[] imageBytes = Base64.getDecoder().decode(imageData);

            // Resize the image
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Thumbnails.of(new ByteArrayInputStream(imageBytes))
                      .size(200, 200)
                      .outputFormat("jpg")
                      .toOutputStream(outputStream);

            // Upload resized image to S3
            byte[] resizedImageBytes = outputStream.toByteArray();
            try {
                s3Client.putObject(
                    PutObjectRequest.builder()
                                    .bucket(bucketName)
                                    .key(fileName+".jpg")
                                    .contentType("image/jpeg")
                                    .build(),
                    software.amazon.awssdk.core.sync.RequestBody.fromBytes(resizedImageBytes)
                );
                response.setStatusCode(200);
                response.setBody("Image resized and uploaded successfully.");
            } catch (S3Exception e) {
                response.setStatusCode(500);
                response.setBody("Failed to upload to S3: " + e.awsErrorDetails().errorMessage());
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setBody("Error: " + e.getMessage());
        }

        return response;
    }
}
