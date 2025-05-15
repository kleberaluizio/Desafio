package com.luizalabs.api.txt.purchase.factories;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

public class FileFactory {

    private static final String VALID_CONTENT_TYPE = "text/plain";

    public static MultipartFile validFile() {
        return create(VALID_CONTENT_TYPE, false);
    }

    public static MultipartFile emptyFile() {
        return create(VALID_CONTENT_TYPE, true);
    }

    public static MultipartFile InvalidContentTypeFile() {
        return create("INVALID_CONTENT_TYPE", false);
    }

    private static MultipartFile create(String contentType, boolean isEmpty) {
        byte[] content = isEmpty? new byte[0] : "content".getBytes(StandardCharsets.UTF_8);
        return new MockMultipartFile(
                "FILE",
                "ORIGINAL_FILE_NAME",
                contentType,
                content);

    }
}
