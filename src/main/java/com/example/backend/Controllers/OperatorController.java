package com.example.backend.Controllers;

import com.example.backend.entities.InterDest;
import com.example.backend.entities.OperateurInterco;
import com.example.backend.entities.operator_ref_id;
import com.example.backend.services.InterDestService;
import com.example.backend.services.OperateurIntercoService;
import com.example.backend.services.OperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/operators")
public class OperatorController {

    @Autowired
    OperatorService operatorService;

    @Autowired
    InterDestService interDestService;

    @Autowired
    OperateurIntercoService operateurIntercoService;

    @RequestMapping(value = "/")
    public List<operator_ref_id> getOperators(){
        return operatorService.getListOperator();
    }

    @RequestMapping(value = "/dest")
    public List<InterDest> getInterDest(){
        return interDestService.getAllDest();
    }

    @RequestMapping(value="/dest/{id}",method= RequestMethod.GET)
    public InterDest getInterDestByIdPays(@PathVariable int id){
       return interDestService.getInterDestByPays(id);
    }

    @RequestMapping(value="/interco/{id}",method= RequestMethod.GET)
    public OperateurInterco getInteroDestByIdPays(@PathVariable int id){
        return operateurIntercoService.getInterDestByPays(id);
    }

    @RequestMapping(value="/interco",method= RequestMethod.GET)
    public List<OperateurInterco> getAllIntercoOper(){
        return operateurIntercoService.getAll();
    }
}
