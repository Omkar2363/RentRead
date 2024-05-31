package com.org.RentRead.configurations;

import com.org.RentRead.dtos.RentalDto;
import com.org.RentRead.exceptions.InvalidActionException;
import com.org.RentRead.services.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class ValidateRequestConfig {
    @Autowired
    private RentalService rentalService;

    //Checking the request to rent and return raised from the logged in account or not :
    public void isValidRequest(RentalDto rentalDto){
        Long authenticatedUserId = getAuthenticatedUserId();
        if (!authenticatedUserId.equals(rentalDto.getUserId())) {
            throw new InvalidActionException("Given userId is different from current Login account");
        }
    }
    private Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        return rentalService.findUserIdByEmail(userEmail);
    }
}
