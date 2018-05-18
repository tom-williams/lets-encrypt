package com.imotions.letsencrypt;

import com.imotions.letsencrypt.api.DebugResource;
import com.imotions.letsencrypt.api.LetsEncryptResource;
import com.imotions.letsencrypt.tls.LoadSslBundle;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.security.Security;

@Slf4j
public class LetsEncryptTestApplication extends Application<LetsEncryptTestConfiguration> {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static void main(String[] args) throws Exception {
        new LetsEncryptTestApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<LetsEncryptTestConfiguration> bootstrap) {
        bootstrap.addBundle(new LoadSslBundle<>());

        // Enable environment variable substitution in configuration file.
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor()
                )
        );
    }

    @Override
    public void run(LetsEncryptTestConfiguration configuration, Environment environment) throws Exception {
        if (Cipher.getMaxAllowedKeyLength("AES/CBC/PKCS5Padding") < Integer.MAX_VALUE) {
            log.error("Cryptography restrictions detected, please install the unlimited strength JCE policy file.");
        }

        environment.jersey().register(DebugResource.class);
        environment.jersey().register(LetsEncryptResource.class);

//        environment.servlets().addServlet("letsencrypt", new LetsEncryptChallengeServlet(getDatabase()))
//                .addMapping(LetsEncryptChallengeServlet.URL_PATTERN);

    }
}
