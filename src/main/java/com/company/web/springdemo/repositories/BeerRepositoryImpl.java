package com.company.web.springdemo.repositories;

import com.company.web.springdemo.exceptions.EntityNotFoundException;
import com.company.web.springdemo.models.Beer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BeerRepositoryImpl implements BeerRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public BeerRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public List<Beer> get(String name, Double minAbv, Double maxAbv, Integer styleId, String sortBy, String sortOrder) {
        try (
                Session session = sessionFactory.openSession();
        ) {
            String query = "SELECT * FROM beers " +
                    "join styles on styles.style_id = beers.beer_style " +
                    "join users on users.user_id = beers.beer_creator  " +
                    "WHERE 1 + 1";
            if (name != null) {
                query += " AND beers.name = :name";
            }
            if (minAbv != null) {
                query += " AND beers.abv >= :abvMin";
            }
            if (maxAbv != null) {
                query += " AND beers.abv <= :abvMax";
            }
            if (styleId != null) {
                query += " AND beers.style_id = styleId";
            }
            if (sortBy != null && sortOrder != null) {
                query += " ORDER BY " + sortBy + " " + sortOrder;
            }
            Query<Beer> query1 = session.createNativeQuery(query, Beer.class);


            if (name != null) {
                query1.setParameter("name", name);
            }
            if (minAbv != null) {
                query1.setParameter("abvMin", minAbv);
            }
            if (maxAbv != null) {
                query1.setParameter("abvMax", maxAbv);
            }
            if (styleId != null) {
                query1.setParameter("styleId", styleId);
            }
            return query1.getResultList();

        }
    }

    @Override
    public List<Beer> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Beer> query = session.createQuery("from Beer", Beer.class);
            return query.list();
        }
    }

    @Override
    public Beer get(int id) {
        try (Session session = sessionFactory.openSession()) {
            Beer beer = session.get(Beer.class, id);
            if (beer == null) {
                throw new EntityNotFoundException("Beer", id);
            }
            return beer;
        }
    }

    public Beer get(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<Beer> query = session.createQuery("from Beer where name = :name", Beer.class);
            query.setParameter("name", name);
            List<Beer> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Beer", "name", name);
            }
            return result.get(0);
        }
    }

    @Override
    public void create(Beer beer) {
        try (Session session = sessionFactory.openSession()) {
            session.save(beer);
        }
    }

    @Override
    public void update(Beer beer) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(beer);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(id);
            session.getTransaction().commit();
        }
    }
}