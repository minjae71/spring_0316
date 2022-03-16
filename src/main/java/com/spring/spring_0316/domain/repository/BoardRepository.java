package com.spring.spring_0316.domain.repository;

import com.spring.spring_0316.domain.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    /* 게시글 전체 */
    //@Query("SELECT p FROM BoardEntity p ORDER BY p.id DESC")
    Page<BoardEntity> findAll(Pageable pageable);

    /* 게시글 제목 검색 */
    Page<BoardEntity> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    /* 게시글 내용 검색 */
    Page<BoardEntity> findByContentContainingIgnoreCase(String content, Pageable pageable);

    /* 게시글 제목+내용 검색 */
    Page<BoardEntity> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content, Pageable pageable);

    /* 게시글 작성자 검색 */
    Page<BoardEntity> findByWriterContainingIgnoreCase(String writer, Pageable pageable);

    /* 분류별 검색 */
    /* 게시글 전체 */
    //@Query("SELECT p FROM BoardEntity p ORDER BY p.id DESC")
    Page<BoardEntity> findByType(int type, Pageable pageable);

    /* 게시글 제목 검색 */
    Page<BoardEntity> findByTitleContainingIgnoreCaseAndType(String title, int type, Pageable pageable);

    /* 게시글 내용 검색 */
    Page<BoardEntity> findByContentContainingIgnoreCaseAndType(String content, int type, Pageable pageable);

    /* 게시글 제목+내용 검색 */
    Page<BoardEntity> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseAndType(String title, String content, int type, Pageable pageable);

    /* 게시글 작성자 검색 */
    Page<BoardEntity> findByWriterContainingIgnoreCaseAndType(String writer, int type, Pageable pageable);

    /* 조회수 증가 */
    @Modifying
    @Query("UPDATE BoardEntity p SET p.count = p.count + 1 WHERE p.id = :id")
    int updateCount(Long id);
}
