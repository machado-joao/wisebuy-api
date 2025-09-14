package br.com.wisebuy.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.wisebuy.dto.ItemDTO;
import br.com.wisebuy.dto.ItemsListDTO;
import br.com.wisebuy.service.ItemsListService;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/list")
public class ItemsListController {

    private final ItemsListService itemsListService;

    public ItemsListController(ItemsListService itemsListService) {
        this.itemsListService = itemsListService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ItemsListDTO>> getLists(@RequestHeader("Authorization") String authHeader) {
        return itemsListService.getLists(authHeader);
    }

    @GetMapping("/{listId}")
    public ResponseEntity<ItemsListDTO> getList(@PathVariable Long listId,
            @RequestHeader("Authorization") String authHeader) {
        return itemsListService.getList(listId, authHeader);
    }

    @PostMapping("/addList")
    public ResponseEntity<ItemsListDTO> addList(@Valid @RequestBody ItemsListDTO dto,
            @RequestHeader("Authorization") String authHeader) {
        return itemsListService.addList(dto, authHeader);
    }

    @DeleteMapping("/removeList/{listId}")
    public ResponseEntity<Void> removeList(@PathVariable Long listId,
            @RequestHeader("Authorization") String authHeader) {
        return itemsListService.removeList(listId, authHeader);
    }

    @PostMapping("/addItem")
    public ResponseEntity<ItemDTO> addItem(@Valid @RequestBody ItemDTO dto,
            @RequestHeader("Authorization") String authHeader) {
        return itemsListService.addItem(dto, authHeader);
    }

    @PostMapping("/toggleItem/{itemId}")
    public ResponseEntity<ItemDTO> toggleItem(@PathVariable Long itemId,
            @RequestHeader("Authorization") String authHeader) {
        return itemsListService.toggleItem(itemId, authHeader);
    }

    @DeleteMapping("/removeItem/{itemId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long itemId,
            @RequestHeader("Authorization") String authHeader) {
        return itemsListService.removeItem(itemId, authHeader);
    }

}
