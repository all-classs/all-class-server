package org.classreviewsite.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ResultCode Enum 테스트")
class ResultCodeTest {

    @Test
    @DisplayName("SUCCESS 상태 코드는 200이다")
    void successCodeIs200() {
        // given & when
        ResultCode resultCode = ResultCode.SUCCESS;

        // then
        assertThat(resultCode.getCode()).isEqualTo(200);
        assertThat(resultCode.getMessage()).isEqualTo("Success");
    }

    @Test
    @DisplayName("BAD_REQUEST 상태 코드는 400이다")
    void badRequestCodeIs400() {
        // given & when
        ResultCode resultCode = ResultCode.BAD_REQUEST;

        // then
        assertThat(resultCode.getCode()).isEqualTo(400);
        assertThat(resultCode.getMessage()).isEqualTo("Bad Request");
    }

    @Test
    @DisplayName("UNAUTHORIZED 상태 코드는 401이다")
    void unauthorizedCodeIs401() {
        // given & when
        ResultCode resultCode = ResultCode.UNAUTHORIZED;

        // then
        assertThat(resultCode.getCode()).isEqualTo(401);
        assertThat(resultCode.getMessage()).isEqualTo("Unauthorized");
    }

    @Test
    @DisplayName("FORBIDDEN 상태 코드는 403이다")
    void forbiddenCodeIs403() {
        // given & when
        ResultCode resultCode = ResultCode.FORBIDDEN;

        // then
        assertThat(resultCode.getCode()).isEqualTo(403);
        assertThat(resultCode.getMessage()).isEqualTo("Forbidden");
    }

    @Test
    @DisplayName("NOT_FOUND 상태 코드는 404이다")
    void notFoundCodeIs404() {
        // given & when
        ResultCode resultCode = ResultCode.NOT_FOUND;

        // then
        assertThat(resultCode.getCode()).isEqualTo(404);
        assertThat(resultCode.getMessage()).isEqualTo("Not Found");
    }

    @Test
    @DisplayName("INTERNAL_SERVER_ERROR 상태 코드는 500이다")
    void internalServerErrorCodeIs500() {
        // given & when
        ResultCode resultCode = ResultCode.INTERNAL_SERVER_ERROR;

        // then
        assertThat(resultCode.getCode()).isEqualTo(500);
        assertThat(resultCode.getMessage()).isEqualTo("Internal Server Error");
    }

    @Test
    @DisplayName("ResultCode는 6개의 상태를 가진다")
    void resultCodeHasSixValues() {
        // when
        ResultCode[] values = ResultCode.values();

        // then
        assertThat(values).hasSize(6);
    }

    @Test
    @DisplayName("ResultCode는 이름으로 조회할 수 있다")
    void resultCodeCanBeFoundByName() {
        // when
        ResultCode resultCode = ResultCode.valueOf("SUCCESS");

        // then
        assertThat(resultCode).isEqualTo(ResultCode.SUCCESS);
    }
}