package com.company.web.springdemo.services;

import com.company.web.springdemo.Helpers;
import com.company.web.springdemo.exceptions.EntityDuplicateException;
import com.company.web.springdemo.exceptions.EntityNotFoundException;
import com.company.web.springdemo.exceptions.UnauthorizedOperationException;
import com.company.web.springdemo.models.Beer;
import com.company.web.springdemo.repositories.BeerRepository;
import org.hibernate.sql.results.graph.AssemblerCreationState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BeerServiceTests {

    @Mock
    BeerRepository mockRepository;

    @InjectMocks
    BeerServiceImpl service;
    @Test
    public void getById_Should_ReturnBeer_When_MatchExists() {

        Mockito.when(mockRepository.get(2))
                .thenReturn(new Beer(2, "MockBeerName", 1.3, Helpers.createMockStyle(), Helpers.createMockUser()));
        Beer result = service.get(2);

        Assertions.assertEquals(2, result.getId());
        Assertions.assertEquals("MockBeerName", result.getName());
        Assertions.assertEquals(1.3, result.getAbv());
    }

    @Test
    public void create_Should_Throw_When_BeerWithSameName_exists() {

        var mockBeer = Helpers.createMockBeer();

        Mockito.when(mockRepository.get(mockBeer.getName()))
                .thenReturn(mockBeer);

        Assertions.assertThrows(EntityDuplicateException.class,
                () -> service.create(mockBeer, Helpers.createMockUser()));
    }

    @Test
    public void create_should_CallRepo_When_BeerWithSameNameExists() {

        var mockBeer = Helpers.createMockBeer();
        var mockUser = Helpers.createMockUser();


        Mockito.when(mockRepository.get(mockBeer.getName()))
                .thenThrow(new EntityNotFoundException("Beer", "name", mockBeer.getName()));

        service.create(mockBeer, mockUser);

        Mockito.verify(mockRepository, Mockito.times(1))
                .create(Mockito.any(Beer.class));
    }

    @Test
    public void create_Should_Throw_When_BeerNameIsTaken() {
        var mockBeer = Helpers.createMockBeer();
        var anotherMockBeer = Helpers.createMockBeer();
        anotherMockBeer.setId(2);

        Mockito.when(mockRepository.get(Mockito.anyString()))
                .thenReturn(anotherMockBeer);

        Assertions.assertThrows(EntityDuplicateException.class,
                () -> service.create(mockBeer, Helpers.createMockUser()));

    }

    @Test
    public void update_Should_Throw_When_UserIsNotCreatorOrAdmin() {
        var mockBeer = Helpers.createMockBeer();
        var mockUser = Helpers.createMockUser();
        mockBeer.setCreatedBy(mockUser);
        mockUser.setId(5);


        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> service.update(mockBeer, Helpers.createMockUser()));
    }

    @Test
    public void update_should_CallRepo_When_BeerWithSameNameExists() {
        var mockBeer = Helpers.createMockBeer();
        var mockUser = Helpers.createMockUser();


        Mockito.when(mockRepository.get(Mockito.anyInt()))
                .thenReturn(mockBeer);


        service.update(mockBeer, mockUser);

        Mockito.verify(mockRepository, Mockito.times(1))
                .update(Mockito.any(Beer.class));
    }



    @Test
    public void delete_Should_Delete_When_UserIsAdminOrOwner() {
        var mockBeer = Helpers.createMockBeer();
        var mockUser = Helpers.createMockUser();
        mockBeer.setCreatedBy(mockUser);

        Mockito.when(mockRepository.get(Mockito.anyInt()))
                        .thenReturn(mockBeer);

        Assertions.assertEquals(service.delete(1, mockUser), 1);
    }

    @Test
    public void delete_Should_Throw_When_UserIsNotAdminOrOwner() {
        var mockBeer = Helpers.createMockBeer();
        var mockUser = Helpers.createMockUser();
        var anotherMockUser = Helpers.createMockUser();
        anotherMockUser.setId(2);
        mockBeer.setCreatedBy(mockUser);

        Mockito.when(mockRepository.get(Mockito.anyInt()))
                .thenReturn(mockBeer);

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> service.delete(1, anotherMockUser));
    }

}
