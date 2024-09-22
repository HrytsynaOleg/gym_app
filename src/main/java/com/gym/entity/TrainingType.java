package com.gym.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "training_type")
public class TrainingType {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "trainingType", cascade = CascadeType.ALL)
    private List<Trainer> trainerList = new java.util.ArrayList<>();

}
