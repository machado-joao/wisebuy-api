package br.com.wisebuy.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.wisebuy.entity.ItemsList;

@Repository
public interface ItemsListRepository extends JpaRepository<ItemsList, Long> {

    List<ItemsList> findByUserId(Long userId);

    Optional<ItemsList> findByListIdAndUserId(Long listId, Long userId);

    @Query("SELECT l FROM ItemsList l LEFT JOIN FETCH l.items WHERE l.listId = :listId AND l.user.id = :userId")
    Optional<ItemsList> findByListIdAndUserIdWithItems(@Param("listId") Long listId, @Param("userId") Long userId);

}
