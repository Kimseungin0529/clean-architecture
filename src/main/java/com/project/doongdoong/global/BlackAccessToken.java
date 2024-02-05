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
@RedisHash(value = "blackToken", timeToLive = 60 * 30)
public class BlackAccessToken implements Serializable {

    @Id
    private String id;

    @Indexed
    private String accessToken;
    // 권한 필드가 추후 필요하면 추가
    @Indexed
    private String otherKey;

    private String socialType;

    public static BlackAccessToken of(String id, String accessToken, String socialType){
        return BlackAccessToken.builder()
                .id(id)
                .socialType(socialType)
                .accessToken(accessToken)
                .otherKey(id + " " + socialType)
                .build();
    }
}
