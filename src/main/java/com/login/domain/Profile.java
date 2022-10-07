package com.login.domain;

import com.login.util.code.YnCode;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;


@Entity
@Getter
@Table(name = "TB_PROFILE")
@NoArgsConstructor
public class Profile extends CommonBaseDateTime {

    @Id
    @Column(name = "PROFILE_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long profileId;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Column(name = "NICKNAME", nullable = false, length = 40)
    private String nickname;

    @Column(name = "PROFILE_IMG_NAME")
    private String profileImgName;

    @Column(name = "INTRODUCE", length = 3000)
    private String introduce;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profile")
    private List<ProfileImage> imageList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "MAIN_PROFILE_YN", nullable = false)
    private YnCode mainProfileYn;

    @Enumerated(EnumType.STRING)
    @Column(name = "USE_YN", nullable = false)
    private YnCode useYn;


    @Builder
    public Profile(Long profileId, User user, String nickname, String profileImgName, String introduce, YnCode useYn, YnCode mainProfileYn) {
        this.profileId = profileId;
        this.user = user;
        this.nickname = nickname;
        this.profileImgName = profileImgName;
        this.introduce = StringUtils.defaultIfEmpty(introduce,"");
        this.mainProfileYn = mainProfileYn;
        this.useYn = useYn;
    }

    /**
     * 프로필 정보 수정
     *
     * @param nickname  변경할 닉네임
     * @param introduce 변경할 자기소개
     */
    public void updateProfile(String nickname, String introduce) {
        this.nickname = nickname;
        this.introduce = StringUtils.defaultIfEmpty(introduce, "");
    }

    /**
     * 프로필 대표 이미지 변경
     *
     * @param imageName 대표 이미지 파일명
     */
    public void updateProfileImage(String imageName) {
        this.profileImgName = imageName;
    }
}

