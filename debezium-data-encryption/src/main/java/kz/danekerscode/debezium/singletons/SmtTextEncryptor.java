package kz.danekerscode.debezium.singletons;

import org.jasypt.util.text.StrongTextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SmtTextEncryptor {
    private static final StrongTextEncryptor textEncryptor;
    private static final Logger log = LoggerFactory.getLogger(SmtTextEncryptor.class);

    static {
        var passwordFromEnv = System.getenv("ENCRYPTION_PASSWORD");
        checkPassword(passwordFromEnv);

        textEncryptor = new StrongTextEncryptor();
        textEncryptor.setPassword(passwordFromEnv);

        log.info("Successfully loaded environment variable ENCRYPTION_PASSWORD");
    }

    private SmtTextEncryptor() {
    }

    public static String encrypt(String text) {
        return textEncryptor.encrypt(text);
    }

    private static void checkPassword(String password) {
        if (password == null) {
            System.exit(-1);
            throw new IllegalStateException("Env param ENCRYPTION_PASSWORD is null");
        }

        if (password.length() < 16) {
            throw new IllegalStateException("Env param ENCRYPTION_PASSWORD should be at least 16 characters");
        }
    }
}
