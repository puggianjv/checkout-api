package br.com.fiap.controller;

import br.com.fiap.dto.CheckoutDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/checkout")
public class CheckoutController {

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody CheckoutDTO checkoutDTO) {
        try {
            //TODO fazer checkout
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

}
