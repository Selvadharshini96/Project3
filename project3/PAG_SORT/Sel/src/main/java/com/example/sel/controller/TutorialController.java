package com.example.anu.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.anu.model.TutorialModel;
import com.example.anu.repository.TutorialRepository;

@RestController
@RequestMapping("/api")
public class TutorialController {

    @Autowired
    TutorialRepository tutorialRepository;

   private Sort.Direction getSortDirection(String direction){
        if(direction.equals("asc")){
            return Sort.Direction.ASC;
        }
        else if(direction.equals("desc")){
            return Sort.Direction.DESC;
        }
        return Sort.Direction.ASC;
    }
    @GetMapping("/paginationandsorting")
    public ResponseEntity<Map<String, Object>> getAlltutorials(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id,asc") String[] sort){
            List<Order> order = new ArrayList<Order>();
            if(sort[0].contains(",")){
                for(String sortOrder : sort){
                    String[] _sort=sortOrder.split(",");
                    order.add(new Order(getSortDirection(_sort[1]),_sort[0]));
                }
            }
            else{
                order.add(new Order(getSortDirection(sort[1]),sort[0]));
            }
            List<TutorialModel> tutorials = new ArrayList<TutorialModel>();
            Pageable pagingSort = PageRequest.of(page,size,Sort.by(order));

            Page<TutorialModel> pageTuts;
            pageTuts = tutorialRepository.findAll(pagingSort);

            tutorials = pageTuts.getContent();
           

            Map<String, Object> response = new HashMap<>();
            response.put("tutorials", tutorials);
            response.put("currentPage", pageTuts.getNumber());
            response.put("pageSize", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());

            return new ResponseEntity<>(response,HttpStatus.OK);
        }
    }