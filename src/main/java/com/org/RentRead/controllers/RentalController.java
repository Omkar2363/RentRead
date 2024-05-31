package com.org.RentRead.controllers;

import com.org.RentRead.configurations.ValidateRequestConfig;
import com.org.RentRead.dtos.RentalDto;
import com.org.RentRead.entities.Rental;
import com.org.RentRead.exceptions.InvalidActionException;
import com.org.RentRead.services.Impl.RentalServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class RentalController {

    @Autowired
    private RentalServiceImpl rentalService;

    @Autowired
    private ValidateRequestConfig validateRequest;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/{bookId}/rent")
    public ResponseEntity<Rental> rentBook(@PathVariable Long bookId, @RequestBody RentalDto rentalDto) {
        validateRequest.isValidRequest(rentalDto);
        return new ResponseEntity<>(rentalService.rentBook(rentalDto.getUserId(), bookId), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/{bookId}/return")
    public ResponseEntity<Rental> returnBook(@PathVariable Long bookId, @RequestBody RentalDto rentalDto) {
        validateRequest.isValidRequest(rentalDto);
        return new ResponseEntity<>(rentalService.returnBook(rentalDto.getUserId(), bookId), HttpStatus.OK);
    }







}
