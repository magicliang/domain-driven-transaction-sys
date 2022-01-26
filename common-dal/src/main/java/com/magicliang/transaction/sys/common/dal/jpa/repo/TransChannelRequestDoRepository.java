package com.magicliang.transaction.sys.common.dal.jpa.repo;

import com.magicliang.transaction.sys.common.dal.jpa.dataObject.TransChannelRequestDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TransChannelRequestDoRepository extends JpaRepository<TransChannelRequestDo, Long>, JpaSpecificationExecutor<TransChannelRequestDo> {
}