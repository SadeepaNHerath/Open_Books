package edu.book.socialnetwork.domain.storage.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String saveFile(MultipartFile file, Integer id);
}
