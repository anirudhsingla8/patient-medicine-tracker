# Java Application Blueprint: Medicine Tracker Backend

## Overview
This document provides a comprehensive blueprint of the Spring Boot medicine tracker application, designed to help Node.js developers transition to Java by mapping familiar JavaScript concepts to Java equivalents.

## UML Class Diagram

```
┌─────────────────────────────────────────────────┐
│                                                                 UML Class Diagram                                                                 │
├─────────────────────────────────────────────────────────────────────────────────────────┤
│                                                                                                                                   │
│  ┌─────────────────────────────────────────────────────┐ │
│  │                              Spring Boot Application Entry Point                                                            │ │
│  │  ┌─────────────────────────────────────────────────┐ │ │
│  │  │                             MedicineTrackerApplication.java                                                           │ │ │
│  │  │                                                                                                                         │ │ │
│  │  │  + main(String[] args) : void                                                                                         │ │ │
│  │  └─────────────────────────────────────────────────────────┘ │
│  └─────────────────────────────────────────────────────────────────────────────────────────────────────┘ │
│                                                                                                                                   │
│  ┌─────────────────────────────────────────────────────────────────────────────────────┐ │
│  │                                      Configuration Classes                                                                  │ │
│  │  ┌─────────────────────────────────────────────────┐ │ │
│  │  │                           SecurityConfig.java                                                                         │ │ │
│  │  │  - jwtAuthFilter: JwtAuthenticationFilter                                                                              │ │ │
│  │  │  - userDetailsService: UserDetailsService                                                                              │ │ │
│  │  │  + securityFilterChain(HttpSecurity) : SecurityFilterChain                                                             │ │ │
│  │  │  + authenticationProvider() : DaoAuthenticationProvider                                                                │ │ │
│  │  │  + authenticationManager(AuthenticationConfiguration) : AuthenticationManager                                          │ │ │
│  │  │  + passwordEncoder() : PasswordEncoder                                                                                 │ │ │
│  │  │  + userDetailsService() : UserDetailsService                                                                           │ │ │
│  │  └─────────────────────────────────────────────────────────┘ │ │
│  │  ┌─────────────────────────────────────────────────────────────────────────────────┐ │ │
│  │  │                           CloudinaryConfig.java                                                                       │ │ │
│  │  │  - cloudName: String                                                                                                   │ │ │
│  │  │  - apiKey: String                                                                                                      │ │ │
│  │  │  - apiSecret: String                                                                                                   │ │ │
│  │  │  + cloudinary() : Cloudinary                                                                                           │ │ │
│  │  └─────────────────────────────────────────────────────────┘ │
│  │  ┌─────────────────────────────────────────────────────────────────┐ │ │
│  │  │                           SchedulerConfig.java                                                                        │ │ │
│  │  │  + (EnableScheduling annotation)                                                                                       │ │ │
│  │  └─────────────────────────────────────────────────────────────────────────┘ │ │
│  │  ┌─────────────────────────────────────────────────────────────────────────────────────────────────────────┐ │ │
│  │  │                           LoggingConfig.java                                                                          │ │ │
│  │  │  + requestLoggingFilter() : CommonsRequestLoggingFilter                                                                │ │ │
│  │  └─────────────────────────────────────────────────────────────────────────────────┘ │ │
│  └─────────────────────────────────────────────────────────────┘ │
│                                                                                                                                   │
│  ┌─────────────────────────────────────────────────────────────────────────────┐ │
│  │                                      Security Classes                                                                       │ │
│  │  ┌─────────────────────────────────────────────────────────────────────────────────┐ │ │
│  │  │                           JwtAuthenticationFilter.java                                                                  │ │ │
│  │  │  - jwtService: JwtService                                                                                              │ │ │
│  │  │  - userService: UserService                                                                                            │ │ │
│  │  │  - tokenBlacklistService: TokenBlacklistService                                                                        │ │ │
│  │  │  - userRepository: UserRepository                                                                                    │ │ │
│  │  │  + doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain) : void                                      │ │ │
│  │  │  + isPublicEndpoint(HttpServletRequest) : boolean                                                                      │ │ │
│  │  └─────────────────────────────────────────┘ │ │
│  │  ┌─────────────────────────────────────────────────────────────────────────────────────────────────────────┐ │ │
│  │  │                           UserDetailsServiceImpl.java                                                                 │ │ │
│  │  │  - userRepository: UserRepository                                                                                    │ │ │
│  │  │  + loadUserByUsername(String) : UserDetails                                                                            │ │ │
│  │  └─────────────────────────────────────────────────────────────────────────┘ │ │
│  │  ┌─────────────────────────────────────────────────────────┐ │ │
│  │  │                           JwtService.java (Interface)                                                                 │ │ │
│  │  │  + generateToken(User) : String                                                                                        │ │ │
│  │  │  + generateToken(User, Claims) : String                                                                                │ │ │
│  │  │  + isTokenValid(String, User) : boolean                                                                                │ │ │
│  │  │  + extractUsername(String) : String                                                                                    │ │ │
│  │  │  + extractExpiration(String) : Date                                                                                    │ │ │
│  │  │  + extractClaim(String, Function<Claims, T>) : T                                                                       │ │ │
│  │  │  + extractAllClaims(String) : Claims                                                                                   │ │ │
│  │  └─────────────────────────────────────────────────┘ │
│  │  ┌─────────────────────────────────────────────────┐ │ │
│  │  │                           JwtServiceImpl.java                                                                         │ │ │
│  │  │  - jwtSecret: String                                                                                                   │ │ │
│  │  │  - jwtExpiration: Long                                                                                                 │ │ │
│  │  │  + generateToken(User) : String                                                                                        │ │ │
│  │  │  + generateToken(User, Claims) : String                                                                                │ │ │
│  │  │  + isTokenValid(String, User) : boolean                                                                                │ │ │
│  │  │  + extractUsername(String) : String                                                                                    │ │ │
│  │  │  + extractExpiration(String) : Date                                                                                    │ │ │
│  │  │  + extractClaim(String, Function<Claims, T>) : T                                                                       │ │ │
│  │  │  + extractAllClaims(String) : Claims                                                                                   │ │ │
│  │  │  + getSignInKey() : Key                                                                                                │ │ │
│  │  │  + convertToDate(LocalDateTime) : Date                                                                                 │ │ │
│  │  └─────────────────────────────────────────────────┘ │ │
│  └─────────────────────────────────────────────────────────────┘ │
│                                                                                                                                   │
│  ┌─────────────────────────────────────────────────────────────────────────────────────┐ │
│  │                                       Entity Classes                                                                        │ │
│  │  ┌─────────────────────────────────────────────────┐ │ │
│  │  │                           User.java                                                                                     │ │ │
│  │  │  - id: UUID                                                                                                            │ │ │
│  │  │  - email: String                                                                                                       │ │ │
│  │  │  - password: String                                                                                                    │ │ │
│  │  │  - passwordLastChanged: LocalDateTime                                                                                  │ │ │
│  │  │  - createdAt: LocalDateTime                                                                                            │ │ │
│  │  │  - fcmToken: String                                                                                                    │ │ │
│  │  │  + (Getters, Setters, Builder, Lombok annotations)                                                                     │ │ │
│  │  │  + getAuthorities() : Collection<? extends GrantedAuthority>                                                           │ │ │
│  │  │  + getUsername() : String                                                                                              │ │ │
│  │  │  + isAccountNonExpired() : boolean                                                                                     │ │ │
│  │  │  + isAccountNonLocked() : boolean                                                                                      │ │ │
│  │  │  + isCredentialsNonExpired() : boolean                                                                                 │ │ │
│  │  │  + isEnabled() : boolean                                                                                               │ │ │
│  │  └─────────────────────────────────────────────────────────┘ │
│  │  ┌─────────────────────────────────────────────────────────────────┐ │ │
│  │  │                           Profile.java                                                                                │ │ │
│  │  │  - id: UUID                                                                                                            │ │ │
│  │  │  - userId: UUID                                                                                                        │ │ │
│  │  │  - name: String                                                                                                        │ │ │
│  │  │  - createdAt: LocalDateTime                                                                                            │ │ │
│  │  │  + (Getters, Setters, Builder, Lombok annotations)                                                                     │ │ │
│  │  └─────────────────────────────────────────────────────────┘ │ │
│  │  ┌─────────────────────────────────────────────────────────────────┐ │ │
│  │  │                           Medicine.java                                                                               │ │ │
│  │  │  - id: UUID                                                                                                            │ │ │
│  │  │  - userId: UUID                                                                                                        │ │ │
│  │  │  - profileId: UUID                                                                                                     │ │ │
│  │  │  - name: String                                                                                                        │ │ │
│  │  │  - imageUrl: String                                                                                                    │ │ │
│  │  │  - dosage: String                                                                                                      │ │ │
│  │  │  - quantity: Integer                                                                                                   │ │ │
│  │  │ - expiryDate: LocalDate                                                                                               │ │ │
│  │  │  - category: String                                                                                                    │ │ │
│  │  │  - notes: String                                                                                                       │ │ │
│  │  │  - composition: List<Composition>                                                                                      │ │ │
│  │  │  - form: String                                                                                                        │ │ │
│  │  │  - status: MedicineStatus                                                                                              │ │ │
│  │  │  - createdAt: LocalDateTime                                                                                            │ │ │
│  │  │  - updatedAt: LocalDateTime                                                                                            │ │ │
│  │  │  + (Getters, Setters, Builder, Lombok annotations)                                                                     │ │ │
│  │  │  + enum MedicineStatus { ACTIVE, INACTIVE }                                                                            │ │ │
│  │  │  + static class Composition { name, strengthValue, strengthUnit }                                                      │ │ │
│  │  └─────────────────────────────────────────────────────────┘ │
│  │  ┌─────────────────────────────────────────┐ │ │
│  │  │                           Schedule.java                                                                               │ │ │
│  │  │  - id: UUID                                                                                                            │ │ │
│  │  │  - medicineId: UUID                                                                                                    │ │ │
│  │  │  - profileId: UUID                                                                                                     │ │ │
│  │  │  - userId: UUID                                                                                                        │ │ │
│  │  │  - timeOfDay: LocalTime                                                                                                │ │ │
│  │  │  - frequency: Frequency                                                                                               │ │ │
│  │  │  - isActive: Boolean                                                                                                   │ │ │
│  │  │  - createdAt: LocalDateTime                                                                                            │ │ │
│  │  │  + (Getters, Setters, Builder, Lombok annotations)                                                                     │ │ │
│  │  │  + enum Frequency { DAILY, WEEKLY, BIWEEKLY, MONTHLY, CUSTOM }                                                         │ │ │
│  │  └─────────────────────────────────────────────────┘ │ │
│  │  ┌─────────────────────────────────────────────────┐ │ │
│  │  │                           GlobalMedicine.java                                                                         │ │ │
│  │  │  - id: UUID                                                                                                            │ │ │
│  │  │  - name: String                                                                                                        │ │ │
│  │  │  - brandName: String                                                                                                   │ │ │
│  │  │  - genericName: String                                                                                                 │ │ │
│  │  │  - dosageForm: String                                                                                                  │ │ │
│  │  │  - strength: String                                                                                                    │ │ │
│  │  │  - manufacturer: String                                                                                                │ │ │
│  │  │  - description: String                                                                                                 │ │ │
│  │  │  - indications: List<String>                                                                                           │ │ │
│  │ │  - contraindications: List<String>                                                                                     │ │ │
│  │  │  - sideEffects: List<String>                                                                                           │ │ │
│  │  │  - warnings: List<String>                                                                                              │ │ │
│  │  │  - interactions: List<String>                                                                                          │ │ │
│  │  │  - storageInstructions: String                                                                                         │ │ │
│  │  │  - category: String                                                                                                    │ │ │
│  │  │  - atcCode: String                                                                                                     │ │ │
│  │  │  - fdaApprovalDate: LocalDate                                                                                          │ │ │
│  │  │  - createdAt: LocalDateTime                                                                                            │ │ │
│  │  │  - updatedAt: LocalDateTime                                                                                            │ │ │
│  │  │  + (Getters, Setters, Builder, Lombok annotations)                                                                     │ │ │
│  │  └─────────────────────────────────────────────────────────┘ │ │
│  │  ┌─────────────────────────────────────────────────────────┐ │ │
│  │  │                           TokenBlacklist.java                                                                         │ │ │
│  │  │  - id: UUID                                                                                                            │ │ │
│  │  │  - token: String                                                                                                       │ │ │
│  │  │  - userId: UUID                                                                                                        │ │ │
│  │  │  - expiresAt: LocalDateTime                                                                                            │ │ │
│  │  │  - blacklistedAt: LocalDateTime                                                                                        │ │ │
│  │  │  + (Getters, Setters, Builder, Lombok annotations)                                                                     │ │ │
│  │  └─────────────────────────────────────────────────────────┘ │
│  └─────────────────────────────────────────────────────────────┘ │
│                                                                                                                                   │
│  ┌─────────────────────────────────────────────────────────────────────────────────────┐ │
│  │                                    Repository Interfaces                                                                    │ │
│  │  ┌─────────────────────────────────────────────────────────┐ │ │
│  │  │                           UserRepository.java                                                                         │ │ │
│  │  │  + findByEmail(String) : Optional<User>                                                                                │ │ │
│  │  │  + existsByEmail(String) : boolean                                                                                     │ │ │
│  │  └─────────────────────────────────────────────────┘ │ │
│  │  ┌─────────────────────────────────────────┐ │ │
│  │  │                           ProfileRepository.java                                                                      │ │ │
│  │  │  + findByUserId(UUID) : List<Profile>                                                                                  │ │ │
│  │  │  + existsByUserIdAndId(UUID, UUID) : boolean                                                                           │ │ │
│  │  └─────────────────────────────────────────────────────────┘ │
│  │  ┌─────────────────────────────────────────┐ │ │
│  │  │                           MedicineRepository.java                                                                     │ │ │
│  │  │  + findByUserIdAndStatus(UUID, MedicineStatus) : List<Medicine>                                                        │ │ │
│  │  │  + findByProfileIdAndStatus(UUID, MedicineStatus) : List<Medicine>                                                     │ │ │
│  │  │  + findByProfileId(UUID) : List<Medicine>                                                                              │ │ │
│  │  │  + findByUserIdAndProfileIdAndStatus(UUID, UUID, MedicineStatus) : List<Medicine>                                      │ │ │
│  │  │  + existsByUserIdAndId(UUID, UUID) : boolean                                                                           │ │ │
│  │  │  + findExpiringMedicines() : List<Medicine>                                                                            │ │ │
│  │  └─────────────────────────────────────────────────────────┘ │ │
│  │  ┌─────────────────────────────────────────────────────────────────────────────────┐ │ │
│  │  │                           ScheduleRepository.java                                                                     │ │ │
│  │  │  + findByMedicineIdAndIsActiveTrue(UUID) : List<Schedule>                                                              │ │ │
│  │  │  + findByProfileIdAndIsActiveTrue(UUID) : List<Schedule>                                                               │ │ │
│  │  │  + findByUserIdAndIsActiveTrue(UUID) : List<Schedule>                                                                  │ │ │
│  │  │  + findByUserIdAndProfileIdAndIsActiveTrue(UUID, UUID) : List<Schedule>                                                │ │ │
│  │  │  + existsByUserIdAndId(UUID, UUID) : boolean                                                                           │ │ │
│  │  │  + findByTimeOfDayAndIsActiveTrue(LocalTime) : List<Schedule>                                                          │ │ │
│  │  └─────────────────────────────────────────────────────────┘ │ │
│  │  ┌─────────────────────────────────────────────────────────┐ │ │
│  │  │                           GlobalMedicineRepository.java                                                               │ │ │
│  │  │  + findByNameContainingIgnoreCase(String) : List<GlobalMedicine>                                                      │ │ │
│  │  │  + findByCategory(String) : List<GlobalMedicine>                                                                       │ │ │
│  │  │  + findByManufacturer(String) : List<GlobalMedicine>                                                                   │ │ │
│  │  └─────────────────────────────────────────────────────────┘ │
│  │  ┌─────────────────────────────────────────────────────────┐ │ │
│  │  │                           TokenBlacklistRepository.java                                                               │ │ │
│  │  │  + existsByToken(String) : boolean                                                                                     │ │ │
│  │  │  + findByToken(String) : Optional<TokenBlacklist>                                                                      │ │ │
│  │  │  + findByExpiresAtBefore(LocalDateTime) : List<TokenBlacklist>                                                         │ │ │
│  │  │  + findByUserId(UUID) : List<TokenBlacklist>                                                                           │ │ │
│  │  └─────────────────────────────────────────────────────────────────────────────────┘ │ │
│  └─────────────────────────────────────────────────────────────┘ │
│                                                                                                                                   │
│  ┌─────────────────────────────────────────────────────────────┐ │
│  │                                      Service Interfaces & Implementations                                                   │ │
│  │  ┌─────────────────────────────────────────────────┐ │ │
│  │  │                           AuthService.java (Interface)                                                                │ │ │
│  │  │  + register(RegisterRequest) : AuthResponse                                                                            │ │ │
│  │  │  + login(LoginRequest) : AuthResponse                                                                                  │ │ │
│  │  │  + forgotPassword(ForgotPasswordRequest) : AuthResponse                                                                │ │ │
│  │  └─────────────────────────────────────────────────────────┘ │
│  │  ┌─────────────────────────────────────────────────────────────────────────┐ │ │
│  │  │                           AuthServiceImpl.java                                                                        │ │ │
│  │  │  - userRepository: UserRepository                                                                                    │ │ │
│  │  │  - passwordEncoder: PasswordEncoder                                                                                    │ │ │
│  │  │  - jwtService: JwtService                                                                                              │ │ │
│  │  │  - authenticationManager: AuthenticationManager                                                                        │ │ │
│  │  │  - tokenBlacklistService: TokenBlacklistService                                                                        │ │ │
│  │  │  + register(RegisterRequest) : AuthResponse                                                                            │ │ │
│  │  │  + login(LoginRequest) : AuthResponse                                                                                  │ │ │
│  │  │  + forgotPassword(ForgotPasswordRequest) : AuthResponse                                                                │ │ │
│  │  └─────────────────────────────────────────────────────────┘ │ │
│  │  ┌─────────────────────────────────────────┐ │ │
│  │  │                           ProfileService.java (Interface)                                                             │ │ │
│  │  │  + createProfile(UUID, ProfileRequest) : ProfileResponse                                                               │ │ │
│  │  │  + getAllProfiles(UUID) : List<ProfileResponse>                                                                        │ │ │
│  │  │  + getProfileById(UUID, UUID) : ProfileResponse                                                                        │ │ │
│  │  │  + updateProfile(UUID, UUID, ProfileRequest) : ProfileResponse                                                         │ │ │
│  │  │  + deleteProfile(UUID, UUID) : void                                                                                    │ │ │
│  │  │  + profileExistsForUser(UUID, UUID) : boolean                                                                          │ │ │
│  │  └─────────────────────────────────────────────────────────┘ │ │
│  │  ┌─────────────────────────────────────────────────────────┐ │ │
│  │  │                           ProfileServiceImpl.java                                                                     │ │ │
│  │  │  - profileRepository: ProfileRepository                                                                                │ │ │
│  │  │  + createProfile(UUID, ProfileRequest) : ProfileResponse                                                               │ │ │
│  │  │  + getAllProfiles(UUID) : List<ProfileResponse>                                                                        │ │ │
│  │  │  + getProfileById(UUID, UUID) : ProfileResponse                                                                        │ │ │
│  │  │  + updateProfile(UUID, UUID, ProfileRequest) : ProfileResponse                                                         │ │ │
│  │  │  + deleteProfile(UUID, UUID) : void                                                                                    │ │ │
│  │  │  + profileExistsForUser(UUID, UUID) : boolean                                                                          │ │ │
│  │  │  + mapToProfileResponse(Profile) : ProfileResponse                                                                     │ │ │
│  │  └─────────────────────────────────────────────────────────┘ │
│  │  ┌─────────────────────────────────────────────────┐ │ │
│  │  │                           MedicineService.java (Interface)                                                            │ │ │
│  │  │  + createMedicine(UUID, UUID, MedicineRequest) : MedicineResponse                                                      │ │ │
│  │  │  + getAllMedicinesForProfile(UUID, UUID) : List<MedicineResponse>                                                      │ │ │
│  │  │  + getAllMedicinesForUser(UUID) : List<MedicineResponse>                                                               │ │ │
│  │  │  + getMedicineById(UUID, UUID) : MedicineResponse                                                                      │ │ │
│  │  │  + updateMedicine(UUID, UUID, MedicineRequest) : MedicineResponse                                                      │ │ │
│  │  │  + deleteMedicine(UUID, UUID) : void                                                                                   │ │ │
│  │  │  + takeDose(UUID, UUID) : MedicineResponse                                                                              │ │ │
│  │  │  + medicineExistsForUser(UUID, UUID) : boolean                                                                         │ │ │
│  │  └─────────────────────────────────────────────────────────┘ │ │
│  │ ┌─────────────────────────────────────────────────────────┐ │ │
│  │  │                           MedicineServiceImpl.java                                                                    │ │ │
│  │  │  - medicineRepository: MedicineRepository                                                                              │ │ │
│  │  │  - profileRepository: ProfileRepository                                                                                │ │ │
│  │  │  + createMedicine(UUID, UUID, MedicineRequest) : MedicineResponse                                                      │ │ │
│  │  │  + getAllMedicinesForProfile(UUID, UUID) : List<MedicineResponse>                                                      │ │ │
│  │  │  + getAllMedicinesForUser(UUID) : List<MedicineResponse>                                                               │ │ │
│  │  │  + getMedicineById(UUID, UUID) : MedicineResponse                                                                      │ │ │
│  │  │  + updateMedicine(UUID, UUID, MedicineRequest) : MedicineResponse                                                      │ │ │
│  │  │  + deleteMedicine(UUID, UUID) : void                                                                                   │ │ │
│  │  │  + takeDose(UUID, UUID) : MedicineResponse                                                                              │ │ │
│  │  │  + medicineExistsForUser(UUID, UUID) : boolean                                                                         │ │ │
│  │  │  + mapToMedicineResponse(Medicine) : MedicineResponse                                                                  │ │ │
│  │  └─────────────────────────────────────────────────────────────────────────────────┘ │ │
│  │  ┌─────────────────────────────────────────┐ │ │
│  │  │                           ScheduleService.java (Interface)                                                            │ │ │
│  │  │  + createSchedule(UUID, UUID, ScheduleRequest) : ScheduleResponse                                                      │ │ │
│  │  │  + getSchedulesForMedicine(UUID, UUID) : List<ScheduleResponse>                                                        │ │ │
│  │  │  + getSchedulesForProfile(UUID, UUID) : List<ScheduleResponse>                                                         │ │ │
│  │  │  + getSchedulesForUser(UUID) : List<ScheduleResponse>                                                                  │ │ │
│  │  │  + getScheduleById(UUID, UUID) : ScheduleResponse                                                                      │ │ │
│  │  │  + updateSchedule(UUID, UUID, ScheduleRequest) : ScheduleResponse                                                      │ │ │
│  │  │  + deleteSchedule(UUID, UUID) : void                                                                                   │ │ │
│  │  │  + scheduleExistsForUser(UUID, UUID) : boolean                                                                         │ │ │
│  │  └─────────────────────────────────────────────────────────┘ │
│  │  ┌─────────────────────────────────────────────────────────┐ │ │
│  │  │                           ScheduleServiceImpl.java                                                                    │ │ │
│  │  │  - scheduleRepository: ScheduleRepository                                                                              │ │ │
│  │  │  - medicineRepository: MedicineRepository                                                                              │ │ │
│  │  │  + createSchedule(UUID, UUID, ScheduleRequest) : ScheduleResponse                                                      │ │ │
│  │  │  + getSchedulesForMedicine(UUID, UUID) : List<ScheduleResponse>                                                        │ │ │
│  │  │  + getSchedulesForProfile(UUID, UUID) : List<ScheduleResponse>                                                         │ │ │
│  │  │  + getSchedulesForUser(UUID) : List<ScheduleResponse>                                                                  │ │ │
│  │  │  + getScheduleById(UUID, UUID) : ScheduleResponse                                                                      │ │ │
│  │  │  + updateSchedule(UUID, UUID, ScheduleRequest) : ScheduleResponse                                                      │ │ │
│  │  │  + deleteSchedule(UUID, UUID) : void                                                                                   │ │ │
│  │  │  + scheduleExistsForUser(UUID, UUID) : boolean                                                                         │ │ │
│  │  │  + mapToScheduleResponse(Schedule) : ScheduleResponse                                                                  │ │ │
│  │  └─────────────────────────────────────────────────────────┘ │ │
│  │  ┌─────────────────────────────────────────────────┐ │ │
│  │  │                           GlobalMedicineService.java (Interface)                                                      │ │ │
│  │  │  + createGlobalMedicine(GlobalMedicineRequest) : GlobalMedicineResponse                                                │ │ │
│  │  │  + getAllGlobalMedicines() : List<GlobalMedicineResponse>                                                              │ │ │
│  │  │  + getGlobalMedicineById(UUID) : GlobalMedicineResponse                                                                │ │ │
│  │  │  + searchGlobalMedicinesByName(String) : List<GlobalMedicineResponse>                                                  │ │ │
│  │  │  + getGlobalMedicinesByCategory(String) : List<GlobalMedicineResponse>                                                 │ │ │
│  │  │  + updateGlobalMedicine(UUID, GlobalMedicineRequest) : GlobalMedicineResponse                                          │ │ │
│  │  │  + deleteGlobalMedicine(UUID) : void                                                                                   │ │ │
│  │  └─────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘ │ │
│  │  ┌─────────────────────────────────────────────────────────────────────────────────┐ │ │
│  │  │                           GlobalMedicineServiceImpl.java                                                              │ │ │
│  │  │  - globalMedicineRepository: GlobalMedicineRepository                                                                  │ │ │
│  │  │  + createGlobalMedicine(GlobalMedicineRequest) : GlobalMedicineResponse                                                │ │ │
│  │  │  + getAllGlobalMedicines() : List<GlobalMedicineResponse>                                                              │ │ │
│  │  │  + getGlobalMedicineById(UUID) : GlobalMedicineResponse                                                                │ │ │
│  │  │  + searchGlobalMedicinesByName(String) : List<GlobalMedicineResponse>                                                  │ │ │
│  │  │  + getGlobalMedicinesByCategory(String) : List<GlobalMedicineResponse>                                                 │ │ │
│  │  │  + updateGlobalMedicine(UUID, GlobalMedicineRequest) : GlobalMedicineResponse                                          │ │ │
│  │  │  + deleteGlobalMedicine(UUID) : void                                                                                   │ │ │
│  │  │  + mapToGlobalMedicineResponse(GlobalMedicine) : GlobalMedicineResponse                                                │ │ │
│  │  └─────────────────────────────────────────────────────────────────────────┘ │ │
│  │  ┌─────────────────────────────────────────────────────────────────────────┐ │ │
│  │ │                           UserService.java (Interface)                                                                │ │ │
│  │  │  + updateFcmToken(UUID, FcmTokenRequest) : void                                                                        │ │ │
│  │  │  + getUserById(UUID) : User                                                                                            │ │ │
│  │  └─────────────────────────────────────────────────────────┘ │
│  │  ┌─────────────────────────────────────────────────────────┐ │ │
│  │  │                           UserServiceImpl.java                                                                        │ │ │
│  │  │  - userRepository: UserRepository                                                                                      │ │ │
│  │  │  + updateFcmToken(UUID, FcmTokenRequest) : void                                                                        │ │ │
│  │  │  + getUserById(UUID) : User                                                                                            │ │ │
│  │  └─────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘ │ │
│  │  ┌─────────────────────────────────────────────────────────────────────────────────┐ │ │
│  │  │                           TokenBlacklistService.java (Interface)                                                      │ │ │
│  │  │  + blacklistToken(String, UUID, LocalDateTime) : void                                                                  │ │ │
│  │  │  + isTokenBlacklisted(String) : boolean                                                                                │ │ │
│  │  │  + blacklistAllUserTokens(UUID) : void                                                                                 │ │ │
│  │  │  + removeExpiredTokens() : void                                                                                        │ │ │
│  │  └─────────────────────────────────────────────────────────┘ │
│  │  ┌─────────────────────────────────────────┐ │ │
│  │  │                           TokenBlacklistServiceImpl.java                                                              │ │ │
│  │  │  - tokenBlacklistRepository: TokenBlacklistRepository                                                                  │ │ │
│  │  │  + blacklistToken(String, UUID, LocalDateTime) : void                                                                  │ │ │
│  │  │  + isTokenBlacklisted(String) : boolean                                                                                │ │ │
│  │  │  + blacklistAllUserTokens(UUID) : void                                                                                 │ │ │
│  │  │  + removeExpiredTokens() : void                                                                                        │ │ │
│  │  └─────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘ │ │
│  │  ┌─────────────────────────────────────────────────────────┐ │ │
│  │ │                           ImageUploadService.java (Interface)                                                         │ │ │
│  │  │  + uploadImage(MultipartFile) : String                                                                                 │ │ │
│  │  │  + deleteImage(String) : boolean                                                                                       │ │ │
│  │  └─────────────────────────────────────────────────────────────────────────────────┘ │ │
│  │  ┌─────────────────────────────────────────────────────────────────────────┐ │ │
│  │  │                           ImageUploadServiceImpl.java                                                                 │ │ │
│  │  │  - cloudinary: Cloudinary                                                                                              │ │ │
│  │  │  + uploadImage(MultipartFile) : String                                                                                 │ │ │
│  │  │  + deleteImage(String) : boolean                                                                                       │ │ │
│  │  │  + extractPublicIdFromUrl(String) : String                                                                             │ │ │
│  │  └─────────────────────────────────────────────────────────────────────────────────┘ │ │
│  │  ┌─────────────────────────────────────────────────┐ │ │
│  │  │                           NotificationService.java (Interface)                                                        │ │ │
│  │  │  + sendDosageReminders() : void                                                                                        │ │ │
│  │  │  + sendExpiryNotifications() : void                                                                                    │ │ │
│  │  │  + sendNotification(String, String, String) : void                                                                     │ │ │
│  │  │  + getDueSchedules() : List<Schedule>                                                                                  │ │ │
│  │  └─────────────────────────────────────────────────────────┘ │
│  │  ┌─────────────────────────────────────────────────────────────────────────────────┐ │ │
│  │  │                           NotificationServiceImpl.java                                                                │ │ │
│  │  │  - scheduleRepository: ScheduleRepository                                                                              │ │ │
│  │  │  - medicineRepository: MedicineRepository                                                                              │ │ │
│  │  │  - userRepository: UserRepository                                                                                      │ │ │
│  │  │  + sendDosageReminders() : void                                                                                        │ │ │
│  │  │  + sendExpiryNotifications() : void                                                                                    │ │ │
│  │  │  + sendNotification(String, String, String) : void                                                                     │ │ │
│  │  │  + getDueSchedules() : List<Schedule>                                                                                  │ │ │
│  │  │  + getMedicineName(UUID) : String                                                                                      │ │ │
│  │  └─────────────────────────────────────────────────────────┘ │ │
│  └─────────────────────────────────────────────────────────────────────────────────────────────────────┘ │
│                                                                                                                                   │
│  ┌─────────────────────────────────────────────────────────────────────────────┐ │
│  │                                      Controller Classes                                                                     │ │
│  │  ┌─────────────────────────────────────────────────────────┐ │ │
│  │  │                           AuthController.java                                                                         │ │ │
│  │  │  - authService: AuthService                                                                                            │ │ │
│  │  │  + register(RegisterRequest) : ResponseEntity<AuthResponse>                                                             │ │ │
│  │  │  + login(LoginRequest) : ResponseEntity<AuthResponse>                                                                   │ │ │
│  │  │  + forgotPassword(ForgotPasswordRequest) : ResponseEntity<AuthResponse>                                                 │ │ │
│  │  └─────────────────────────────────────────────────────────┘ │ │
│  │  ┌─────────────────────────────────────────────────┐ │ │
│  │  │                           ProfileController.java                                                                      │ │ │
│  │  │  - profileService: ProfileService                                                                                      │ │ │
│  │  │  + createProfile(ProfileRequest) : ResponseEntity<ProfileResponse>                                                     │ │ │
│  │  │  + getAllProfiles() : ResponseEntity<List<ProfileResponse>>                                                            │ │ │
│  │  │  + getProfileById(UUID) : ResponseEntity<ProfileResponse>                                                              │ │ │
│  │  │  + updateProfile(UUID, ProfileRequest) : ResponseEntity<ProfileResponse>                                               │ │ │
│  │  │  + deleteProfile(UUID) : ResponseEntity<Void>                                                                          │ │ │
│  │  └─────────────────────────────────────────────────────────┘ │ │
│  │  ┌─────────────────────────────────────────────────────────┐ │ │
│  │  │                           MedicineController.java                                                                     │ │ │
│  │  │  - medicineService: MedicineService                                                                                    │ │ │
│  │  │  - imageUploadService: ImageUploadService                                                                              │ │ │
│  │  │  + createMedicine(UUID, MedicineRequest) : ResponseEntity<MedicineResponse>                                            │ │ │
│  │  │  + getAllMedicinesForProfile(UUID) : ResponseEntity<List<MedicineResponse>>                                            │ │ │
│  │  │  + getAllMedicinesForUser() : ResponseEntity<List<MedicineResponse>>                                                   │ │ │
│  │  │  + getMedicineById(UUID) : ResponseEntity<MedicineResponse>                                                            │ │ │
│  │  │  + updateMedicine(UUID, MedicineRequest) : ResponseEntity<MedicineResponse>                                            │ │ │
│  │  │  + deleteMedicine(UUID) : ResponseEntity<Void>                                                                         │ │ │
│  │  │  + takeDose(UUID) : ResponseEntity<MedicineResponse>                                                                   │ │ │
│  │  │  + uploadMedicineImage(MultipartFile) : ResponseEntity<String>                                                         │ │ │
│  │  └─────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘ │ │
│  │  ┌─────────────────────────────────────────────────────────┐ │ │
│  │  │                           ScheduleController.java                                                                     │ │ │
│  │  │  - scheduleService: ScheduleService                                                                                    │ │ │
│  │  │  + createSchedule(UUID, ScheduleRequest) : ResponseEntity<ScheduleResponse>                                            │ │ │
│  │  │  + getSchedulesForMedicine(UUID) : ResponseEntity<List<ScheduleResponse>>                                              │ │ │
│  │  │  + getSchedulesForProfile(UUID) : ResponseEntity<List<ScheduleResponse>>                                               │ │ │
│  │  │  + getSchedulesForUser() : ResponseEntity<List<ScheduleResponse>>                                                      │ │ │
│  │  │  + getScheduleById(UUID) : ResponseEntity<ScheduleResponse>                                                            │ │ │
│  │  │  + updateSchedule(UUID, ScheduleRequest) : ResponseEntity<ScheduleResponse>                                            │ │ │
│  │  │  + deleteSchedule(UUID) : ResponseEntity<Void>                                                                         │ │ │
│  │  └─────────────────────────────────────────────────────────┘ │ │
│  │  ┌─────────────────────────────────────────────────────────┐ │ │
│  │  │                           GlobalMedicineController.java                                                               │ │ │
│  │  │  - globalMedicineService: GlobalMedicineService                                                                        │ │ │
│  │  │  + createGlobalMedicine(GlobalMedicineRequest) : ResponseEntity<GlobalMedicineResponse>                                │ │ │
│  │  │  + getAllGlobalMedicines() : ResponseEntity<List<GlobalMedicineResponse>>                                              │ │ │
│  │  │  + getGlobalMedicineById(UUID) : ResponseEntity<GlobalMedicineResponse>                                                │ │ │
│  │  │  + searchGlobalMedicinesByName(String) : ResponseEntity<List<GlobalMedicineResponse>>                                  │ │ │
│  │  │  + getGlobalMedicinesByCategory(String) : ResponseEntity<List<GlobalMedicineResponse>>                                 │ │ │
│  │  │  + updateGlobalMedicine(UUID, GlobalMedicineRequest) : ResponseEntity<GlobalMedicineResponse>                          │ │ │
│  │  │  + deleteGlobalMedicine(UUID) : ResponseEntity<Void>                                                                   │ │ │
│  │  └─────────────────────────────────────────────────────────────────────────────────┘ │ │
│  │  ┌─────────────────────────────────────────────────────────┐ │ │
│  │  │                           UserController.java                                                                         │ │ │
│  │  │  - userService: UserService                                                                                            │ │ │
│  │  │  + updateFcmToken(FcmTokenRequest) : ResponseEntity<Void>                                                              │ │ │
│  │  └─────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘ │ │
│  │  ┌─────────────────────────────────────────────────────────────────────────┐ │ │
│  │  │                           HealthCheckController.java                                                                  │ │ │
│  │  │  + getServerStatus() : ResponseEntity<Map<String, Object>>                                                             │ │ │
│  │  └─────────────────────────────────────────────────────────────────────────┘ │ │
│  └─────────────────────────────────────────────────────────────────────────────────────┘ │
│                                                                                                                                   │
│  ┌─────────────────────────────────────────────────────────────────────────────────────────────────────┐ │
│  │                                      DTO Classes (Data Transfer Objects)                                                    │ │
│  │  ┌─────────────────────────────────────────────────────────┐ │ │
│  │  │                           Request DTOs                                                                                │ │ │
│  │  │  LoginRequest, RegisterRequest, ForgotPasswordRequest, ProfileRequest,                                                 │ │ │
│  │  │  MedicineRequest, ScheduleRequest, GlobalMedicineRequest, FcmTokenRequest                                              │ │ │
│  │  │  + (fields, validation annotations, Lombok annotations)                                                                │ │ │
│  │  └─────────────────────────────────────────────────────────┘ │
│  │  ┌─────────────────────────────────────────────────────────────────────────────────┐ │ │
│  │  │                           Response DTOs                                                                               │ │ │
│  │  │  AuthResponse, ProfileResponse, MedicineResponse, ScheduleResponse,                                                    │ │ │
│  │  │  GlobalMedicineResponse                                                                                                │ │ │
│  │  │  + (fields, Lombok annotations)                                                                                        │ │ │
│  │  └─────────────────────────────────────────────────────────────────────────────────────────────────────────┘ │ │
│  └─────────────────────────────────────────────────────────────────────────────────────┘ │
│                                                                                                                                   │
│  ┌─────────────────────────────────────────────────────┐ │
│  │                                      Exception Classes                                                                      │ │
│  │  ┌─────────────────────────────────────────────────────────┐ │ │
│  │  │                           GlobalExceptionHandler.java                                                                 │ │ │
│  │  │  + handleValidationExceptions(MethodArgumentNotValidException) : ResponseEntity<ErrorResponse>                         │ │ │
│  │  │  + handleResourceNotFoundException(ResourceNotFoundException) : ResponseEntity<ErrorResponse>                           │ │ │
│  │  │  + handleUnauthorizedException(UnauthorizedException) : ResponseEntity<ErrorResponse>                                  │ │ │
│  │  │  + handleGenericException(Exception) : ResponseEntity<ErrorResponse>                                                   │ │ │
│  │  │  + handleRuntimeException(RuntimeException) : ResponseEntity<ErrorResponse>                                            │ │ │
│  │  └─────────────────────────────────────────────────────────┘ │ │
│  │  ┌─────────────────────────────────────────────────────────┐ │ │
│  │  │                           Custom Exceptions                                                                           │ │ │
│  │  │  ResourceNotFoundException, UnauthorizedException                                                                      │ │ │
│  │  │  + (constructors)                                                                                                      │ │ │
│  │  └─────────────────────────────────────────────────┘ │ │
│  │  ┌─────────────────────────────────────────────────────────────────────────────────┐ │ │
│  │  │                           ErrorResponse.java                                                                          │ │ │
│  │  │  - timestamp: LocalDateTime                                                                                            │ │ │
│  │  │  - status: int                                                                                                         │ │ │
│  │  │  - error: String                                                                                                       │ │ │
│  │  │  - message: String                                                                                                     │ │ │
│  │  │  - details: Map<String, String>                                                                                        │ │ │
│  │  │  + (Getters, Setters, Builder, Lombok annotations)                                                                     │ │ │
│  │  └─────────────────────────────────────────────────────────┘ │
│  └─────────────────────────────────────────────────────────────────────────────────────┘ │
│                                                                                                                                   │
│  ┌─────────────────────────────────────────────────────────────────────────────┐ │
│  │                                      Scheduled Task Classes                                                                 │ │
│  │  ┌─────────────────────────────────────────────────────────────────────────────────┐ │ │
│  │  │                           MedicineNotificationScheduler.java                                                          │ │ │
│  │  │  - notificationService: NotificationService                                                                            │ │ │
│  │  │  + sendDosageReminders() : void                                                                                        │ │ │
│  │  │  + sendExpiryNotifications() : void                                                                                    │ │ │
│  │  └─────────────────────────────────────────────────────────┘ │ │
│  └─────────────────────────────────────────────────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────────────────────────────┘
```

