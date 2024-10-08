package org.khtml.hexagonal.domain.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.khtml.hexagonal.domain.common.BaseEntity;

@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends BaseEntity {

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "user_type")
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "username")
    private String username;

    @Column(name = "number_of_persons")
    private Integer numberOfPersons;

    @Column(name = "location_consent")
    private Boolean locationConsent;

    @Builder
    public User(String providerId, UserType userType, String phoneNumber, String username, Integer numberOfPersons, Boolean locationConsent) {
        this.providerId = providerId;
        this.userType = userType;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.numberOfPersons = numberOfPersons;
        this.locationConsent = locationConsent;
    }

}
