package com.gym.dao.impl;

import com.gym.dao.ITrainingDao;
import com.gym.entity.Trainer;
import com.gym.entity.Training;
import com.gym.model.TrainingModel;
import com.gym.utils.Mapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.gym.utils.StorageUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository("trainingDao")
@Log4j2
public class TrainingDao implements ITrainingDao {
    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public TrainingDao(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public TrainingModel create(TrainingModel trainingModel) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        return trainingModel;
    }

    @Override
    public List<TrainingModel> getTrainerTrainingList(Map<String, Object> parameters) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        String queryString = "select t from Training t where t.trainer.user.userName = :trainer " +
                "and t.trainingDate between :startDate and :endDate and t.trainee.user.userName like :trainee";
        TypedQuery<Training> query = entityManager.createQuery(queryString, Training.class);
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        List<TrainingModel> resultList = query.getResultList().stream()
                .map(Mapper::mapTrainingToTrainingModel)
                .collect(Collectors.toList());
        entityManager.close();
        return resultList;
    }

    @Override
    public TrainingModel get(long id) {
        return null;
    }
}
