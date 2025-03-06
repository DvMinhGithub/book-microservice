package com.mdv.identity.dto.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileCreateRequest {
    String userId;
    String firstName;
    String lastName;
    LocalDate dob;
    String city;
}
