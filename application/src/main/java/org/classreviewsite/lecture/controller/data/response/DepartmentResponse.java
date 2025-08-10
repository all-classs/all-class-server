package org.classreviewsite.lecture.controller.data.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DepartmentResponse {
    private int status;
    private List<String> DeaprtmentList;
    private String message;
}