## Java to JavaScript/Node.js Concept Mapping

| Java Concept | JavaScript/Node.js Equivalent | Description |
|--------------|-------------------------------|-------------|
| `class` | `class` or `function constructor` | Defines a blueprint for objects |
| `interface` | `abstract class` or `duck typing` | Defines contracts that classes must implement |
| `@RestController` | Express.js route handlers | Handles HTTP requests and responses |
| `@Service` | Business logic modules | Contains application business logic |
| `@Repository` | Database access layer | Handles database operations |
| `@Entity` | Mongoose schema/model | Represents database tables/entities |
| `@Autowired` | `require()` or dependency injection | Injects dependencies automatically |
| `Optional<T>` | `null` checks or optional chaining | Handles potentially null values safely |
| `@Valid` | Input validation middleware | Validates request data |
| `try-catch` | `try-catch` | Error handling mechanism |
| `@ExceptionHandler` | Error handling middleware | Handles exceptions globally |
| `@Configuration` | Configuration modules | Defines configuration beans |
| `@Bean` | Factory functions | Creates and manages objects in Spring container |
| `@Component` | Utility modules | Generic Spring-managed components |
| `@Slf4j` | Logging libraries (winston, console.log) | Logging functionality |

## Execution Flow & Method Invocation Sequence

