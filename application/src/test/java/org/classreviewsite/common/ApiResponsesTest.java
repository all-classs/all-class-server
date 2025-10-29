package org.classreviewsite.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ApiResponses 클래스 테스트")
class ApiResponsesTest {

    @Nested
    @DisplayName("성공 응답 테스트")
    class SuccessResponseTest {

        @Test
        @DisplayName("데이터와 메시지를 포함한 성공 응답을 생성한다")
        void createSuccessResponseWithData() {
            // given
            String data = "test data";
            String message = "요청이 성공했습니다.";

            // when
            ApiResponses<String> response = ApiResponses.success(data, message);

            // then
            assertThat(response.getCode()).isEqualTo(ResultCode.SUCCESS);
            assertThat(response.getData()).isEqualTo(data);
            assertThat(response.getMessage()).isEqualTo(message);
        }

        @Test
        @DisplayName("null 데이터로 성공 응답을 생성할 수 있다")
        void createSuccessResponseWithNullData() {
            // given
            String message = "요청이 성공했습니다.";

            // when
            ApiResponses<String> response = ApiResponses.success(null, message);

            // then
            assertThat(response.getCode()).isEqualTo(ResultCode.SUCCESS);
            assertThat(response.getData()).isNull();
            assertThat(response.getMessage()).isEqualTo(message);
        }

        @Test
        @DisplayName("리스트 데이터로 성공 응답을 생성할 수 있다")
        void createSuccessResponseWithListData() {
            // given
            List<String> data = Arrays.asList("item1", "item2", "item3");
            String message = "목록 조회에 성공했습니다.";

            // when
            ApiResponses<List<String>> response = ApiResponses.success(data, message);

            // then
            assertThat(response.getCode()).isEqualTo(ResultCode.SUCCESS);
            assertThat(response.getData()).hasSize(3);
            assertThat(response.getData()).containsExactly("item1", "item2", "item3");
            assertThat(response.getMessage()).isEqualTo(message);
        }

        @Test
        @DisplayName("복잡한 객체로 성공 응답을 생성할 수 있다")
        void createSuccessResponseWithComplexObject() {
            // given
            TestDto dto = new TestDto(1L, "test");
            String message = "상세 조회에 성공했습니다.";

            // when
            ApiResponses<TestDto> response = ApiResponses.success(dto, message);

            // then
            assertThat(response.getCode()).isEqualTo(ResultCode.SUCCESS);
            assertThat(response.getData()).isNotNull();
            assertThat(response.getData().getId()).isEqualTo(1L);
            assertThat(response.getData().getName()).isEqualTo("test");
            assertThat(response.getMessage()).isEqualTo(message);
        }

        @Test
        @DisplayName("빈 문자열 메시지로 성공 응답을 생성할 수 있다")
        void createSuccessResponseWithEmptyMessage() {
            // given
            String data = "test";
            String message = "";

            // when
            ApiResponses<String> response = ApiResponses.success(data, message);

            // then
            assertThat(response.getCode()).isEqualTo(ResultCode.SUCCESS);
            assertThat(response.getData()).isEqualTo(data);
            assertThat(response.getMessage()).isEmpty();
        }
    }

    @Nested
    @DisplayName("실패 응답 테스트")
    class FailureResponseTest {

        @Test
        @DisplayName("BAD_REQUEST 코드로 실패 응답을 생성한다")
        void createFailureResponseWithBadRequest() {
            // given
            ResultCode resultCode = ResultCode.BAD_REQUEST;
            String message = "잘못된 요청입니다.";

            // when
            ApiResponses<String> response = ApiResponses.failure(resultCode, message);

            // then
            assertThat(response.getCode()).isEqualTo(ResultCode.BAD_REQUEST);
            assertThat(response.getData()).isNull();
            assertThat(response.getMessage()).isEqualTo(message);
        }

        @Test
        @DisplayName("UNAUTHORIZED 코드로 실패 응답을 생성한다")
        void createFailureResponseWithUnauthorized() {
            // given
            ResultCode resultCode = ResultCode.UNAUTHORIZED;
            String message = "인증이 필요합니다.";

            // when
            ApiResponses<Object> response = ApiResponses.failure(resultCode, message);

            // then
            assertThat(response.getCode()).isEqualTo(ResultCode.UNAUTHORIZED);
            assertThat(response.getData()).isNull();
            assertThat(response.getMessage()).isEqualTo(message);
        }

        @Test
        @DisplayName("FORBIDDEN 코드로 실패 응답을 생성한다")
        void createFailureResponseWithForbidden() {
            // given
            ResultCode resultCode = ResultCode.FORBIDDEN;
            String message = "접근 권한이 없습니다.";

            // when
            ApiResponses<Object> response = ApiResponses.failure(resultCode, message);

            // then
            assertThat(response.getCode()).isEqualTo(ResultCode.FORBIDDEN);
            assertThat(response.getData()).isNull();
            assertThat(response.getMessage()).isEqualTo(message);
        }

        @Test
        @DisplayName("NOT_FOUND 코드로 실패 응답을 생성한다")
        void createFailureResponseWithNotFound() {
            // given
            ResultCode resultCode = ResultCode.NOT_FOUND;
            String message = "리소스를 찾을 수 없습니다.";

            // when
            ApiResponses<Object> response = ApiResponses.failure(resultCode, message);

            // then
            assertThat(response.getCode()).isEqualTo(ResultCode.NOT_FOUND);
            assertThat(response.getData()).isNull();
            assertThat(response.getMessage()).isEqualTo(message);
        }

        @Test
        @DisplayName("INTERNAL_SERVER_ERROR 코드로 실패 응답을 생성한다")
        void createFailureResponseWithInternalServerError() {
            // given
            ResultCode resultCode = ResultCode.INTERNAL_SERVER_ERROR;
            String message = "서버 오류가 발생했습니다.";

            // when
            ApiResponses<Object> response = ApiResponses.failure(resultCode, message);

            // then
            assertThat(response.getCode()).isEqualTo(ResultCode.INTERNAL_SERVER_ERROR);
            assertThat(response.getData()).isNull();
            assertThat(response.getMessage()).isEqualTo(message);
        }

        @Test
        @DisplayName("실패 응답은 항상 데이터가 null이다")
        void failureResponseAlwaysHasNullData() {
            // given
            ResultCode resultCode = ResultCode.BAD_REQUEST;
            String message = "에러 메시지";

            // when
            ApiResponses<String> response = ApiResponses.failure(resultCode, message);

            // then
            assertThat(response.getData()).isNull();
        }
    }

    @Nested
    @DisplayName("toString 테스트")
    class ToStringTest {

        @Test
        @DisplayName("성공 응답의 toString은 모든 필드를 포함한다")
        void successResponseToStringContainsAllFields() {
            // given
            ApiResponses<String> response = ApiResponses.success("data", "message");

            // when
            String toString = response.toString();

            // then
            assertThat(toString).contains("SUCCESS");
            assertThat(toString).contains("data");
            assertThat(toString).contains("message");
        }

        @Test
        @DisplayName("실패 응답의 toString은 모든 필드를 포함한다")
        void failureResponseToStringContainsAllFields() {
            // given
            ApiResponses<Object> response = ApiResponses.failure(ResultCode.BAD_REQUEST, "error message");

            // when
            String toString = response.toString();

            // then
            assertThat(toString).contains("BAD_REQUEST");
            assertThat(toString).contains("error message");
        }
    }

    // Test DTO helper class
    private static class TestDto {
        private final Long id;
        private final String name;

        public TestDto(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}