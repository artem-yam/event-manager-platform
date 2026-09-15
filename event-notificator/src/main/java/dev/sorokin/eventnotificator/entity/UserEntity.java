package dev.sorokin.eventnotificator.entity;

import dev.sorokin.eventcommon.user.UserBaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_account")
public class UserEntity extends UserBaseEntity {
}