### 1. Application Startup Flow
```
MedicineTrackerApplication.main()
  ↓
Spring Boot auto-configuration
  ↓
Configuration classes loaded (@Configuration)
  ↓
Security configuration applied
  ↓
Database connection established
  ↓
Controllers registered
  ↓
Application ready to receive requests
```

### 2. User Registration Flow
```
┌─────────────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                User Registration Flow                                                                   │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                                                         │
│  Frontend/Client                                                                                                       │
│         │                                                                                                               │
│         │ POST /api/auth/register                                                                                       │
│         │ {email, password}                                                                                             │
│         ▼                                                                                                               │
│  AuthController.register()                                                                                              │
│         │                                                                                                               │
│         │ 1. Validate request data (@Valid)                                                                             │
│         │ 2. Call authService.register()                                                                                │
│         ▼                                                                                                               │
│  AuthServiceImpl.register()                                                                                             │
│         │                                                                                                               │
│         │ 1. Check if user exists (userRepository.existsByEmail())                                                      │
│         │ 2. Create new User entity                                                                                     │
│         │ 3. Encode password (passwordEncoder.encode())                                                                 │
│         │ 4. Save user to database (userRepository.save())                                                              │
│         │ 5. Generate JWT token (jwtService.generateToken())                                                            │
│         ▼                                                                                                               │
│  JWT Token Generated                                                                                                    │
│         │                                                                                                               │
│         │ Return AuthResponse with token                                                                                │
│         ▼                                                                                                               │
│  ResponseEntity<AuthResponse> returned to client                                                                        │
│                                                                                                                         │
└─────────────────────────────────────────────────────────────────────────────────┘
```

