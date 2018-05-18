package com.imotions.letsencrypt.core;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeyStoreConfiguration {
    private String name;
    private String type;
    private String password;
    private String keyPairAlias;
    private String keyPairAliasPassword;
}