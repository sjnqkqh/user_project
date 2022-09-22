package com.login.domain;

import com.login.dto.profile.req.UpdateProfileReqDto;
import com.login.util.code.YnCode;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Table(name = "TB_PROFILE")
@NoArgsConstructor
public class Profile extends CommonBaseDateTime {

    @Id
    @Column(name = "PROFILE_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long profileId;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Column(name = "NICKNAME", nullable = false, length = 40)
    private String nickname;

    @Column(name = "PROFILE_IMG_NAME")
    private String profileImgName;

    @Column(name = "INTRODUCE", length = 3000)
    private String introduce;

    @Enumerated(EnumType.STRING)
    @Column(name = "USE_YN", nullable = false)
    private YnCode useYn;

    @Builder
    public Profile(Long profileId, User user, String nickname, String profileImgName, String introduce, YnCode useYn) {
        this.profileId = profileId;
        this.user = user;
        this.nickname = nickname;
        this.profileImgName = profileImgName;
        this.introduce = introduce;
        this.useYn = useYn;
    }

    public void updateProfile(UpdateProfileReqDto reqDto){
        this.nickname = reqDto.getNickname();
        this.introduce = reqDto.getIntroduce();
    }

    public void updateProfileImage(String imageName){
        this.profileImgName = imageName;
    }
}

