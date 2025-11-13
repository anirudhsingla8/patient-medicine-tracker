package com.medicine.tracker.repository;

import com.medicine.tracker.model.entity.GlobalMedicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for GlobalMedicine entity operations
 * Provides CRUD operations and custom queries for global medicine management
 */
@Repository
public interface GlobalMedicineRepository extends JpaRepository<GlobalMedicine, UUID> {
    
    /**
     * Find global medicines by name (case-insensitive partial match)
     * @param name The name to search for
     * @return List of global medicines matching the name
     */
    List<GlobalMedicine> findByNameContainingIgnoreCase(String name);
    
    /**
     * Find global medicines by category
     * @param category The category to filter by
     * @return List of global medicines in the category
     */
    List<GlobalMedicine> findByCategory(String category);
    
    /**
     * Find global medicines by manufacturer
     * @param manufacturer The manufacturer to filter by
     * @return List of global medicines by the manufacturer
     */
    List<GlobalMedicine> findByManufacturer(String manufacturer);
}