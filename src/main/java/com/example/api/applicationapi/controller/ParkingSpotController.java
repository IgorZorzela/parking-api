package com.example.api.applicationapi.controller;

import com.example.api.applicationapi.dto.ParkingSpotDto;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/parking-spot")
public class ParkingSpotController {

    @Autowired
    ParkingSpotService parkingSpotService;

    @PostMapping
    public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotDto parkingSpotDto){
        //verifica se já está em uso pela placa
        if(parkingSpotService.existsByLicensePlateCar(parkingSpotDto.getLicensePlateCar())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: License Plate Car is already in use!");
        }
        //verifica se já está em uso pelo numero da vaga
        if(parkingSpotService.existsByParkingSpotNumber(parkingSpotDto.getParkingSpotNumber())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot is already in use!");
        }
        //verifica se já está em uso pelo apartamento e bloco
        if(parkingSpotService.existsByApartmentAndBlock(parkingSpotDto.getApartment(), parkingSpotDto.getBlock())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflito: vaga já está em uso pelo apartamento " +
                    parkingSpotDto.getApartment() + " do bloco " +
                    parkingSpotDto.getBlock());
        }
        var parkingSpotModel = new ParkingSpotModel();
        //converte o DTO em model
        BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
        //seta a data de registro
        parkingSpotModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
        return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotService.save(parkingSpotModel));
    }

    @GetMapping
    public ResponseEntity<List<ParkingSpotModel>> getAllParkingSpots(){
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneParkingSpot(@PathVariable(value = "id") UUID id){
        Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);
        if(!parkingSpotModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estacionamento não cadastrado");
        }

        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotModelOptional.get());

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteParkingSpot(@PathVariable(value = "id") UUID id){
        Optional<ParkingSpotModel> parkingSpotModeloOptional = parkingSpotService.findById(id);
        if(!parkingSpotModeloOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estacionamento não encontrado");
        }
        parkingSpotService.delete(parkingSpotModeloOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Estacionamento deletado com sucesso!");
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> updateParkingSpot(@PathVariable(value = "id") UUID id,
                                                    @RequestBody @Valid ParkingSpotDto parkingSpotDto){
        Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);
        if(!parkingSpotModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estacionamento não encontrado");
        }
        //assim
        var parkingSpotModel = new ParkingSpotModel();
        BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
        parkingSpotModel.setId(parkingSpotModelOptional.get().getId());
        parkingSpotModel.setRegistrationDate(parkingSpotModelOptional.get().getRegistrationDate());
        // OU ASSIM -------------------------------------------------------------------------------------
        // var parkingSpotModel = parkingSpotModelOptional.get();
        // parkingSpotModel.setParkingSpotNumber(parkingSpotDto.getParkingSpotNumber());
        // parkingSpotModel.setLicensePlateCar(parkingSpotDto.getLicensePlateCar());
        // parkingSpotModel.setModelCar(parkingSpotDto.getModelCar());
        // parkingSpotModel.setBrandCar(parkingSpotDto.getBrandCar());
        // parkingSpotModel.setColorCar(parkingSpotDto.getColorCar());
        // parkingSpotModel.setResponsibleName(parkingSpotDto.getResponsibleName());
        // parkingSpotModel.setApartment(parkingSpotDto.getApartment());
        // parkingSpotModel.setBlock(parkingSpotDto.getBlock());

        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.save(null));
    }

}
