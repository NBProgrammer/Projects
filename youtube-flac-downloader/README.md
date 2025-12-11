# YouTube FLAC Downloader

A web application built with Java Spring Boot that allows users to download high-quality audio from YouTube videos in FLAC format.

## Features

- 🎵 Download audio from YouTube videos in FLAC format
- 🎨 Clean and user-friendly web interface
- 📊 Preview video information before downloading
- ⚡ Fast and efficient processing
- 🔒 Secure and lightweight

## Prerequisites

Before running this application, ensure you have the following installed:

1. **Java 17 or higher**
   ```bash
   java -version
   ```

2. **Maven 3.6 or higher**
   ```bash
   mvn -version
   ```

3. **yt-dlp** (YouTube downloader)
   ```bash
   # On Ubuntu/Debian
   sudo apt update
   sudo apt install yt-dlp

   # On macOS
   brew install yt-dlp

   # On Windows
   # Download from: https://github.com/yt-dlp/yt-dlp/releases
   ```

4. **FFmpeg** (for audio conversion)
   ```bash
   # On Ubuntu/Debian
   sudo apt install ffmpeg

   # On macOS
   brew install ffmpeg

   # On Windows
   # Download from: https://ffmpeg.org/download.html
   ```

## Installation

1. Navigate to the project directory:
   ```bash
   cd youtube-flac-downloader
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

## Running the Application

1. Start the application:
   ```bash
   mvn spring-boot:run
   ```

   Or run the JAR file directly:
   ```bash
   java -jar target/youtube-flac-downloader-1.0.0.jar
   ```

2. Open your web browser and navigate to:
   ```
   http://localhost:8080
   ```

## Usage

1. Copy a YouTube video URL (e.g., `https://www.youtube.com/watch?v=dQw4w9WgXcQ`)
2. Paste the URL into the input field
3. (Optional) Click "Get Info" to preview the video title
4. Click "Download FLAC" to download the audio in FLAC format
5. The file will be automatically downloaded to your default downloads folder

## API Endpoints

### GET /api/health
Health check endpoint to verify the service is running.

**Response:**
```json
{
  "status": "ok",
  "service": "YouTube FLAC Downloader"
}
```

### GET /api/video-info?url={youtube_url}
Get information about a YouTube video.

**Parameters:**
- `url`: The YouTube video URL

**Response:**
```json
{
  "status": "success",
  "title": "Video Title Here"
}
```

### POST /api/download
Download audio from YouTube as FLAC.

**Request Body:**
```json
{
  "url": "https://www.youtube.com/watch?v=..."
}
```

**Response:**
Binary FLAC file download

## Project Structure

```
youtube-flac-downloader/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/nbprogrammer/ytflac/
│       │       ├── YouTubeFlacDownloaderApplication.java
│       │       ├── controller/
│       │       │   ├── DownloadController.java
│       │       │   └── WebController.java
│       │       └── service/
│       │           └── YouTubeDownloadService.java
│       └── resources/
│           ├── application.properties
│           └── templates/
│               └── index.html
├── pom.xml
└── README.md
```

## Technologies Used

- **Java 17**: Programming language
- **Spring Boot 3.1.5**: Web framework
- **Thymeleaf**: Template engine for the web interface
- **Maven**: Build and dependency management
- **yt-dlp**: YouTube video/audio downloader
- **FFmpeg**: Audio/video processing

## Security & Legal Considerations

⚠️ **Important Notice:**
- This tool is for personal use only
- Ensure you have the right to download content
- Respect copyright laws and YouTube's Terms of Service
- Do not use this tool for commercial purposes or to distribute copyrighted content

## Troubleshooting

### Issue: "yt-dlp: command not found"
**Solution:** Install yt-dlp using the instructions in the Prerequisites section.

### Issue: "Failed to download audio"
**Solution:** 
- Verify the YouTube URL is correct
- Check your internet connection
- Ensure yt-dlp and FFmpeg are installed
- Try updating yt-dlp: `yt-dlp -U`

### Issue: Application won't start
**Solution:**
- Verify Java 17+ is installed
- Check if port 8080 is already in use
- Review the logs for specific error messages

## Contributing

This is a personal project, but suggestions and improvements are welcome!

## License

This project is for educational and personal use only.

## Author

NBProgrammer
