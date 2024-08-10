package com.sunyesle.board_project.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query(value = "select new com.sunyesle.board_project.board.BoardResponse(b.id, b.title, b.content, b.createdAt, m.id, m.name) " +
            "from Board b join Member m on m.id = b.memberId " +
            "where (:title is null or b.title like concat('%', :title, '%'))",
            countQuery = "select count(*) " +
                    "from Board b " +
                    "where (:title is null or b.title like concat('%', :title, '%'))"
    )
    Page<BoardResponse> findByTitleContains(String title, Pageable pageable);
}
