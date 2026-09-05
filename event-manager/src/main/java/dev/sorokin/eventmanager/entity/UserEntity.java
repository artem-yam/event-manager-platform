package dev.sorokin.eventmanager.entity;

import dev.sorokin.eventcommon.user.UserBaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_account")
public class UserEntity extends UserBaseEntity {

    public UserEntity(String login, Integer age, String passwordHash, String role) {
        super(login, age, passwordHash, role);
    }

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<RegistrationEntity> registrations = new ArrayList<>();

    @OneToMany(
            mappedBy = "owner",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<EventEntity> ownedEvents = new ArrayList<>();

}