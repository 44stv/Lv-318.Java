package org.uatransport.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.uatransport.entity.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllUsersDTO {
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
}
