package org.classreviewsite.util;

import org.assertj.core.api.Assertions;
import org.classreviewsite.common.util.NumberFormat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UtilsTest {

    @Test
    @DisplayName("소수점이 깊은 실수값을 소수점 첫째자리 실수형태로 변환한다.")
    void numberFormat() {
        // given
        Double number = 1.4532455;

        // when
        Double translatedNumber = NumberFormat.format(number);
        String strValue = Double.toString(translatedNumber);

        // then
        Assertions.assertThat(strValue.length()).isEqualTo(3);
    }

}
