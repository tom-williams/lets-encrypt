package com.imotions.letsencrypt;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.imotions.letsencrypt.core.AmazonS3Configuration;
import com.imotions.letsencrypt.core.LetsEncryptConfiguration;
import com.imotions.letsencrypt.core.PublicS3Url;
import com.imotions.letsencrypt.tls.HasLetsEncryptConfiguration;
import io.dropwizard.Configuration;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class LetsEncryptTestConfiguration extends Configuration implements HasLetsEncryptConfiguration {
    @NotNull
    @Valid
    private PublicS3Url publicS3Url;

    @NotNull
    @Valid
    @JsonProperty("s3")
    private AmazonS3Configuration s3Config;

    @JsonProperty("letsEncrypt")
    private LetsEncryptConfiguration letsEncryptConfig = new LetsEncryptConfiguration();
}
