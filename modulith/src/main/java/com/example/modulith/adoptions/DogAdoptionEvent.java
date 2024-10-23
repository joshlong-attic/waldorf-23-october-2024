package com.example.modulith.adoptions;


import org.springframework.modulith.events.Externalized;

@Externalized (target = "adoptions")
public record DogAdoptionEvent (int dogId) {
}
