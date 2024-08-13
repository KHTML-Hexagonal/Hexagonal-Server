package org.khtml.hexagonal.domain.building;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class BlobManager {

    @Value("${azure.storage.account-name}")
    private String accountName;

    @Value("${azure.storage.account-key}")
    private String accountKey;

    public String storeFile(String filename, InputStream content, long length) {
        BlobClient client = containerClient().getBlobClient(filename.trim());
        client.upload(content, length);
        return "File uploaded with success!";
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

}