### 3. User Login Flow
```
┌─────────────────────────────────────────────────┐
│                                  User Login Flow                                                                        │
├─────────────────────────────────────────────────┤
│                                                                                                                         │
│  Frontend/Client                                                                                                       │
│         │                                                                                                               │
│         │ POST /api/auth/login                                                                                          │
│         │ {email, password}                                                                                             │
│         ▼                                                                                                               │
│  AuthController.login()                                                                                                 │
│         │                                                                                                               │
│         │ 1. Validate request data (@Valid)                                                                             │
│         │ 2. Call authService.login()                                                                                   │
│         ▼                                                                                                               │
│  AuthServiceImpl.login()                                                                                                │
│         │                                                                                                               │
│         │ 1. Authenticate user (authenticationManager.authenticate())                                                    │
│         │ 2. Generate JWT token (jwtService.generateToken())                                                            │
│         ▼                                                                                                               │
│  JWT Token Generated                                                                                                    │
│         │                                                                                                               │
│         │ Return AuthResponse with token                                                                                │
│         ▼                                                                                                               │
│  ResponseEntity<AuthResponse> returned to client                                                                        │
│                                                                                                                         │
└─────────────────────────────────────────────────────────────────┘
```

### 4. Medicine Creation Flow (Authenticated)
```
┌─────────────────────────────────────────────────────────────────────────────────────────────────────────┐
│                              Medicine Creation Flow                                                                     │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                                                         │
│  Frontend/Client                                                                                                       │
│         │                                                                                                               │
│         │ POST /api/medicines/profiles/{profileId}/medicines                                                            │
│         │ Authorization: Bearer <token>                                                                                 │
│         │ {name, dosage, quantity, expiryDate, ...}                                                                     │
│         ▼                                                                                                               │
│  JwtAuthenticationFilter.doFilterInternal()                                                                            │
│         │                                                                                                               │
│         │ 1. Extract token from Authorization header                                                                    │
│         │ 2. Validate token (jwtService.isTokenValid())                                                                 │
│         │ 3. Set authentication in SecurityContext                                                                      │
│         ▼                                                                                                               │
│  MedicineController.createMedicine()                                                                                    │
│         │                                                                                                               │
│         │ 1. Extract user ID from authentication principal                                                              │
│         │ 2. Validate request data (@Valid)                                                                             │
│         │ 3. Call medicineService.createMedicine()                                                                      │
│         ▼                                                                                                               │
│  MedicineServiceImpl.createMedicine()                                                                                   │
│         │                                                                                                               │
│         │ 1. Verify profile belongs to user (profileRepository.existsByUserIdAndId())                                   │
│         │ 2. Create Medicine entity                                                                                     │
│         │ 3. Set status to ACTIVE                                                                                       │
│         │ 4. Save to database (medicineRepository.save())                                                               │
│         │ 5. Map to response DTO                                                                                        │
│         ▼                                                                                                               │
│  MedicineResponse created                                                                                               │
│         │                                                                                                               │
│         │ Return ResponseEntity<MedicineResponse>                                                                       │
│         ▼                                                                                                               │
│  ResponseEntity<MedicineResponse> returned to client                                                                    │
│                                                                                                                         │
└─────────────────────────────────────────────────────────────────────────────────────────────────┘
```

