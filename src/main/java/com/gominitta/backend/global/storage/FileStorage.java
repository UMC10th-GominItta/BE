package com.gominitta.backend.global.storage;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorage {

	String store(MultipartFile file, String subDir);

	String resolveUrl(String mediaKey);
}
