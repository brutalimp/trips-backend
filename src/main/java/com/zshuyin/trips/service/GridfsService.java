package com.zshuyin.trips.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.mongodb.async.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.gridfs.GridFSDBFile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class GridfsService {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private MongoDbFactory mongoDbFactory;

    public void save(MultipartFile file, String fileID) {

        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            String name = file.getOriginalFilename();
            gridFsTemplate.store(inputStream, fileID);
        } catch (Exception ex) {
            throw new RuntimeException("System Exception while handling request");
        }
    }

    public GridFSFile get(String fileId) {
        // try {
        //     GridFSFile fsFile =  gridFsTemplate.findOne(Query.query(Criteria.where("filename").is(fileId)));
        //     GridFsResource resource =  gridFsTemplate.getResource(fsFile);
        //     File file = resource.getFile();
        //     OutputStream stream = new FileOutputStream(file);
        //     return stream;
        // } catch (Exception ex) {
        //     throw new RuntimeException("System Exception while handling request");
        // }
        return gridFsTemplate.findOne(Query.query(Criteria.where("filename").is(fileId)));
    }
}