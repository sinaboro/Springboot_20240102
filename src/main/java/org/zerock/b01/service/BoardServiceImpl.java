package org.zerock.b01.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.b01.domain.Board;
import org.zerock.b01.dto.*;
import org.zerock.b01.repository.BoardReposotory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements  BoardService{

    private final BoardReposotory boardReposotory;

    private final ModelMapper modelMapper;

    @Override
    public Long register(BoardDTO boardDTO) {

//       Board board = modelMapper.map(boardDTO, Board.class);

       Board board = dtoToEntity(boardDTO);

       Long bno = boardReposotory.save(board).getBno();

        return bno;
    }

    @Override
    public BoardDTO readOne(Long bno) {

        //board_image까지 조인 처리되는 findByWithImages()를 이용

        Board board = boardReposotory.findByIdWithImages(bno).orElseThrow();

//        BoardDTO boardDTO = modelMapper.map(board, BoardDTO.class);

        return entityToDTO(board);
    }

    @Override
    public void midify(BoardDTO boardDTO) {


        log.info("modify......................");
//        Optional<Board> result = boardReposotory.findByIdWithImages(boardDTO.getBno());
        Optional<Board> result = boardReposotory.findById(boardDTO.getBno());

        log.info("result => " + result);

        Board board = result.orElseThrow();

        log.info("board => " + board);

        board.change(boardDTO.getTitle(), boardDTO.getContent());

        board.clearImages();

        if(boardDTO.getFileNames() != null){
            for(String fileName : boardDTO.getFileNames()){
                String[] arr = fileName.split("_");
                board.addImage(arr[0],arr[1]);
            }
        }

        boardReposotory.save(board);
    }

    @Override
    public void remove(Long bno) {

        boardReposotory.deleteById(bno);
    }

    @Override
    public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {

        String[] types = pageRequestDTO.getTypes();  //tcw   ==> t c w
        String keyword = pageRequestDTO.getKeyword();

        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<Board> result = boardReposotory.searchAll(types, keyword, pageable);

//        result.getTotalElements();
//        result.getTotalPages();
//        result.getContent().forEach(board -> log.info(board));

        List<BoardDTO> dtoList = result.getContent().stream().
                map(board -> modelMapper.map(board, BoardDTO.class))
                .collect(Collectors.toList());

        return PageResponseDTO.<BoardDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getTypes();  //tcw   ==> t c w
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");


        Page<BoardListReplyCountDTO> result = boardReposotory.searchWithReplyCount(types, keyword, pageable);

        return PageResponseDTO.<BoardListReplyCountDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO) {

        String[] types = pageRequestDTO.getTypes();  //tcw   ==> t c w
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<BoardListAllDTO> result = boardReposotory.searchWithAll(types, keyword, pageable);

        return PageResponseDTO.<BoardListAllDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
    }




}
