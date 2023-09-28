package com.eddie.youtubeclone.service;

import com.eddie.youtubeclone.model.Video;
import com.eddie.youtubeclone.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final S3Service s3Service;
    private final VideoRepository videoRepository;

    public void uploadVideo(MultipartFile multipartFile) {
        // upload file to aws S3
        // Save video data to database
        String videoUrl = s3Service.uploadFile(multipartFile);
        var video = new Video(); // should use DI instead of new an Video object
        video.setVideoUrl(videoUrl);

        videoRepository.save(video);
    }
}
