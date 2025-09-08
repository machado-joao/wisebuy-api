package br.com.wisebuy.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemsListDTO {

    private Long listId;

    @NotBlank(message = "O nome da lista é obrigatório.")
    @Size(min = 2, max = 30, message = "O nome da lista deve conter entre 2 e 30 caracteres.")
    private String name;

    @NotBlank(message = "A descrição da lista é obrigatória.")
    @Size(max = 100, message = "A descrição da lista deve conter no máximo 100 caracteres.")
    private String description;

    private List<ItemDTO> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
