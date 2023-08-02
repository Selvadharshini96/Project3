
package com.example.anu.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.example.anu.model.TutorialModel;

public interface TutorialRepository extends JpaRepository<TutorialModel, Long> {

}