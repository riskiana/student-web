package com.ubl.studentweb.domain;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
public class Student {

    private String nim;

    @NotBlank(message = "full name is required")
    @Size(min = 3, max = 50)
    private String fullName;

    private String address;
    @DateTimeFormat(pattern = "yyyy-MM-dd")

    @NotNull(message = "Date of Birth is required")
    private Date dateOfBirth;

    @NotEmpty(message = "Password should not be empty")
    private String password;

}
