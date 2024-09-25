package com.gym.dao.impl;

import com.gym.dao.ITraineeDao;
import com.gym.entity.Trainee;
import com.gym.entity.Trainer;
import com.gym.entity.TrainerTrainee;
import com.gym.entity.User;
import com.gym.model.TraineeModel;
import com.gym.model.TrainerModel;
import com.gym.utils.Mapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@Repository("traineeDao")
@Log4j2
public class TraineeDao implements ITraineeDao {
    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public TraineeDao(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public TraineeModel create(TraineeModel traineeModel) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Trainee trainee = Mapper.mapTraineeModelToTraineeEntity(traineeModel);
        User user = trainee.getUser();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(user);
            trainee.setUser(user);
            entityManager.persist(trainee);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            log.error("Dao error occurred - create trainee ");
            return null;
        } finally {
            entityManager.close();
        }
        entityManager.close();
        TraineeModel newTraineeModel = get(trainee.getId());
        log.info(String.format("Trainee id = %s created in database", trainee.getId()));
        return newTraineeModel;
    }

    @Override
    public TraineeModel getByUserName(String userName) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        String queryString = "select t from Trainee t where t.user.userName like ?1";
        TypedQuery<Trainee> query = entityManager.createQuery(queryString, Trainee.class);
        query.setParameter(1, userName);
        List<Trainee> resultList;
        try {
            resultList = query.getResultList();
        } catch (Exception e) {
            log.error("Dao error occurred - get trainee by name");
            return null;
        } finally {
            entityManager.close();
        }
        if (resultList.size() != 1) {
            return null;
        }
        return Mapper.mapTraineeEntityToTraineeModel(resultList.get(0));
    }

    @Override
    public void update(TraineeModel traineeModel) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Trainee trainee = Mapper.mapTraineeModelToTraineeEntity(traineeModel);
        User user = trainee.getUser();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(user);
            entityManager.merge(trainee);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            log.error("Dao error occurred - update trainee ");
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void delete(long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Trainee trainee = entityManager.find(Trainee.class, id);
            entityManager.remove(trainee);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            log.error("Dao error occurred - delete trainee");
        } finally {
            entityManager.close();
        }
    }

    @Override
    public TraineeModel get(long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Trainee trainee;
        try {
            trainee = entityManager.find(Trainee.class, id);
        } catch (Exception e) {
            log.error("Dao error occurred - get trainee");
            return null;
        } finally {
            entityManager.close();
        }
        if (trainee == null) {
            return null;
        }
        return Mapper.mapTraineeEntityToTraineeModel(trainee);
    }

    @Override
    public void intendTrainer(TraineeModel traineeModel, TrainerModel trainerModel) {
        Trainer trainer = Mapper.mapTrainerModelToTrainerEntity(trainerModel);
        Trainee trainee = Mapper.mapTraineeModelToTraineeEntity(traineeModel);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            TrainerTrainee newTrainerTrainee = new TrainerTrainee();
            newTrainerTrainee.setTrainer(trainer);
            newTrainerTrainee.setTrainee(trainee);
            entityManager.getTransaction().begin();
            entityManager.persist(newTrainerTrainee);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            log.error("Dao error occurred - intend trainer");
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void deleteTrainer(TraineeModel traineeModel, TrainerModel trainerModel) {
        Trainer trainer = Mapper.mapTrainerModelToTrainerEntity(trainerModel);
        Trainee trainee = Mapper.mapTraineeModelToTraineeEntity(traineeModel);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        String queryString = "select t from TrainerTrainee t where t.trainee.id like ?1 and " +
                "t.trainer.id like ?2";
        TypedQuery<TrainerTrainee> query = entityManager.createQuery(queryString, TrainerTrainee.class);
        query.setParameter(1, trainee.getId());
        query.setParameter(2, trainer.getId());
        List<TrainerTrainee> trainerTraineeList = query.getResultList();
        try {
            trainerTraineeList = query.getResultList();
            if (trainerTraineeList.size() == 1) {
                entityManager.getTransaction().begin();
                entityManager.remove(trainerTraineeList.get(0));
                entityManager.getTransaction().commit();
            }
            else {
                throw new Exception();
            }
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            log.error("Dao error occurred - delete trainer");
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<TrainerModel> getIntendedTrainerList(TraineeModel traineeModel) {
        Trainee trainee = Mapper.mapTraineeModelToTraineeEntity(traineeModel);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<TrainerModel> trainerModelList;
        try {
            String queryString = "select t from TrainerTrainee t where t.trainee.id like ?1";
            TypedQuery<TrainerTrainee> query = entityManager.createQuery(queryString, TrainerTrainee.class);
            query.setParameter(1, trainee.getId());
            List<TrainerTrainee> trainerTraineeList = query.getResultList();
            trainerModelList = trainerTraineeList.stream()
                    .map(TrainerTrainee::getTrainer)
                    .map(Mapper::mapTrainerEntityToTrainerModel)
                    .toList();
        } catch (Exception e) {
            log.error("Dao error occurred - get trainer list");
            return new ArrayList<>();
        } finally {
            entityManager.close();
        }
        return trainerModelList;
    }
}

