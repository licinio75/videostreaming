package com.videostreaming.service;

import com.videostreaming.model.Video;
import com.videostreaming.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;


import java.util.UUID;

@Service
public class VideoService {

    @Autowired
    private S3Client s3Client;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Value("${kafka.topic.name}")
    private String kafkaTopic;

    public Video uploadVideo(String fileName, MultipartFile file) throws Exception {
        String uniqueFileName = UUID.randomUUID() + "_" + fileName;
        String fileUrl = bucketName + "/" + uniqueFileName;

        // Subir el archivo a MinIO
        s3Client.putObject(
            PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(uniqueFileName)
                    .build(),
            RequestBody.fromInputStream(file.getInputStream(), file.getSize())
    );

        // Guardar los metadatos en la base de datos
        Video video = new Video();
        video.setName(fileName);
        video.setUrl(fileUrl);
        Video savedVideo = videoRepository.save(video);

        // Enviar mensaje a Kafka
        String message = String.format("Video uploaded: {id: %s, name: %s, url: %s}", savedVideo.getId(), savedVideo.getName(), savedVideo.getUrl());
        kafkaTemplate.send(kafkaTopic, message);

        return savedVideo;
    }
}
