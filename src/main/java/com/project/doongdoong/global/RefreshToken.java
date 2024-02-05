package com.project.doongdoong.global;

import com.project.doongdoong.domain.user.model.SocialType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;


@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "jwtToken", timeToLive = 60 * 60 * 24 * 14)
public class RefreshToken implements Serializable {

    @Id
    private String id;

    @Indexed
    private String refreshToken;
    // 권한 필드가 추후 필요하면 추가

    private String socialType;

    public static RefreshToken of(String id, String refreshToken, String socialType){
        return RefreshToken.builder()
                .id(id)
                .socialType(socialType)
                .refreshToken(refreshToken)
                .build();
    }
}
