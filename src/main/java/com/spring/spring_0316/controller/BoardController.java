package com.spring.spring_0316.controller;

import com.spring.spring_0316.domain.entity.BoardEntity;
import com.spring.spring_0316.domain.entity.FileEntity;
import com.spring.spring_0316.dto.BoardDto;
import com.spring.spring_0316.dto.FileDto;
import com.spring.spring_0316.service.BoardService;
import com.spring.spring_0316.service.FileService;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BoardController {
    private BoardService boardService;
    private FileService fileService;

    public BoardController(BoardService boardService, FileService fileService) {
        this.boardService = boardService;
        this.fileService = fileService;
    }

    @GetMapping("/introduce")
    public String introduce() {
        return "/introduce/list";
    }

    @GetMapping("/gallery")
    public String gallery() {
        return "/gallery/list";
    }

    /* 게시글 리스트 페이지 */
    /*@GetMapping("/board")
    public String list(Model model) {
        List<BoardDto> boardList = boardService.getBoardList();

        //model.addAttribute("boardList", boardList);
        model.addAttribute("boardList", boardService.findAllDesc(boardList));
        //model.addAttribute("boardList", boardService.pageList(pageable));
        return "board/list";
    }*/

    @GetMapping("/board/insertText")
    public String insertTest() {
        boardService.insertTest();

        return "/board/list";
    }
    /* 게시글 페이징 처리 */
    @GetMapping("/board")
    public String index(String type, String criteria, String keyword, String sort, Model model, @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        if(type==null) {
            type="0";
        }
        if(criteria==null)
        {
            criteria="1";
        }
        if(keyword==null) {
            keyword="";
        }
        if(sort==null) {
            sort="id,desc";
        }

        int typeNum = Integer.parseInt(type);
        int criteriaNum = Integer.parseInt(criteria);

        int size = pageable.getPageSize();
        int page = pageable.getPageNumber();

        int idx = sort.indexOf(",");
        String standard = sort.substring(0, idx);
        String ascDesc = sort.substring(idx+1);

        System.out.println(criteria);

        model.addAttribute("boardList", boardService.getBoardList(typeNum, criteriaNum, keyword, pageable));
        model.addAttribute("maxPage", 5);

        model.addAttribute("type", typeNum);
        model.addAttribute("criteria", criteriaNum);
        model.addAttribute("keyword", keyword);
        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", pageable.next().getPageNumber());
        model.addAttribute("hasPrevious", boardService.getListPreviousCheck(typeNum, criteriaNum, keyword, pageable));
        model.addAttribute("hasNext", boardService.getListNextCheck(typeNum, criteriaNum, keyword, pageable));
        model.addAttribute("size", size);
        model.addAttribute("page", page);
        model.addAttribute("standard", standard);
        model.addAttribute("ascDesc", ascDesc);

        return "/board/list";
    }

    /* 게시글 작성 페이지 */
    @GetMapping("/board/edit")
    public String write() {
        System.out.println("write");
        return "/board/write";
    }

    /* 게시글 작성 후 저장 */
    @PostMapping("/board/edit")
    //@ResponseStatus(HttpStatus.CREATED)
    //public String write(/*@RequestParam("files") List<MultipartFile> files, BoardDto boardDto*/) throws Exception {
    public String write(BoardDto boardDto, MultipartHttpServletRequest multiRequest) throws Exception {
        List<MultipartFile> fileList = multiRequest.getFiles("fileList");

        boardService.createBoard(boardDto, fileList);
        System.out.println("save Ok!!");

        return "redirect:/board";
    }

    /* 특정 게시글 페이지 */
    @GetMapping("/board/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        BoardDto boardDTO = boardService.getPost(id);

        int type = boardDTO.getType();
        if(type==3) {
            boardDTO = boardService.getPostSecure(id);
        }

        List<FileEntity> fileList = fileService.getBoardFile(id);
        if(fileList.isEmpty())
        {
            System.out.println("FileList is Empty");
        }
        else {
            System.out.println("FileList is NotEmpty");
            model.addAttribute("fileDto", fileList);
        }


        /*Long fileId = boardDTO.getFileId();
        if(fileId!=null) {
            FileDto fileDto = fileService.getFile(fileId);
            model.addAttribute("fileDto", fileDto);
        }*/

        model.addAttribute("boardDto", boardDTO);
        return "/board/detail";
    }

    /* 업로드한 파일 다운로드 */
    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> fileDownload(@PathVariable("fileId") Long fileId, @RequestHeader("User-Agent") String agent) throws IOException {
        FileDto fileDto = fileService.getFile(fileId);
        Path path = Paths.get(fileDto.getFilePath());
        String originalFilename = fileDto.getOriginalFilename();

        /* 브라우저별 한글파일명 처리 (@RequestHeader("User-Agent") String agent : 브라우저가 무엇인지 정보를 받음) */
        if(agent.contains("Trident"))
            originalFilename = URLEncoder.encode(originalFilename, "UTF-8").replaceAll("\\+", " ");
        else if(agent.contains("Edge"))
            originalFilename = URLEncoder.encode(originalFilename, "UTF-8");
        else
            originalFilename = new String(originalFilename.getBytes("UTF-8"), "ISO-8859-1");

        Resource resource = new InputStreamResource(Files.newInputStream(path));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + originalFilename + "\"")
                .body(resource);
    }

    /* 특정 게시글 수정 페이지 */
    @GetMapping("/board/edit/{id}")
    public String edit(@PathVariable("id") Long id,  Model model) {
        BoardDto boardDTO = boardService.getPostSecure(id);

        model.addAttribute("boardDto", boardDTO);
        return "/board/update";
    }

    /* 특정 게시글 수정 후 저장 */
    @PostMapping("/board/edit/{id}")
    public String update(BoardDto boardDTO) {
        boardService.updatePost(boardDTO);

        return "redirect:/board";
    }

    /* 특정 게시글 삭제 */
    @PostMapping("/board/{id}")
    public String delete(@PathVariable("id") Long id) {
        boardService.deletePost(id);

        return "redirect:/board";
    }

    /* 게시글 검색 */
    /*@GetMapping("/board/search")
    public String search(String keyword, Model model,  @PageableDefault(size = 3, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<BoardEntity> boardList = boardService.search(keyword, pageable);

        model.addAttribute("boardList", boardList);

        return "board/list";
    }*/

    /* 엑셀 업로드 팝업 */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/board/excel/upload")
    public String excelUpload(String status, Model model) {
        if(status==null) {
            status="0";
        }
        model.addAttribute("status", status);

        return "/board/excelUpload";
    }

    /* 엑셀 업로드 */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/board/excel/uploadFile")
    public String readExcel(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        List<BoardDto> excelData = new ArrayList<>();

        String url = "redirect:/board/excel/upload?status=";

        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        if(!extension.equals("xlsx")) {
            //throw new IOException("엑셀파일(.xlsx)만 업로드 해주세요");
            url = url + "2";
        } else {
            Workbook workbook = null;

            workbook = new XSSFWorkbook(file.getInputStream());

            Sheet worksheet = workbook.getSheetAt(0);

            for(int i=1;i<worksheet.getPhysicalNumberOfRows();i++) {
                Row row = worksheet.getRow(i);

                BoardDto board = new BoardDto();

                String typeStr = row.getCell(0).getStringCellValue();
                switch(typeStr) {
                    default:
                    case "일반글":
                        board.setType(1);
                        break;
                    case "공지글":
                        board.setType(2);
                        break;
                    case "비밀글":
                        board.setType(3);
                        break;
                    case "문의글":
                        board.setType(4);
                        break;
                }
                board.setTitle(row.getCell(1).getStringCellValue());
                board.setContent(row.getCell(2).getStringCellValue());
                board.setWriter(row.getCell(3).getStringCellValue());

                excelData.add(board);
            }

            for(BoardDto list : excelData) {
                boardService.savePost(list);
            }
            url = url + "1";
        }

        return url;
    }

    /* 엑셀 다운로드 */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/board/excel/download")
    public void excelDownload(String type, String criteria, String keyword, HttpServletResponse response, @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) throws IOException {
        if(type==null) {
            type="0";
        }
        if(criteria==null)
        {
            criteria="1";
        }
        if(keyword==null) {
            keyword="";
        }

        int typeNum = Integer.parseInt(type);
        int criteriaNum = Integer.parseInt(criteria);


        Page<BoardEntity> boardList = boardService.getBoardList(typeNum, criteriaNum, keyword, pageable);

        List<BoardEntity> list = boardList.getContent();
        LocalDateTime now = LocalDateTime.now();
        String fileName = "BOARD_" + now.format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH:mm:ss"));

        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("게시판 리스트");
        Row row = null;
        Cell cell = null;
        int rowNum = 0;

        // Header
        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellValue("번호");
        cell = row.createCell(1);
        cell.setCellValue("분류");
        cell = row.createCell(2);
        cell.setCellValue("제목");
        cell = row.createCell(3);
        cell.setCellValue("작성자");
        cell = row.createCell(4);
        cell.setCellValue("작성일");
        cell = row.createCell(5);
        cell.setCellValue("수정일");
        cell = row.createCell(6);
        cell.setCellValue("조회수");

        // Body
        for (BoardEntity board : list) {
            row = sheet.createRow(rowNum++);
            cell = row.createCell(0);
            cell.setCellValue(board.getId());
            cell = row.createCell(1);
            cell.setCellValue(board.getType());
            cell = row.createCell(2);
            cell.setCellValue(board.getTitle());
            cell = row.createCell(3);
            cell.setCellValue(board.getWriter());
            cell = row.createCell(4);
            cell.setCellValue(board.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            cell = row.createCell(5);
            cell.setCellValue(board.getModifiedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            cell = row.createCell(6);
            cell.setCellValue(board.getCount());
        }

        // 컨텐츠 타입과 파일명 지정
        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xlsx");

        // Excel File output
        wb.write(response.getOutputStream());
        wb.close();
    }
}
