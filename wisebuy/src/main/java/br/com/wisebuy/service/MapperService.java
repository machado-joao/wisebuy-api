package br.com.wisebuy.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.wisebuy.dto.ItemDTO;
import br.com.wisebuy.dto.ItemsListDTO;
import br.com.wisebuy.entity.Item;
import br.com.wisebuy.entity.ItemsList;

@Service
public class MapperService {

        public List<ItemsListDTO> mapItemsListToDTOs(List<ItemsList> lists) {
                return lists.stream()
                                .map(this::mapItemsListToDTOs)
                                .collect(Collectors.toList());
        }

        public ItemsListDTO mapItemsListToDTOs(ItemsList list) {
                return ItemsListDTO.builder().listId(list.getListId()).name(list.getName())
                                .description(list.getDescription())
                                .items(list.getItems() != null
                                                ? list.getItems().stream()
                                                                .map(this::mapItemToDTO)
                                                                .collect(Collectors.toList())
                                                : List.of())
                                .createdAt(list.getCreatedAt()).updatedAt(list.getUpdatedAt())
                                .build();
        }

        public ItemDTO mapItemToDTO(Item item) {
                return ItemDTO.builder().itemId(item.getItemId()).listId(item.getList().getListId())
                                .name(item.getName())
                                .quantity(item.getQuantity()).price(item.getPrice()).createdAt(item.getCreatedAt())
                                .updatedAt(item.getUpdatedAt()).build();
        }

}
