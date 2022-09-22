package com.login.domain;

import com.login.util.code.YnCode;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "TB_PROFILE_IMAGE")
@NoArgsConstructor
public class ProfileImage extends CommonBaseDateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IMG_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @Column(name = "IMG_FILE_NAME", nullable = false)
    private String imgFileName;

    @Column(name = "IMG_ORIGIN_FILE_NAME", nullable = false)
    private String originFileName;

    @Column(name = "USE_YN", nullable = false)
    private YnCode useYn;

    @Builder
    public ProfileImage(Long id, Profile profile, String imgFileName, String originFileName, YnCode useYn) {
        this.id = id;
        this.profile = profile;
        this.imgFileName = imgFileName;
        this.originFileName = originFileName;
        this.useYn = useYn;
    }

    public void updateImageFile(String imgFileName, String originFileName){
        this.imgFileName = imgFileName;
        this.originFileName = originFileName;
    }

}