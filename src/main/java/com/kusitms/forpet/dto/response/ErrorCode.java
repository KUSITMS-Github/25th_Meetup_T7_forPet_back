package com.kusitms.forpet.dto.response;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 400 BAD_REQUEST : 잘못된 요청 */
    UNAUTHORIZED_REDIRECT_URI(BAD_REQUEST, "인증되지 않은 Redirect URI 입니다."),
    INVALID_REFRESH_TOKEN(BAD_REQUEST, "리프레시 토큰이 유효하지 않습니다"),
    MISMATCH_REFRESH_TOKEN(BAD_REQUEST, "리프레시 토큰의 유저 정보가 일치하지 않습니다"),
    NOT_EXPIRED_TOKEN(BAD_REQUEST, "토큰이 만료되지 않았습니다."),

    CANNOT_DUPLICATE_LIKE(BAD_REQUEST, "이미 좋아요한 게시물입니다."),
    CANNOT_DUPLICATE_BOOKMARK(BAD_REQUEST, "이미 북마크한 게시물입니다."),

    CANNOT_CERTIFY(BAD_REQUEST, "인증 정보를 함께 등록해야 합니다."),

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    INVALID_AUTH_TOKEN(UNAUTHORIZED, "권한 정보가 없는 토큰입니다"),
    EXPIRED_AUTH_TOKEN(UNAUTHORIZED, "액세스 토큰이 만료되었습니다. 토큰을 재발급해주세요."),
    UNAUTHORIZED_MEMBER(UNAUTHORIZED, "현재 내 계정 정보가 존재하지 않습니다"),
    UNSUPPORTED_AUTH_TOKEN(UNAUTHORIZED, "지원되지 않는 토큰입니다."),
    WRONG_TOKEN(UNAUTHORIZED, "잘못된 형식의 토큰입니다."),

    /* 403 FORBIDDEN : 허가되지 않은 사용자 */

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    MEMBER_NOT_FOUND(NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다"),
    REFRESH_TOKEN_NOT_FOUND(NOT_FOUND, "로그아웃 된 사용자입니다"),
    NOT_FOLLOW(NOT_FOUND, "팔로우 중이지 않습니다"),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_RESOURCE(CONFLICT, "데이터가 이미 존재합니다"),

    /* 500 INTERNAL SERVER ERROR */
    UNKNOWN_ERROR(INTERNAL_SERVER_ERROR, "승인되지 않은 오류로 응답합니다.");

    ;

    private final HttpStatus httpStatus;
    private final String detail;
}