### 5. JWT Token Validation Flow
```
┌─────────────────────────────────────────────────────────────────────────────────────────────────┐
│                            JWT Token Validation Flow                                                                    │
├─────────────────────────────────────────────────────────────────────────────────────────────────┤
│                                                                                                                         │
│  HTTP Request with Authorization Header                                                                                │
│         │                                                                                                               │
│         │ Authorization: Bearer <token>                                                                                 │
│         ▼                                                                                                               │
│  JwtAuthenticationFilter.doFilterInternal()                                                                            │
│         │                                                                                                               │
│         │ 1. Check if endpoint is public (isPublicEndpoint())                                                          │
│         │ 2. Extract JWT from Authorization header                                                                      │
│         │ 3. Check if token is blacklisted (tokenBlacklistService.isTokenBlacklisted())                                 │
│         │ 4. Extract username from JWT (jwtService.extractUsername())                                                   │
│         │ 5. Load user from database (userRepository.findByEmail())                                                     │
│         │ 6. Validate token (jwtService.isTokenValid())                                                                 │
│         │    └─ Check token expiration                                                                                  │
│         │    └─ Check if token issued before password change                                                            │
│         │ 7. Set authentication in SecurityContext if valid                                                             │
│         ▼                                                                                                               │
│  Request proceeds to Controller if token is valid,                                                                     │
│  otherwise returns 401 Unauthorized                                                                                    │
│                                                                                                                         │
└─────────────────────────────────────────────────────────────────────────────────┘
```

