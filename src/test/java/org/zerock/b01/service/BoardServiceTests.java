package org.zerock.b01.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.b01.dto.BoardDTO;
import org.zerock.b01.dto.BoardListAllDTO;
import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    @Test
    public void testRegiter(){
        BoardDTO boardDTO =  BoardDTO.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .writer("테스트 저자")
                .build();

       Long bno = boardService.register(boardDTO);
       log.info("bno ==>  " + bno);
    }

    @Test
    public void testReadOne(){
       BoardDTO boardDTO = boardService.readOne(102L);

       log.info("boardDTO = > " + boardDTO);
    }

    @Test
    public void testModify() {

        //변경에 필요한 데이터
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(1L)
                .title("Updated....1")
                .content("Updated content 1...")
                .build();

        //첨부파일을 하나 추가
        boardDTO.setFileNames(Arrays.asList(UUID.randomUUID()+"_fff.jpg"));

        boardService.midify(boardDTO);

    }

    @Test
    public void testDelete(){
        boardService.remove(103L);
    }



    @Test
    public  void testList(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type("tcw")
                .keyword("1")
                .page(1)
                .size(10)
                .build();

        PageResponseDTO<BoardDTO> pageResponseDTO = boardService.list(pageRequestDTO);

        log.info(pageResponseDTO);
    }

    @Test
    public void testRegisterWithImages(){

        for(int i=0; i<100; i++) {
            BoardDTO boardDTO = BoardDTO.builder()
                    .title("File.....Sample Title" +i)
                    .content("Sample Content..." +i)
                    .writer("user"+i)
                    .build();

            boardDTO.setFileNames(
                    Arrays.asList(
                            UUID.randomUUID() + "_aaa"+i+".jpg",
                            UUID.randomUUID() + "_bbb"+i+".jpg",
                            UUID.randomUUID() + "_ccc"+i+".jpg"
                    )
            );
            Long bno = boardService.register(boardDTO);
        }

//        log.info("bno : " + bno);

    }


    @Test
    public void testReadAll(){
        BoardDTO boardDTO = boardService.readOne(101L);

        log.info(boardDTO);

        boardDTO.getFileNames().forEach(fileName->
                log.info(fileName)
        );
    }

    @Test
    public void testRemvoeAll(){

        boardService.remove(2L);
    }


    @Test
    public void testListWithAll(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build();

        PageResponseDTO<BoardListAllDTO> responseDTO = boardService.listWithAll(pageRequestDTO);

        List<BoardListAllDTO> dtoList = responseDTO.getDtoList();

        dtoList.forEach(boardListAllDTO -> {
            log.info(boardListAllDTO.getBno() + " : " + boardListAllDTO.getTitle() + " : " + boardListAllDTO.getReplyCount());

            if(boardListAllDTO.getBoardImages() != null){
                boardListAllDTO.getBoardImages().forEach(image -> log.info(image));
            }
            log.info("-------------------------------------------------------------------------");
        });
    }


}