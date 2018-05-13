/*
 * Copyright 2018-2019, https://beingtechie.io.
 *
 * File: UserAuthTokenEntity.java
 * Date: May 5, 2018
 * Author: Thribhuvan Krishnamurthy
 */
package apps.proman.service.user.entity;

import static apps.proman.service.common.entity.Entity.SCHEMA;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import apps.proman.service.common.entity.Identifier;
import apps.proman.service.common.entity.MutableEntity;
import apps.proman.service.common.entity.ext.EntityEqualsBuilder;
import apps.proman.service.common.entity.ext.EntityHashCodeBuilder;

/**
 * User Entity JPA mapping class.
 */
@Entity
@Table(name = "USER_AUTH_TOKENS", schema = SCHEMA)
@NamedQueries({ //
        @NamedQuery(name = UserAuthTokenEntity.TOKEN_BY_USER_AND_CLIENT, //
                query = "SELECT uat FROM UserAuthTokenEntity uat WHERE uat.user.id = :userId AND uat.clientId = :clientId AND uat.originIpAddress = :originIpAddress ORDER BY uat.createdAt DESC"), //
        @NamedQuery(name = UserAuthTokenEntity.TOKEN_BY_ACCESS_TOKEN, //
                query = "SELECT uat FROM UserAuthTokenEntity uat WHERE uat.accessToken = :accessToken"), //
        @NamedQuery(name = UserAuthTokenEntity.VALID_TOKEN_BY_USER, //
                query = "SELECT uat FROM UserAuthTokenEntity uat WHERE uat.user.id = :userId AND uat.expiresAt > :currentTime AND uat.logoutAt is null"), //
        @NamedQuery(name = UserAuthTokenEntity.EXPIRED_TOKEN_BY_USER, //
                query = "SELECT uat FROM UserAuthTokenEntity uat WHERE uat.user.id = :userId AND uat.expiresAt <= :currentTime AND uat.logoutAt is null") //
})
public class UserAuthTokenEntity extends MutableEntity implements Identifier<Long>, Serializable {

    private static final long serialVersionUID = -8078348738674166228L;

    public static final String TOKEN_BY_USER_AND_CLIENT = "UserAuthTokenEntity.latestAuthTokenByUserAndClient";

    public static final String TOKEN_BY_ACCESS_TOKEN = "UserAuthTokenEntity.authTokenByAccessToken";

    public static final String VALID_TOKEN_BY_USER = "UserAuthTokenEntity.validTokenByUser";

    public static final String EXPIRED_TOKEN_BY_USER = "UserAuthTokenEntity.expiredTokenByUser";

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private UserEntity user;

    @Column(name = "CLIENT_ID")
    @NotNull
    @Size(max = 20)
    private String clientId;

    @Column(name = "ORIGIN_IPADDRESS")
    @NotNull
    @Size(max = 20)
    private String originIpAddress;

    @Column(name = "ACCESS_TOKEN")
    @NotNull
    @Size(max = 500)
    private String accessToken;

    @Column(name = "REFRESH_TOKEN")
    @Size(max = 500)
    private String refreshToken;

    @Column(name = "LOGIN_AT")
    @NotNull
    private ZonedDateTime loginAt;

    @Column(name = "EXPIRES_AT")
    @NotNull
    private ZonedDateTime expiresAt;

    @Column(name = "LOGOUT_AT")
    private ZonedDateTime logoutAt;

    @Column(name = "LOGOUT_ACTION")
    private int logoutAction;

    @Override
    public Long getId() {
        return id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getOriginIpAddress() {
        return originIpAddress;
    }

    public void setOriginIpAddress(String ipAddress) {
        this.originIpAddress = ipAddress;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public ZonedDateTime getLoginAt() {
        return loginAt;
    }

    public void setLoginAt(ZonedDateTime loginTime) {
        this.loginAt = loginTime;
    }

    public ZonedDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(ZonedDateTime expiryTime) {
        this.expiresAt = expiryTime;
    }

    public ZonedDateTime getLogoutAt() {
        return logoutAt;
    }

    public void setLogoutAt(ZonedDateTime logoutTime) {
        this.logoutAt = logoutTime;
    }

    public int getLogoutAction() {
        return logoutAction;
    }

    public void setLogoutAction(int logoutAction) {
        this.logoutAction = logoutAction;
    }

    @Override
    public boolean equals(Object obj) {
        return new EntityEqualsBuilder<Long>().equalsById(this, obj);
    }

    @Override
    public int hashCode() {
        return new EntityHashCodeBuilder<Long>().hashCodeById(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
