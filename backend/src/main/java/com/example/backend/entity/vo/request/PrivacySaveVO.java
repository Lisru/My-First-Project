package com.example.backend.entity.vo.request;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PrivacySaveVO {
    //为保证性能，用户勾选哪个，就处理哪个
    @Pattern(regexp = "phone|email|qq|wx|gender")
    String type;
    boolean status;
}
