package com.imotions.letsencrypt.tls;

import com.imotions.letsencrypt.core.AmazonS3Configuration;
import com.imotions.letsencrypt.core.LetsEncryptConfiguration;

public interface HasLetsEncryptConfiguration {
    AmazonS3Configuration getS3Config();
    LetsEncryptConfiguration getLetsEncryptConfig();
}