### 6. Scheduled Task Flow (Notifications)
```
┌─────────────────────────────────────────────────────────────────────────────────────────────────┐
│                          Scheduled Notification Flow                                                                    │
├─────────────────────────────────────────────────────────────────────────────────────────┤
│                                                                                                                         │
│  Spring Scheduler Trigger (every minute for dosage, daily for expiry)                                                  │
│         │                                                                                                               │
│         │ MedicineNotificationScheduler.sendDosageReminders()                                                           │
│         ▼                                                                                                               │
│  NotificationServiceImpl.sendDosageReminders()                                                                         │
│         │                                                                                                               │
│         │ 1. Get due schedules (getDueSchedules())                                                                      │
│         │ 2. For each schedule:                                                                                         │
│         │    └─ Get user by schedule.getUserId()                                                                        │
│         │    └─ Check if user has FCM token                                                                            │
│         │    └─ Send notification via sendNotification()                                                                │
│         ▼                                                                                                               │
│  NotificationServiceImpl.sendNotification()                                                                             │
│         │                                                                                                               │
│         │ 1. Send FCM notification (simulated in this implementation)                                                   │
│         │ 2. Log notification sent                                                                                      │
│         ▼                                                                                                               │
│  Notification sent to user's device                                                                                    │
│                                                                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```

