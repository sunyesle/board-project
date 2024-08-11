package com.sunyesle.board_project.board;

import com.sunyesle.board_project.board.dto.BoardResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query(value = "select new com.sunyesle.board_project.board.dto.BoardResponse(b.id, b.title, b.content, b.createdAt, m.id, m.name) " +
            "from Board b join Member m on m.id = b.memberId " +
            "where (:title is null or b.title like concat('%', :title, '%'))" +
            "and b.deletedAt is null",
            countQuery = "select count(*) " +
                    "from Board b " +
                    "where (:title is null or b.title like concat('%', :title, '%'))" +
                    "and b.deletedAt is null"
    )
    Page<BoardResponse> findByTitleContains(String title, Pageable pageable);

    Optional<Board> findByIdAndDeletedAtIsNull(Long id);
}
