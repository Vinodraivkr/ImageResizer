# AWS Lambda Image Resizer

This project implements an AWS Lambda function that resizes images and uploads the resized version to an S3 bucket. The function is written in Java and leverages the `net.coobird.thumbnailator` library for efficient image processing.

## Overview

The **Image Resizer Lambda Function** accepts a base64-encoded image through an API Gateway POST request, resizes it to the specified dimensions, and uploads the resized image to a specified S3 bucket in JPEG format.

### Key Features:
- **Input**: Base64-encoded image, bucket name, file name, and dimensions (width and height) via API Gateway POST request.
- **Processing**: The image is resized using the `Thumbnailator` library.
- **Output**: Resized image is stored as a `.jpg` file in the specified S3 bucket.
- **Error Handling**: Returns detailed error messages if input is invalid or if there are any issues with S3 upload.

## Technology Stack

- **AWS Lambda**: Serverless compute service to run the image processing logic.
- **AWS S3**: Object storage to store the resized images.
- **API Gateway**: To trigger the Lambda function via an HTTP POST request.
- **Thumbnailator**: Java library used to resize the images.
- **Java**: Lambda function written in Java (Java 8 or higher).
- **Maven**: Dependency management and project build tool.

## Setup and Configuration

### Prerequisites

- AWS account with S3 and Lambda permissions.
- Java Development Kit (JDK 8 or higher).
- AWS CLI configured with appropriate IAM roles and permissions.
- Maven (for dependency management).

### Step-by-Step Guide

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-username/image-resizer-lambda.git

2. **Configure Your S3 Bucket**:
   Make sure to create an S3 bucket in the AWS region where your Lambda function is deployed.

3. **Build the Project**:
   Use Maven to package the Lambda function into a deployable JAR file:
   ```bash
   mvn clean package

3. **Deploy the Lambda Function**:
   - Use the AWS Management Console or the AWS CLI to create a new Lambda function.
   - Upload the JAR file generated by Maven.
   - Set the necessary environment variables and permissions for the Lambda function to interact with S3.

4. **API Gateway Setup**:
   - Create a POST method in API Gateway to trigger the Lambda function.
   - Ensure the API Gateway is configured to pass JSON data (image, bucket name, file name, width, and height).

## Request and Response Format

### POST Request

The API Gateway POST request should include a JSON body with the following structure:

```json
{
    "imageData": "BASE64_ENCODED_IMAGE_STRING",
    "bucketName": "your-s3-bucket",
    "fileName": "desired-output-filename",
    "width": 200,
    "height": 200
}
```
  - **imageData**: Base64-encoded string of the image.
  - **bucketName**: S3 bucket name where the resized image will be stored.
  - **fileName**: The name of the resized image file (without extension).
  - **width**: Desired width of the resized image.
  - **height**: Desired height of the resized image.

### Example Response

- **Success** (HTTP 200):
  ```json
  {
      "message": "Image resized and uploaded successfully."
  }
- **Failure** (HTTP 400 or 500):
  ```json
  {
    "message": "Error: <detailed error message>"
  }

## Dependencies

The key dependencies used in this project are:

```xml
<dependencies>
    <dependency>
        <groupId>com.amazonaws</groupId>
        <artifactId>aws-lambda-java-core</artifactId>
        <version>1.2.1</version>
    </dependency>
    <dependency>
        <groupId>software.amazon.awssdk</groupId>
        <artifactId>s3</artifactId>
        <version>2.20.86</version>
    </dependency>
    <dependency>
        <groupId>net.coobird</groupId>
        <artifactId>thumbnailator</artifactId>
        <version>0.4.14</version>
    </dependency>
</dependencies>
```
## How It Works

1. The Lambda function receives the base64-encoded image and specified dimensions via an API Gateway POST request.
2. It decodes the image and uses the `Thumbnailator` library to resize it to the requested width and height.
3. The resized image is uploaded to the specified S3 bucket as a `.jpg` file.
4. If successful, the function returns a 200 response. If there are any errors (e.g., missing parameters or S3 issues), appropriate error messages are returned.

## Error Handling

The Lambda function handles the following errors:

- **Missing or malformed input parameters**: The function checks for missing image data, bucket name, file name, width, and height. It responds with a 400 status code if any required parameters are missing.
- **S3 upload issues**: Any errors encountered during the S3 upload (e.g., bucket not found or permission issues) will result in a 500 status code with an appropriate error message.
