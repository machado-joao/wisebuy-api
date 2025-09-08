package br.com.wisebuy.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignInDTO {

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "O e-mail deve ser válido.")
    @Size(max = 254, message = "O e-mail deve conter no máximo 254 caracteres.")
    private String email;

    @NotBlank(message = "A senha é obrigatória.")
    private String password;

}
