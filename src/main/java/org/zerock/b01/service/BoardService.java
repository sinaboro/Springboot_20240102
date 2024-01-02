package org.zerock.b01.service;


import org.zerock.b01.domain.Board;
import org.zerock.b01.dto.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public interface BoardService {

    Long register(BoardDTO boardDTO);

    BoardDTO readOne(Long bno);

    void midify(BoardDTO boardDTO);

    void remove(Long bno);

    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);

    PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);

    //게시글의 이미지와 댓글 숫자까지 처리
    PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO);


    default  Board dtoToEntity(BoardDTO boardDTO){
        Board board = Board.builder()
                .bno(boardDTO.getBno())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .writer(boardDTO.getWriter())
                .build();

        //uuid_originalFileName
        if(boardDTO.getFileNames() != null){
            boardDTO.getFileNames().forEach(fileName -> {

                String[] arr = fileName.split("_" , 2);
                //0fcf646a-95b0-4fea-82f1-a7ed32ede70c_event06_02.PNG
                //이런 문자열이 기입이 되면 첫번째_에서만 문자열을 분리함.


//                System.out.println("---------------------------------dtoToEntity-");
//
//                Arrays.stream(arr).forEach(list-> System.out.println(list));
//
//                System.out.println("fileName : " + fileName);
//                System.out.println("arr[0] : " + arr[0]);
//                System.out.println("arr[1] : " +arr[1]);
//                System.out.println("----------------------------------dtoToEntity");
                board.addImage(arr[0], arr[1]);  //arr[0] : uuid, arr[1] : original filename
            });
        }

        return  board;
    }

    default  BoardDTO entityToDTO(Board board){

       BoardDTO boardDTO   =   BoardDTO.builder()
                    .bno(board.getBno())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .writer(board.getWriter())
                    .regDate(board.getRegDate())
                    .modDate(board.getModDate())
                    .build();

        List<String> fileNames = board.getImageSet().stream().map(boardImage ->
                boardImage.getUuid()+"_"+boardImage.getFileName()).collect(Collectors.toList());

        boardDTO.setFileNames(fileNames);

        return boardDTO;
    }





}
