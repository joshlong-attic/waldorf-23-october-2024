package com.example.modulith.adoptions;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.annotation.Id;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Transactional
@Controller
@ResponseBody
class DogAdoptionController {

    private final ApplicationEventPublisher publisher;
    
    private final DogRepository repository;

    DogAdoptionController(ApplicationEventPublisher publisher, DogRepository repository) {
        this.publisher = publisher;
        this.repository = repository;
    }

    @PostMapping("/dogs/{dogId}/adoptions")
    void adopt(@PathVariable int dogId, @RequestBody Map<String, String> owner) {
        this.repository.findById(dogId).ifPresent(dog -> {
            var newDog = this.repository.save(new Dog(dog.id(),
                    dog.name(), owner.get("name"), dog.description()));
            System.out.println("adopted " + newDog + "!");
            this.publisher.publishEvent(new DogAdoptionEvent(newDog.id()));
        });
    }
}

interface DogRepository extends ListCrudRepository<Dog, Integer> {
}

record Dog(@Id int id, String name, String owner, String description) {
}