package com.vtt.security.Refresh;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends MongoRepository<RefreshToken,Long> {

    Optional<RefreshToken> findByRefreshToken(String token);

}
