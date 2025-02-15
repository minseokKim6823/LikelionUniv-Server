package likelion.univ.domain.project.repository;

import likelion.univ.domain.project.entity.Tech;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TechRepository extends JpaRepository<Tech, Long> {
    List<Tech> findTechByTechName(String techName);
}
