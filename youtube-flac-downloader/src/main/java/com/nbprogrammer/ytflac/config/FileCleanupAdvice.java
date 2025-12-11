package com.nbprogrammer.ytflac.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ControllerAdvice
public class FileCleanupAdvice implements ResponseBodyAdvice<Resource> {

    private static final Logger logger = LoggerFactory.getLogger(FileCleanupAdvice.class);
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return Resource.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public Resource beforeBodyWrite(Resource body, MethodParameter returnType,
                                    MediaType selectedContentType,
                                    Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                    ServerHttpRequest request, ServerHttpResponse response) {
        
        if (body instanceof FileSystemResource) {
            FileSystemResource fileResource = (FileSystemResource) body;
            File file = fileResource.getFile();
            
            // Schedule file deletion after a short delay to ensure it's been sent
            scheduleFileDeletion(file);
        }
        
        return body;
    }

    private void scheduleFileDeletion(File file) {
        // Use scheduled executor service instead of creating new threads
        scheduler.schedule(() -> {
            if (file.exists() && file.delete()) {
                logger.info("Cleaned up temporary file: {}", file.getAbsolutePath());
            } else {
                logger.warn("Failed to delete temporary file: {}", file.getAbsolutePath());
            }
        }, 5, TimeUnit.SECONDS);
    }
}
