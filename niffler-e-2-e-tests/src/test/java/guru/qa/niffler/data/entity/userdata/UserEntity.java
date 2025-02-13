package guru.qa.niffler.data.entity.userdata;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Getter
@Setter
public class UserEntity implements Serializable {
    private UUID id;
    private String username;
    private CurrencyValues currency;
    private String firstname;
    private String surname;
    private String fullname;
    private byte[] photo;
    private byte[] photoSmall;

    public static UserEntity fromJson(UserJson json) {
        UserEntity ue = new UserEntity();
        ue.setId(json.id());
        ue.setUsername(json.username());
        ue.setCurrency(json.currency());
        ue.setFirstname(json.firstname());
        ue.setFullname(json.fullname());
        ue.setPhoto(json.photo() != null && !json.photo().isEmpty() ? json.photo().getBytes(StandardCharsets.UTF_8) : null);
        ue.setPhotoSmall(json.photoSmall() != null && !json.photoSmall().isEmpty() ? json.photoSmall().getBytes(StandardCharsets.UTF_8) : null);

        return ue;
    }
}
