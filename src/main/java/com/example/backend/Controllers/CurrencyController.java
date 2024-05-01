package com.example.backend.Controllers;

import com.example.backend.entities.ChangeRate;
import com.example.backend.entities.Module;
import com.example.backend.entities.Monnaie;
import com.example.backend.services.CurrencyInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/currency")
public class CurrencyController {
    @Autowired
    CurrencyInterface currencyInterface;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Monnaie> getAllCurencies(){
        return currencyInterface.getAllCurencies();
    }

    @RequestMapping(value = "/changeRate", method = RequestMethod.GET)
    public List<ChangeRate> getAllChangeRates(){
        return currencyInterface.getAllChangeRate();
    }

    @RequestMapping(value = "/changeRate/{from}/{to}/{value}", method = RequestMethod.GET)
    public Float convert(@PathVariable Long from,@PathVariable Long to,@PathVariable Float value){
        // result = (value * taux_de_change_from )/taux_de_change_to
        Float taux_de_change_from = currencyInterface.findChangeRate(from).getTaux_change();
        Float taux_de_change_to = currencyInterface.findChangeRate(to).getTaux_change();

        return (value * taux_de_change_from )/taux_de_change_to;
    }
}
