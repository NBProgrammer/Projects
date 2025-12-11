package com.nbprogrammer.ytflac.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class YouTubeDownloadService {

    private static final Logger logger = LoggerFactory.getLogger(YouTubeDownloadService.class);
    private static final String DOWNLOAD_DIR = "downloads";

    public YouTubeDownloadService() {
        // Create downloads directory if it doesn't exist
        try {
            Files.createDirectories(Paths.get(DOWNLOAD_DIR));
        } catch (IOException e) {
            logger.error("Failed to create downloads directory", e);
        }
    }

    /**
     * Download audio from YouTube URL and convert to FLAC format
     * 
     * @param youtubeUrl The YouTube URL to download from
     * @return The path to the downloaded FLAC file
     * @throws IOException if download fails
     */
    public File downloadAsFlac(String youtubeUrl) throws IOException {
        validateYouTubeUrl(youtubeUrl);

        String uniqueId = UUID.randomUUID().toString();
        String outputTemplate = DOWNLOAD_DIR + "/" + uniqueId + ".%(ext)s";

        // Build yt-dlp command
        List<String> command = new ArrayList<>();
        command.add("yt-dlp");
        command.add("--extract-audio");
        command.add("--audio-format");
        command.add("flac");
        command.add("--audio-quality");
        command.add("0"); // Best quality
        command.add("--output");
        command.add(outputTemplate);
        command.add(youtubeUrl);

        logger.info("Executing command: {}", String.join(" ", command));

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();

        // Read output
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
                logger.info("yt-dlp: {}", line);
            }
        }

        int exitCode;
        try {
            exitCode = process.waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Download process was interrupted", e);
        }

        if (exitCode != 0) {
            logger.error("yt-dlp failed with exit code {}: {}", exitCode, output);
            throw new IOException("Failed to download audio: " + output);
        }

        // Find the downloaded file
        File downloadedFile = new File(DOWNLOAD_DIR, uniqueId + ".flac");
        if (!downloadedFile.exists()) {
            throw new IOException("Downloaded file not found: " + downloadedFile.getAbsolutePath());
        }

        logger.info("Successfully downloaded: {}", downloadedFile.getAbsolutePath());
        return downloadedFile;
    }

    /**
     * Get video title from YouTube URL
     * 
     * @param youtubeUrl The YouTube URL
     * @return The video title
     * @throws IOException if retrieval fails
     */
    public String getVideoTitle(String youtubeUrl) throws IOException {
        validateYouTubeUrl(youtubeUrl);

        List<String> command = new ArrayList<>();
        command.add("yt-dlp");
        command.add("--get-title");
        command.add(youtubeUrl);

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();

        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }
        }

        try {
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("Failed to get video title");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Process was interrupted", e);
        }

        return output.toString().trim();
    }

    private void validateYouTubeUrl(String url) throws IOException {
        if (url == null || url.trim().isEmpty()) {
            throw new IOException("YouTube URL cannot be empty");
        }

        // Basic YouTube URL validation
        if (!url.contains("youtube.com/watch") && 
            !url.contains("youtu.be/") && 
            !url.contains("youtube.com/shorts/")) {
            throw new IOException("Invalid YouTube URL");
        }
    }

    /**
     * Delete a file from the downloads directory
     * 
     * @param file The file to delete
     */
    public void deleteFile(File file) {
        if (file != null && file.exists()) {
            try {
                Files.delete(file.toPath());
                logger.info("Deleted file: {}", file.getAbsolutePath());
            } catch (IOException e) {
                logger.error("Failed to delete file: {}", file.getAbsolutePath(), e);
            }
        }
    }
}
