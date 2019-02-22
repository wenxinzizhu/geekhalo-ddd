package com.geekhalo.ddd.lite.domain.support.vo;

import com.geekhalo.ddd.lite.domain.ValueObject;
import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Data
@Setter(AccessLevel.PRIVATE)
public class Email implements ValueObject {
    @Column(name = "email_name")
    private String name;
    @Column(name = "email_domain")
    private String domain;

    private Email() {
    }

    private Email(String name, String domain) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(name), "name can not be null");
        Preconditions.checkArgument(StringUtils.isNotEmpty(domain), "domain can not be null");
        this.setName(name);
        this.setDomain(domain);
    }

    public static Email apply(String email) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(email), "email can not be null");
        String[] ss = email.split("@");
        Preconditions.checkArgument(ss.length == 2, "not Email");
        return new Email(ss[0], ss[1]);
    }

    @Override
    public String toString() {
        return this.getName() + "@" + this.getDomain();
    }
}
