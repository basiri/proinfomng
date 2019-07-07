
package com.abs.cmtl.proinfomng.repository;

import com.abs.cmtl.proinfomng.model.Category;
import com.abs.cmtl.proinfomng.model.Product;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CategoryRepository extends MongoRepository<Category,String> {


}

