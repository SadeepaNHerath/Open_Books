package edu.book.socialnetwork.service;

import edu.book.socialnetwork.entity.BookEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService{
    String saveFile(MultipartFile file, Integer id);
}
