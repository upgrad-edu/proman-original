/*
 * Copyright 2018-2019, https://beingtechie.io.
 *
 * File: AuthorizationServiceImpl.java
 * Date: May 5, 2018
 * Author: Thribhuvan Krishnamurthy
 */
package apps.proman.service.user.domain;

import java.time.ZonedDateTime;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import apps.proman.service.common.data.RequestContext;
import apps.proman.service.user.dao.UserAuthDao;
import apps.proman.service.user.dao.UserDao;
import apps.proman.service.user.entity.UserAuthTokenEntity;
import apps.proman.service.user.entity.UserEntity;
import apps.proman.service.user.model.LogoutAction;
import apps.proman.service.user.model.UserStatus;
import apps.proman.service.user.provider.token.JwtTokenProvider;
import apps.proman.service.user.provider.token.Token;

/**
 * Implementation of {@link AuthorizationService}.
 */
@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserAuthDao userAuthDao;


    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void loginAttemptFailed(final RequestContext requestContext, final UserEntity userEntity) {
        int failedLoginCount = userEntity.getFailedLoginCount();
        if (userEntity.getFailedLoginCount() <= 5) {
            failedLoginCount++;
            userEntity.setFailedLoginCount(failedLoginCount);
        }

        if (failedLoginCount == 5) {
            userEntity.setStatus(UserStatus.LOCKED.getCode());
        }
        userDao.update(userEntity);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void resetFailedLoginAttempt(@NotNull RequestContext requestContext, @NotNull UserEntity userEntity) {
        userEntity.setFailedLoginCount(0);
        userDao.update(userEntity);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public UserAuthTokenEntity authorizeAccessToken(RequestContext requestContext, UserEntity userEntity) {

        final UserAuthTokenEntity userAuthToken = userAuthDao.findToken(userEntity.getId(), requestContext.getClientId(), requestContext.getOriginIpAddress());
        final UserAuthTokenVerifier userAuthTokenVerifier = new UserAuthTokenVerifier(userAuthToken);

        switch (userAuthTokenVerifier.getStatus()) {
            case NOT_FOUND:
                return issueNewToken(requestContext, userEntity);
            case ACTIVE:
            case CONCURRENT_LOGIN:
                return userAuthToken;
            case LOGGED_OUT:
                return issueNewToken(requestContext, userEntity);
            case EXPIRED:
                performLogout(userAuthToken, LogoutAction.EXPIRED, requestContext.getRequestTime());
                return issueNewToken(requestContext, userEntity);
            default:
                throw new IllegalArgumentException("Invalid status");
        }

    }

    @Override
    public UserAuthTokenEntity findAccessToken(final RequestContext requestContext, final String accessToken) {
        return userAuthDao.findToken(accessToken);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void logout(final RequestContext requestContext, final UserAuthTokenEntity userAuthToken, final LogoutAction logoutAction) {
        performLogout(userAuthToken, logoutAction, requestContext.getRequestTime());
    }

    private void performLogout(final UserAuthTokenEntity userAuthToken, final LogoutAction logoutAction, final ZonedDateTime logoutTime) {
        userAuthToken.setLogoutAt(logoutTime);
        userAuthToken.setLogoutAction(logoutAction.getCode());
        userAuthDao.update(userAuthToken);
    }

    private UserAuthTokenEntity issueNewToken(final RequestContext requestContext, final UserEntity userEntity) {

        final ZonedDateTime requestTime = requestContext.getRequestTime();

        // FIXME - Read the expiration time from configuration
        final ZonedDateTime expirationTime = requestTime.plusHours(8);

        final JwtTokenProvider tokenProvider = new JwtTokenProvider(userEntity.getPassword());


        final Token tokenSpec = new Token.Builder().clientId(requestContext.getClientId()).clientIpAddress(requestContext.getOriginIpAddress()) //
                .issuedTime(requestTime).expirationTime(expirationTime) //
                .userId(userEntity.getUuid()).userFirstname(userEntity.getFirstName()).userLastname(userEntity.getLastName()) //
                .userEmailAddress(userEntity.getEmail()).userMobilePhone(userEntity.getMobilePhone()).lastLoginAt(userEntity.getLastLoginAt()) //
                .roleId(userEntity.getRole().getUuid()).roleName(userEntity.getRole().getName()).build();

        final String authToken = tokenProvider.serialize(tokenSpec);

        LOGGER.debug("Generated authToken: " + authToken);

        final UserAuthTokenEntity authTokenEntity = new UserAuthTokenEntity();
        authTokenEntity.setUser(userEntity);
            authTokenEntity.setAccessToken(authToken);
        authTokenEntity.setClientId(requestContext.getClientId());
        authTokenEntity.setOriginIpAddress(requestContext.getOriginIpAddress());
        authTokenEntity.setLoginAt(requestTime);
        authTokenEntity.setExpiresAt(expirationTime);

        userAuthDao.create(authTokenEntity);

        userEntity.setLastLoginAt(requestTime);
        userDao.update(userEntity);

        return authTokenEntity;
    }

}