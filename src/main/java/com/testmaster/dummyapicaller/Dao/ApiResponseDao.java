package com.testmaster.dummyapicaller.Dao;

import com.testmaster.dummyapicaller.Enum.RequestTypes;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class ApiResponseDao {

    private String apiName;
    private String apiUrl;
    private RequestTypes restMethod;
}
