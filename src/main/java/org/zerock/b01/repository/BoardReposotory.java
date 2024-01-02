package org.zerock.b01.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.b01.domain.Board;
import org.zerock.b01.repository.search.BoardSearch;

import java.util.List;
import java.util.Optional;

public interface BoardReposotory extends JpaRepository<Board, Long> , BoardSearch {

    @EntityGraph(attributePaths = {"imageSet"})
    @Query("select b from Board b where b.bno = :bno")
    Optional<Board> findByIdWithImages(@Param("bno") Long bno);


    List<Board> findByWriter(String writer);

    List<Board> findByWriterAndTitle(String writer, String title);

    List<Board> findByTitleLike(String writer);

    @Query("select i from Board i  where i.writer = :writer")
    List<Board> findByWriter2(@Param("writer") String writer);

    @Query("select b from Board b where b.title like %:title% order by b.bno desc")
    List<Board> findByTitile(@Param("title") String title);

    @Query(value = "select * from board where title like %:title% order by bno desc", nativeQuery = true)
    List<Board> findByTitile2(@Param("title") String title);

    @Query("select b from Board b where b.title like concat('%', :keyword, '%')")
    Page<Board> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