### 7. Data Flow Between Components

```
┌─────────────────────────────────────────────────────────────────────────────────────────────────┐
│                          Data Flow Between Components                                                                   │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                                                         │
│  Client Request → Controller → Service → Repository → Database                                                         │
│         │              │            │            │        │                                                              │
│         │              │            │            │        └─ Entity objects (Java classes representing DB records)      │
│         │              │            │            └─ JPA/Hibernate for DB operations                                     │
│         │              │            └─ Business logic, validation, transaction management                               │
│         │              └─ Request mapping, validation, response formatting                                              │
│         └─ HTTP request with JSON payload                                                                               │
│                                                                                                                         │
│  Database → Repository → Service → Controller → Client Response                                                        │
│         │            │            │            │        │                                                              │
│         │            │            │            │        └─ ResponseEntity with JSON                                     │
│         │            │            │            └─ DTO mapping for response                                              │
│         │            │            └─ Business logic processing                                                          │
│         │            └─ Data access and entity conversion                                                              │
│         └─ Raw data from database                                                                                       │
│                                                                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```

## Key Spring Boot Concepts for Node.js Developers

### 1. Dependency Injection
- **Java**: Spring automatically injects dependencies using `@Autowired`
- **JavaScript**: Similar to using dependency injection containers or manual `require()`/`import`

### 2. Inversion of Control (IoC)
- **Java**: Spring manages object lifecycle and dependencies
- **JavaScript**: Similar to how frameworks like Express manage middleware and routing

### 3. Annotations vs Middleware
- **Java**: `@RestController`, `@Service`, `@Repository` define component roles
- **JavaScript**: Express middleware functions define request handling behavior

### 4. Configuration
- **Java**: `application.properties` with environment variable substitution `${VAR:default}`
- **JavaScript**: Environment variables accessed via `process.env.VAR || 'default'`

### 5. Error Handling
- **Java**: Global exception handler with `@ControllerAdvice` and `@ExceptionHandler`
- **JavaScript**: Express error handling middleware

### 6. Data Validation
- **Java**: Built-in validation with annotations like `@NotBlank`, `@Email`
- **JavaScript**: Validation libraries like Joi or custom validation middleware

This blueprint provides a comprehensive overview of the Spring Boot application architecture, showing how components interact and how the application processes requests from start to finish.