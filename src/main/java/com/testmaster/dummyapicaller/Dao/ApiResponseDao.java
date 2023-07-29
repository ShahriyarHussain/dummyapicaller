package com.testmaster.dummyapicaller.Dao;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class ApiResponseDao {

    private String apiName;
    private String apiUrl;
    private String restMethod;
}
