// UserProfileDTO.java
package com.ctecx.brs.tenant.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserProfileDTO {
    @NotBlank(message = "First name cannot be blank")
    @Size(max = 50)
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(max = 50)
    private String lastName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Please provide a valid email")
    @Size(max = 50)
    private String email;

    @Pattern(regexp = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$", message = "Please provide a valid phone number")
    @Size(max = 20)
    private String phone;

    @NotBlank(message = "Timezone cannot be blank")
    @Size(max = 50)
    private String timezone;
}
