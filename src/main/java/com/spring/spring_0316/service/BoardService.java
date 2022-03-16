package com.spring.spring_0316.service;

import com.spring.spring_0316.domain.entity.BoardEntity;
import com.spring.spring_0316.domain.entity.FileEntity;
import com.spring.spring_0316.domain.repository.BoardRepository;
import com.spring.spring_0316.domain.repository.FileRepository;
import com.spring.spring_0316.dto.BoardDto;
import com.spring.spring_0316.util.FileHandler;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@AllArgsConstructor
@Service
public class BoardService {
    private BoardRepository boardRepository;
    private FileHandler fileHandler;
    private FileRepository fileRepository;


    /* 게시글 리스트 */
    /*@Transactional
    public List<BoardDto> getBoardList() {
        List<BoardEntity> boardEntities = boardRepository.findAll();
        List<BoardDto> boardDtoList = new ArrayList<>();

        for(BoardEntity boardEntity : boardEntities) {
            BoardDto boardDTO = BoardDto.builder()
                    .id(boardEntity.getId())
                    .type(boardEntity.getType())
                    .title(boardEntity.getTitle())
                    .content(boardEntity.getContent())
                    .writer(boardEntity.getWriter())
                    .count(boardEntity.getCount())
                    .createdDate(boardEntity.getCreatedDate())
                    .build();

            boardDtoList.add(boardDTO);
        }

        return boardDtoList;
    }*/

    /* 하나의 게시글 가져오기 */
    @Transactional
    public BoardDto getPost(Long id) {
        Optional<BoardEntity> boardEntityWrapper = boardRepository.findById(id);
        BoardEntity boardEntity = boardEntityWrapper.get();

        BoardDto boardDTO = BoardDto.builder()
                .id(boardEntity.getId())
                .type(boardEntity.getType())
                .title(boardEntity.getTitle())
                .writer(boardEntity.getWriter())
                .content(boardEntity.getContent())
                .files(boardEntity.getFiles())
                .count(boardEntity.getCount())
                .createdDate(boardEntity.getCreatedDate())
                .modifiedDate(boardEntity.getModifiedDate())
                .build();

        return boardDTO;
    }

    /* 수정 시 하나의 게시글 가져오기 (작성자 or 관리자만 가능) */
    @PostAuthorize("isAuthenticated() and (( returnObject.writer.equals(principal.username)) or hasRole('ROLE_ADMIN'))")
    @Transactional
    public BoardDto getPostSecure(Long id) {

        Optional<BoardEntity> boardEntityWrapper = boardRepository.findById(id);
        BoardEntity boardEntity = boardEntityWrapper.get();

        BoardDto boardDTO = BoardDto.builder()
                .id(boardEntity.getId())
                .type(boardEntity.getType())
                .title(boardEntity.getTitle())
                .writer(boardEntity.getWriter())
                .content(boardEntity.getContent())
                .files(boardEntity.getFiles())
                .count(boardEntity.getCount())
                .createdDate(boardEntity.getCreatedDate())
                .modifiedDate(boardEntity.getModifiedDate())
                .build();

        return boardDTO;
    }

    /* 다중 첨부파일 생성 및 저장 */
    @Transactional
    public Long createBoard(BoardDto boardDto, List<MultipartFile> files) throws Exception {
        BoardEntity board = boardDto.toEntity();
        List<FileEntity> fileList = fileHandler.parseFileInfo(files);

        if(!fileList.isEmpty()) {
            for(FileEntity file : fileList) {
                board.addFile(file);
            }
        }

        return boardRepository.save(board).getId();
    }

    /* 게시글 저장 */
    @Transactional
    public Long savePost(BoardDto boardDto) {return boardRepository.save(boardDto.toEntity()).getId();
    }

    /* 게시글 수정 (작성자 or 관리자만 가능) */
    @Transactional
    @PreAuthorize("isAuthenticated() and (( #boardDto.writer.equals(principal.username)) or hasRole('ROLE_ADMIN'))")
    public Long updatePost(BoardDto boardDto) {
        return boardRepository.save(boardDto.toEntity()).getId();
    }

    @Transactional
    public void insertTest() {
        Random random = new Random();

        for(int i=0;i<100;i++) {
            int typeRandom = random.nextInt(4) + 1;
            int countRandom = random.nextInt(100);
            BoardEntity board = BoardEntity.builder()
                    .type(typeRandom)
                    .title("test" + i + 1)
                    .content("내용" + i + 11)
                    .writer("작성자" + i + 1)
                    .count(countRandom)
                    .build();
            boardRepository.save(board);
        }
    }

    /* 삭제 처리 */
    @Transactional
    public void deletePost(Long id) {
        boardRepository.deleteById(id);
    }


    /* 조회수 처리 */
    @Transactional
    public int updateCount(Long id) {
        return boardRepository.updateCount(id);
    }

    /* 게시글 정렬 */
    /*@Transactional
    public List<BoardEntity> findAllDesc(List<BoardDto> boardList) {
        return boardRepository.findAllDesc();
    }*/

    /* 게시글 검색 + 페이징 처리 */
    @Transactional
    public Page<BoardEntity> getBoardList(int type, int criteria, String keyword, Pageable pageable) {
        Page<BoardEntity> boardList;
        if(type==0) {
            switch(criteria) {
                case 1:
                    boardList = boardRepository.findByTitleContainingIgnoreCase(keyword, pageable);
                    break;
                case 2:
                    boardList = boardRepository.findByContentContainingIgnoreCase(keyword, pageable);
                    break;
                case 3:
                    boardList = boardRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword, pageable);
                    break;
                case 4:
                    boardList = boardRepository.findByWriterContainingIgnoreCase(keyword, pageable);
                    break;
                default:
                    boardList = boardRepository.findAll(pageable);
                    break;
            }
        } else {
            switch(criteria) {
                case 1:
                    boardList = boardRepository.findByTitleContainingIgnoreCaseAndType(keyword, type, pageable);
                    break;
                case 2:
                    boardList = boardRepository.findByContentContainingIgnoreCaseAndType(keyword, type, pageable);
                    break;
                case 3:
                    boardList = boardRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseAndType(keyword, keyword, type, pageable);
                    break;
                case 4:
                    boardList = boardRepository.findByWriterContainingIgnoreCaseAndType(keyword, type, pageable);
                    break;
                default:
                    boardList = boardRepository.findByType(type, pageable);
                    break;
            }
        }
        return boardList;
    }

    /* 페이징 처리 */
    @Transactional
    public Page<BoardEntity> getBoardListAll(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    /* 페이징 - 이전 버튼 비활성화 처리 */
    @Transactional
    public Boolean getListPreviousCheck(int type, int criteria, String keyword, Pageable pageable) {
        Page<BoardEntity> saved = getBoardList(type, criteria, keyword, pageable);
        Boolean hasPrevious = saved.hasPrevious();

        return hasPrevious;
    }

    /* 페이징 - 다음 버튼 비활성화 처리 */
    @Transactional
    public Boolean getListNextCheck(int type, int criteria, String keyword, Pageable pageable) {
        Page<BoardEntity> saved = getBoardList(type, criteria, keyword, pageable);
        Boolean hasNext = saved.hasNext();

        return hasNext;
    }
}
