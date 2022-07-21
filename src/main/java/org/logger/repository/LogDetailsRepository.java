package org.logger.repository;

import org.logger.entity.LogDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogDetailsRepository extends CrudRepository<LogDetails, String> {
}
