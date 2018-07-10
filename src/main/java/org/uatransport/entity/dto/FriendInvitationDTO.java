package org.uatransport.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendInvitationDTO {
    private String friendName;
    private String friendEmail;
}
