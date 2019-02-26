package com.geekhalo.ddd.lite.domain.support.entity;

import com.geekhalo.ddd.lite.domain.ValidationHandler;
import com.geekhalo.ddd.lite.domain.support.jpa.IdentitiedJpaEntity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Table;

import static com.geekhalo.ddd.lite.domain.util.UUIDUtil.genUUID;

@Data

@Entity
@Table(name = "tb_user2")
public class User extends IdentitiedJpaEntity<UserId> {
    private String name;
    private String password;

    @Setter(AccessLevel.PRIVATE)
    private UserStatus status;

    public static User create(String name, String password){
        UserId userId = UserId.apply(genUUID());
        User user = new User();
        user.setId(userId);
        user.setName(name);
        user.setPassword(password);
        user.setStatus(UserStatus.ENABLE);
        return user;
    }

    public void update(String name, String password){
        setName(name);
        setPassword(password);
    }

    public void enable(){
        setStatus(UserStatus.ENABLE);
    }

    public void disable(){
        setStatus(UserStatus.DISABLE);
    }

    @Override
    public void validate(ValidationHandler handler) {
        super.validate(handler);
        if (StringUtils.isEmpty(getName())){
            handler.handleError("user name can not be null");
        }
        if (StringUtils.isEmpty(getPassword())){
            handler.handleError("user password can not be null");
        }
    }
}
