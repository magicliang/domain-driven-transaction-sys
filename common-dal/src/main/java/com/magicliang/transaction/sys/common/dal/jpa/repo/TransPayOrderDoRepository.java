package com.magicliang.transaction.sys.common.dal.jpa.repo;

import com.magicliang.transaction.sys.common.dal.jpa.dataObject.TransPayOrderDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TransPayOrderDoRepository extends JpaRepository<TransPayOrderDo, Long>, JpaSpecificationExecutor<TransPayOrderDo> {
}