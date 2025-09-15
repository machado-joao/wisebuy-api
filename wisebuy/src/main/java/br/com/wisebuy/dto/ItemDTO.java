package br.com.wisebuy.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemDTO {

    private Long itemId;
    private Long listId;

    @NotBlank(message = "O nome do item é obrigatório.")
    @Size(min = 2, max = 30, message = "O nome do item deve conter entre 2 e 30 caracteres.")
    private String name;

    @NotNull(message = "A quantidade é obrigatória.")
    @Min(value = 1, message = "A quantidade mínima é 1.")
    private Long quantity;

    @NotNull(message = "O preço é obrigatório.")
    @DecimalMin(value = "0.01", inclusive = true, message = "O preço deve ser maior que 0.")
    private BigDecimal price;

    private Boolean done;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
