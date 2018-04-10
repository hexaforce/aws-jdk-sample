package io.hexaforce.history;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationHistoryManager extends JpaRepository<OperationHistory, Integer> {

}
