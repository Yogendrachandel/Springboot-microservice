package com.learn.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data // data annotation also contain geter setter and equal and hashcode etc
@AllArgsConstructor
@NoArgsConstructor
public class AccountsDto {

    @NotEmpty(message = "AccountNumber can not be a null or empty")
    @Pattern(regexp="(^$|[0-9]{10})",message = "AccountNumber must be 10 digits")
    private Long accountNumber;

    @NotEmpty(message = "AccountType can not be a null or empty")
    @NotNull(message = "AccountType can not be a null")
    private String accountType;

    @NotEmpty(message = "BranchAddress can not be a null or empty")
    private String branchAddress;

}