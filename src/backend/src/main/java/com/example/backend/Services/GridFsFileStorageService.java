package com.example.backend.Services;

import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.bson.Document;

import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
public class GridFsFileStorageService {

    private final GridFsTemplate gridFsTemplate;

    @Autowired
    public GridFsFileStorageService(GridFsTemplate gridFsTemplate) {
        this.gridFsTemplate = gridFsTemplate;
    }


    public GridFsResource getFile(String id) {
        GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
        return gridFsTemplate.getResource(gridFSFile);
    }

    public String store(MultipartFile file) {
        ObjectId fileId;
        try {
            fileId = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType());
        } catch (IOException e) {
            throw new RuntimeException("Error storing file", e);
        }
        return "/files/" + fileId.toString();
    }

    // New overloaded method to store files from byte arrays
    public String store(byte[] content, String filename, String contentType) {
        // Convert byte array to ByteArrayInputStream
        ByteArrayInputStream stream = new ByteArrayInputStream(content);

        // Define additional metadata as needed
        GridFSUploadOptions options = new GridFSUploadOptions().metadata(
                new Document("contentType", contentType)
        );

        // Store the file in GridFS
        Object fileId = gridFsTemplate.store(stream, filename, contentType, options);

        // Return the fileId as a string or the URL to access the file, depending on your application's requirements
        return fileId.toString();
    }
}
