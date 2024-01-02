package org.zerock.b01.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.b01.domain.Board;
import org.zerock.b01.dto.BoardListAllDTO;
import org.zerock.b01.dto.BoardListReplyCountDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class BoardReposotoryTests {

    @Autowired
    private BoardReposotory boardReposotory;

    @Autowired
    private  ReplyRepository replyRepository;


    @Test
    public void testInsert(){
        for(int i=0; i<=100; i++) {
            Board board = Board.builder()
                    .title("title.." +i)
                    .content("content..." +i)
                    .writer("user"+ (i%10))
                    .build();

           Board result = boardReposotory.save(board);
           log.info("BNO : " + result.getBno());
        }
    }

    @Test
    public void testSelect(){
        Long bno  = 100L;

//       Optional<Board> result = boardReposotory.findById(bno);
//       Board board = result.orElseThrow();

       Board board  = boardReposotory.findById(bno).orElseThrow();

       log.info(board);
    }


    @Test
    public void testUPdate(){
        Long bno  = 100L;

        Board board = boardReposotory.findById(bno).orElseThrow();
        log.info("board >> " + board);

        board.change("update title222....", "update content2222....");

        log.info("board2 >> " + board);
        boardReposotory.save(board);

        log.info("board3 >> " + board);
    }

    @Test
    public void testDelete(){
        boardReposotory.deleteById(100L);
    }

    @Test
    public void testGetList(){
        //boardReposotory.findAll().forEach(list-> log.info(list));

        List<Board> list = boardReposotory.findAll();
        for(Board b : list)
            log.info(b);
    }


    @Test
    public void testPaging(){
       Pageable pageable =  PageRequest.of(2,10, Sort.by("bno").descending());

       Page<Board> result =  boardReposotory.findAll(pageable);

       log.info(result.getTotalElements());
       log.info(result.getTotalPages());


       result.getContent().forEach(list -> log.info(list));
    }


    @Test
    public void testWriter(){
        boardReposotory.findByWriter("user1")
                .forEach(list-> log.info(list));
    }

    @Test
    public void testWriterAndTitle(){
        boardReposotory
                .findByWriterAndTitle("user1","title..1")
                .forEach(list-> log.info(list));
    }

    @Test
    public void testTitleLike(){
        boardReposotory
                .findByTitleLike("%1%")
                .forEach(list-> log.info(list));
    }


    @Test
    public void testWriter2(){
        boardReposotory.findByWriter2("user1")
                .forEach(list-> log.info(list));
    }

    @Test
    public void testTitle2(){
        boardReposotory.findByTitile2("2")
                .forEach(list-> log.info(list));
    }

    @Test
    public void testKeyword(){
        Pageable pageable = PageRequest.of(1, 10, Sort.by("bno").descending());

       Page<Board>  result = boardReposotory.findByKeyword("1",pageable);
       log.info(result.getTotalElements());
       log.info(result.getTotalPages());
       result.getContent().forEach(board -> log.info(board));

    }


    @Test
    public void  testSearch1(){
      Pageable pageable =  PageRequest.of(1,10, Sort.by("bno")
                .descending());

        boardReposotory.search1(pageable);
    }


    @Test
    public void testSearchAll(){
        String[] types = {"t", "c","w"};

        String keyword = "1";

        Pageable pageable=
        PageRequest.of(0,10,Sort.by("bno")
                .descending());

       Page<Board>  result =  boardReposotory.searchAll(types, keyword, pageable);

       log.info("---------------------------------");
       log.info(result.getTotalElements());
       log.info(result.getTotalPages());
       log.info(result.getSize());
       log.info(result.getNumber());
       log.info(result.hasPrevious());
       log.info(result.hasNext());
       log.info("---------------------------------");

    }



    @Test
    public void testSearchReplyCount(){

        String[] types = { "t", "c","w"};

        String keyword = "1";

        Pageable pageable =
                PageRequest.of(0,10,
                        Sort.by("bno").descending());

        Page<BoardListReplyCountDTO>  result =
                boardReposotory.searchWithReplyCount(types,keyword, pageable);

        log.info(result.getTotalElements());
        log.info(result.getTotalPages());
        log.info(result.getSize());
        log.info(result.getNumber());
        log.info("Next : " + result.hasNext());
        log.info("Prev : " + result.hasPrevious() );

        result.getContent().forEach(list-> log.info(list));

    }



    @Test
    public void testInertWithImages(){

        Board board = Board.builder()
                .title("Image test")
                .content("첨부파일 테스트")
                .writer("tester")
                .build();

        for (int i=0; i<3; i++) {
            board.addImage(UUID.randomUUID().toString(), "file" + i + ".jpg");
        }

        boardReposotory.save(board);
    }


   @Test
   public void testReadWithImage(){
       Board board = boardReposotory.findByIdWithImages(1L)
               .orElseThrow();

       log.info(board);
       log.info("--------------------");
       log.info(board.getImageSet());
   }

   @Transactional
   @Commit
   @Test
    public void testMidifyImages(){

       Board board = boardReposotory.findByIdWithImages(1L).orElseThrow();
       board.clearImages();

       for(int i=0; i<2; i++){
           board.addImage(UUID.randomUUID().toString(), "updateFile2" + i + ".jpg");
       }
       boardReposotory.save(board);

   }


   @Test
   @Transactional
   @Commit
   public void testRemoveAll(){

        Long bno = 1L;

        replyRepository.deleteByBoard_Bno(bno);

        boardReposotory.deleteById(bno);
   }


   @Test
    public void testInsertAll(){
        IntStream.rangeClosed(1,100).forEach(i->{

            Board board = Board.builder()
                    .title("Title.."+ i)
                    .content("Content.." + i)
                    .writer("writer.."+i)
                    .build();

            for(int j=0; j<3; j++){
                if((i % 5)==0){
                    continue;
                }
                board.addImage(UUID.randomUUID().toString(), i+ "file"+j + ".jpg");
            }
            boardReposotory.save(board);
        });
   }

   @Test
   @Transactional
   public void testSearchImageReplyCount(){
        Pageable pageable =  PageRequest.of(0,10, Sort.by("bno").descending());

        Page<BoardListAllDTO>  result = boardReposotory.searchWithAll(null, null, pageable);

        log.info("-----------------------------------------");
        log.info(result.getTotalElements());

        result.getContent().forEach(boardListAllDTO -> log.info(boardListAllDTO));
   }












}