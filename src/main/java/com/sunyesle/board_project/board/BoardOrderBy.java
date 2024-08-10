package com.sunyesle.board_project.board;

import org.springframework.data.domain.Sort;

public enum BoardOrderBy {
    LATEST(Sort.by("createdAt").ascending()),
    OLDEST(Sort.by("createdAt").descending());

    private final Sort sort;

    BoardOrderBy(Sort sort) {
        this.sort = sort;
    }

    public Sort getSort() {
        return sort;
    }
}

