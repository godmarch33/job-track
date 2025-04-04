package co.uk.offerland.job_track.application.dto.user;



/*

пользователь - основной объект, с которым связаны все остальные (через внешние ключи)

 */

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegister {

    private Long id;
    private String email;
    private String username;
    private String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRegister user = (UserRegister) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return username;
    }
}
