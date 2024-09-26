package com.example.meettify.service.meetBoard;

import com.example.meettify.dto.meetBoard.MeetBoardDetailsResponseDTO;

public interface MeetBoardService {

    MeetBoardDetailsResponseDTO getDetails(Long meetBoardId);
}
