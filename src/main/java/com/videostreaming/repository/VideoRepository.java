package com.videostreaming.repository;

import com.videostreaming.model.Video;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends MongoRepository<Video, String> {
    // Métodos adicionales si son necesarios
}
