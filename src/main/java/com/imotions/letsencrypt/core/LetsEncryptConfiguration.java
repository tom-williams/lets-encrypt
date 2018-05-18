package com.imotions.letsencrypt.core;

import io.dropwizard.jetty.HttpsConnectorFactory;
import io.dropwizard.server.DefaultServerFactory;
import io.dropwizard.server.ServerFactory;
import lombok.Getter;
import lombok.Setter;
import org.shredzone.acme4j.util.CSRBuilder;

import java.net.URI;
import java.util.Optional;

@Getter
@Setter
public class LetsEncryptConfiguration {
    /** Domain Name for which LetsEncrypt should issue certificate for */
    private String domainName;

    /** LetsEncrypt issued unique account URI to associate our Account KeyStore with */
    private URI accountURI;

    /** Current URI of the AcmeServer of where to request a certificate */
    private URI acmeServer;

    /** The relative location of where to fetch and store our KeyStore with our public Certificate issued by LetsEncrypt */
    private String s3KeyStoreLocation;

    /** Name of the (O)rganization that LetsEncrypt should issue the certificate for */
    private String organization = "iMotions";

    KeyStoreConfiguration domainKeyStore;
    KeyStoreConfiguration accountKeyStore;

    public CSRBuilder createSigningRequest() {
        CSRBuilder csrb = new CSRBuilder();
        csrb.addDomain(domainName);
        csrb.setOrganization(organization);
        return csrb;
    }

    public String pathToKeyStore() {
        return s3KeyStoreLocation + domainKeyStore.getName();
    }

    public static Optional<HttpsConnectorFactory> getHttpsConnector(ServerFactory serverFactory) {
        if (!(serverFactory instanceof DefaultServerFactory)) {
            return Optional.empty();
        }

        return ((DefaultServerFactory) serverFactory).getApplicationConnectors().stream()
                .filter(c -> c instanceof HttpsConnectorFactory)
                .map(c -> (HttpsConnectorFactory) c)
                .findFirst();
    }
}

