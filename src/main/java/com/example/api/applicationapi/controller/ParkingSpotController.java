package com.example.api.applicationapi.controller;

import com.example.api.applicationapi.dto.ParkingSpotDTO;
import com.example.api.applicationapi.model.ParkingSpotModel;
import com.example.api.applicationapi.service.ParkingSpotService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/parking-spot")
public class ParkingSpotController {

    @Autowired
    ParkingSpotService parkingSpotService;

    @PostMapping
    public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotDTO parkingSpotDTO){
        //verifica se já está em uso pela placa
        if(parkingSpotService.existsByLicencePlateCar(parkingSpotDTO.getLicensePlateCar())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflito: Vaga já possui um placa cadastrada na mesma!")
        }
        //verifica se já está em uso pelo numero da vaga
        if(parkingSpotService.existsBySpotNumber(parkingSpotDTO.getParkSpotNumber())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflito: Número de vaga já está em uso!");
        }
        //verifica se já está em uso pelo apartamento e bloco
        if(parkingSpotService.existisByApartmentoAndBlock(parkingSpotDTO.getApartment(), parkingSpotDTO.getBlock())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflito: vaga já está em uso pelo apartamento " +
                    parkingSpotDTO.getApartment() + " do bloco " +
                    parkingSpotDTO.getBlock());
        }

        var parkingSpotModel = new ParkingSpotModel();
        //converte o DTO em model
        BeanUtils.copyProperties(parkingSpotDTO, parkingSpotModel);
        //seta a data de registro
        parkingSpotModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
        return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotService.save(parkingSpotModel));
    }
}
