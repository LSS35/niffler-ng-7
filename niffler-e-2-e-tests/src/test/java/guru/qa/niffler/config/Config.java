package guru.qa.niffler.config;

public interface Config {

    static Config getInstance() {
        return LocalConfig.INSTANCE;
    }

    String frontUrl();

    String spendUrl();

    String username();

    String password();

    String ghUrl();

    String authUrl();

    String gatewayUrl();

    String userdataUrl();

    String userdataJdbcUrl();

    String spendJdbcUrl();

    String currencyJdbcUrl();
}
