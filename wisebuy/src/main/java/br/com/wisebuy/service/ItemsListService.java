package br.com.wisebuy.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.wisebuy.dto.ItemDTO;
import br.com.wisebuy.dto.ItemsListDTO;
import br.com.wisebuy.entity.Item;
import br.com.wisebuy.entity.ItemsList;
import br.com.wisebuy.entity.User;
import br.com.wisebuy.repository.ItemRepository;
import br.com.wisebuy.repository.ItemsListRepository;
import br.com.wisebuy.repository.UserRepository;

@Service
public class ItemsListService {

    private final ItemsListRepository itemsListRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final MapperService mapperService;

    public ItemsListService(ItemsListRepository itemsListRepository, ItemRepository itemRepository,
            UserRepository userRepository,
            JwtService jwtService,
            MapperService mapperService) {
        this.itemsListRepository = itemsListRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.mapperService = mapperService;
    }

    public ResponseEntity<List<ItemsListDTO>> getLists(String authHeader) {

        Long userId = extractUserId(authHeader);

        List<ItemsList> lists = itemsListRepository.findByUserId(userId);
        List<ItemsListDTO> dtos = mapperService.mapItemsListToDTOs(lists);

        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }

    public ResponseEntity<ItemsListDTO> getList(Long listId, String authHeader) {

        Long userId = extractUserId(authHeader);

        ItemsList list = itemsListRepository.findByListIdAndUserIdWithItems(listId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Lista não encontrada."));
        ItemsListDTO dto = mapperService.mapItemsListToDTOs(list);

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    public ResponseEntity<ItemsListDTO> addList(ItemsListDTO dto, String authHeader) {

        Long userId = extractUserId(authHeader);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        ItemsList list = new ItemsList();
        list.setName(dto.getName());
        list.setDescription(dto.getDescription());
        list.setUser(user);

        ItemsList newList = itemsListRepository.save(list);
        ItemsListDTO newDTO = mapperService.mapItemsListToDTOs(newList);

        return ResponseEntity.status(HttpStatus.CREATED).body(newDTO);
    }

    public ResponseEntity<Void> removeList(Long listId, String authHeader) {

        Long userId = extractUserId(authHeader);

        Optional<ItemsList> optionalList = itemsListRepository.findByListIdAndUserId(listId, userId);

        if (!optionalList.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        itemsListRepository.delete(optionalList.get());
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<ItemDTO> addItem(ItemDTO dto, String authHeader) {

        Long userId = extractUserId(authHeader);

        ItemsList list = itemsListRepository.findByListIdAndUserId(dto.getListId(), userId)
                .orElseThrow(() -> new IllegalArgumentException("Lista não encontrada."));

        Item item = new Item();
        item.setName(dto.getName());
        item.setQuantity(dto.getQuantity());
        item.setPrice(dto.getPrice());
        item.setList(list);

        Item newItem = itemRepository.save(item);
        ItemDTO newDTO = mapperService.mapItemToDTO(newItem);

        return ResponseEntity.status(HttpStatus.CREATED).body(newDTO);
    }

    public ResponseEntity<Void> removeItem(Long itemId, String authHeader) {

        Long userId = extractUserId(authHeader);

        Optional<Item> optionalItem = itemRepository.findById(itemId);

        if (!optionalItem.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Optional<ItemsList> optionalItemsList = itemsListRepository
                .findByListIdAndUserId(optionalItem.get().getList().getListId(), userId);

        if (!optionalItemsList.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        itemRepository.delete(optionalItem.get());
        return ResponseEntity.noContent().build();
    }

    private Long extractUserId(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return jwtService.extractUserId(token);
    }

}
