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

@Entity
@Table(name = "TB_PROFILE_IMAGE")
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

    @Column(name = "IMG_FILE_ORIGIN_NAME", nullable = false)
    private String imgFileOriginName;

    @Column(name = "USE_YN", nullable = false)
    private YnCode useYn;

}