package com.ecom.webshop.store.services.impl;

import com.ecom.webshop.store.exceptions.BadApiRequestException;
import com.ecom.webshop.store.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
@Service
public class FileServiceImpl implements FileService {
    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
    @Override
    public String uploadImage(MultipartFile file, String path) throws IOException {

        String originalFilename=file.getOriginalFilename(); //we will append the filename with uuid
        logger.info("Filename :{}",originalFilename);
        String filename= UUID.randomUUID().toString();
        String extension=originalFilename.substring(originalFilename.lastIndexOf("."));
        String finalExtendedfilename=filename+extension;
        String completePathwithName=path +finalExtendedfilename;
        logger.info("Full image path:{}",completePathwithName);

        if(extension.equalsIgnoreCase((".jpg"))|| extension.equalsIgnoreCase(".jpeg") || extension.equalsIgnoreCase(".png")){
            logger.info("file extension is {}",extension);
            File folder=new File(path);
            if(!folder.exists()){
                folder.mkdirs();
            }

            //upload
             Files.copy(file.getInputStream(), Paths.get(completePathwithName));
            return finalExtendedfilename;

        }else{
            throw new BadApiRequestException("File with this extension"+extension +" is not allowed !!");

        }

    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {
        String completePath=path+File.separator+name;
        InputStream inputStream=new FileInputStream(completePath);
        return inputStream;

    }
}
