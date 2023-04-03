package com.example.api.applicationapi.service;

import com.example.api.applicationapi.model.ParkingSpotModel;
import com.example.api.applicationapi.repository.ParkingSpotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class ParkingSpotService {
//controller solicita o service
    //service solicita o repository
    //ponto de injeção
    @Autowired
    ParkingSpotRepository parkingSpotRepository;
//ou-----------------------------------------------------------------------
//    final ParkingSpotRepository parkingSpotRepository;
//
//    public ParkingSpotService(ParkingSpotRepository parkingSpotRepository){
//        this.parkingSpotRepository = parkingSpotRepository;
//    }
    @Transactional
    public ParkingSpotModel save(ParkingSpotModel parkingSpotModel){
        return parkingSpotRepository.save(parkingSpotModel);
    }

}
