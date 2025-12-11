# Quick Start Guide

## Getting Started with YouTube FLAC Downloader

This guide will help you get the YouTube FLAC Downloader up and running quickly.

## Prerequisites Check

Before you begin, verify you have the required software installed:

```bash
# Check Java version (should be 17 or higher)
java -version

# Check Maven version
mvn -version

# Check yt-dlp installation
yt-dlp --version

# Check FFmpeg installation
ffmpeg -version
```

If any of these are missing, follow the installation instructions in the main [README.md](README.md).

## Quick Start

### 1. Build the Application

```bash
cd youtube-flac-downloader
mvn clean package
```

### 2. Start the Application

```bash
java -jar target/youtube-flac-downloader-1.0.0.jar
```

You should see output indicating the server has started:
```
Started YouTubeFlacDownloaderApplication in X.XXX seconds
Tomcat started on port(s): 8080 (http)
```

### 3. Access the Web Interface

Open your web browser and navigate to:
```
http://localhost:8080
```

### 4. Download Your First FLAC

1. Find a YouTube video you want to download
2. Copy the URL (e.g., `https://www.youtube.com/watch?v=dQw4w9WgXcQ`)
3. Paste it into the input field on the web page
4. Click "Download FLAC"
5. Wait for the file to download

## API Usage Examples

### Get Video Information

```bash
curl "http://localhost:8080/api/video-info?url=https://www.youtube.com/watch?v=dQw4w9WgXcQ"
```

Response:
```json
{
  "status": "success",
  "title": "Rick Astley - Never Gonna Give You Up"
}
```

### Download FLAC via API

```bash
curl -X POST http://localhost:8080/api/download \
  -H "Content-Type: application/json" \
  -d '{"url":"https://www.youtube.com/watch?v=dQw4w9WgXcQ"}' \
  --output audio.flac
```

### Health Check

```bash
curl http://localhost:8080/api/health
```

Response:
```json
{
  "status": "ok",
  "service": "YouTube FLAC Downloader"
}
```

## Common Issues

### Port 8080 Already in Use

If port 8080 is already in use, you can change it by:

1. Create or edit `src/main/resources/application.properties`
2. Add or modify: `server.port=8081`
3. Rebuild and restart the application

### yt-dlp Not Found

If you get an error about yt-dlp not being found:

```bash
# Ubuntu/Debian
sudo apt install yt-dlp

# macOS
brew install yt-dlp

# Or install via pip
pip install yt-dlp
```

### FFmpeg Not Found

```bash
# Ubuntu/Debian
sudo apt install ffmpeg

# macOS
brew install ffmpeg
```

## Stopping the Application

To stop the application, press `Ctrl+C` in the terminal where it's running.

## Next Steps

- Read the full [README.md](README.md) for detailed documentation
- Check out the API endpoints for integration with other tools
- Review the troubleshooting section for common issues

## Support

For issues or questions:
1. Check the [README.md](README.md) troubleshooting section
2. Review the application logs for error messages
3. Ensure all prerequisites are properly installed

## Legal Notice

⚠️ This tool is for personal use only. Always respect copyright laws and YouTube's Terms of Service.
