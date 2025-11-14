package com.medicine.tracker.service.impl;

import com.medicine.tracker.model.dto.request.ProfileRequest;
import com.medicine.tracker.model.entity.Profile;
import com.medicine.tracker.repository.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceImplTest {

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private ProfileServiceImpl profileService;

    private UUID userId;
    private UUID profileId;
    private ProfileRequest profileRequest;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        profileId = UUID.randomUUID();
        profileRequest = new ProfileRequest();
        profileRequest.setName("John Doe");
    }

    @Test
    void createProfile_shouldSucceedWhenNameIsUnique() {
        // Arrange
        when(profileRepository.existsByUserIdAndName(userId, profileRequest.getName())).thenReturn(false);
        when(profileRepository.save(any(Profile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        var result = profileService.createProfile(userId, profileRequest);

        // Assert
        assertNotNull(result);
        assertEquals(profileRequest.getName(), result.getName());
        verify(profileRepository).existsByUserIdAndName(userId, profileRequest.getName());
        verify(profileRepository).save(any(Profile.class));
    }

    @Test
    void createProfile_shouldFailWhenNameAlreadyExists() {
        // Arrange
        when(profileRepository.existsByUserIdAndName(userId, profileRequest.getName())).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> profileService.createProfile(userId, profileRequest));
        assertEquals("A profile with this name already exists for this user", exception.getMessage());
        verify(profileRepository).existsByUserIdAndName(userId, profileRequest.getName());
        verify(profileRepository, never()).save(any(Profile.class));
    }

    @Test
    void updateProfile_shouldSucceedWhenNameIsUnique() {
        // Arrange
        Profile existingProfile = new Profile();
        existingProfile.setId(profileId);
        existingProfile.setUserId(userId);
        existingProfile.setName("Old Name");
        
        when(profileRepository.findById(profileId)).thenReturn(Optional.of(existingProfile));
        when(profileRepository.existsByUserIdAndNameAndIdNot(userId, profileRequest.getName(), profileId)).thenReturn(false);
        when(profileRepository.save(any(Profile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        var result = profileService.updateProfile(profileId, userId, profileRequest);

        // Assert
        assertNotNull(result);
        assertEquals(profileRequest.getName(), result.getName());
        verify(profileRepository).existsByUserIdAndNameAndIdNot(userId, profileRequest.getName(), profileId);
        verify(profileRepository).save(any(Profile.class));
    }

    @Test
    void updateProfile_shouldFailWhenNameAlreadyExists() {
        // Arrange
        Profile existingProfile = new Profile();
        existingProfile.setId(profileId);
        existingProfile.setUserId(userId);
        existingProfile.setName("Old Name");
        
        when(profileRepository.findById(profileId)).thenReturn(Optional.of(existingProfile));
        when(profileRepository.existsByUserIdAndNameAndIdNot(userId, profileRequest.getName(), profileId)).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> profileService.updateProfile(profileId, userId, profileRequest));
        assertEquals("A profile with this name already exists for this user", exception.getMessage());
        verify(profileRepository).existsByUserIdAndNameAndIdNot(userId, profileRequest.getName(), profileId);
        verify(profileRepository, never()).save(any(Profile.class));
    }
}