package org.khtml.hexagonal.domain.building;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.UUID;

@Component
public class BlobManager {

    @Value("${azure.storage.account-name}")
    private String accountName;

    @Value("${azure.storage.account-key}")
    private String accountKey;

    public String storeFile(String filename, InputStream content, long length) {
        String filenameUUID = generateUniqueFilename(filename);
        BlobClient client = containerClient().getBlobClient(filenameUUID);
        client.upload(content, length);
        return client.getBlobUrl();
    }

    private BlobContainerClient containerClient() {
        String connectionString = "DefaultEndpointsProtocol=https;"
                + "AccountName=" + accountName
                + ";AccountKey=" + accountKey
                + ";EndpointSuffix=core.windows.net";
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString).buildClient();
        return blobServiceClient.getBlobContainerClient("images");
    }

    private String generateUniqueFilename(String originalFilename) {
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');

        // 파일 확장자 추출
        if (dotIndex > 0) {
            extension = originalFilename.substring(dotIndex);
            originalFilename = originalFilename.substring(0, dotIndex);
        }

        // UUID 기반의 고유한 ID 생성 (8자리로 줄이기)
        String uniqueID = UUID.randomUUID().toString().substring(0, 8);

        // 원래 파일 이름에 고유 ID 추가
        return originalFilename + "_" + uniqueID + extension;
    }
}
