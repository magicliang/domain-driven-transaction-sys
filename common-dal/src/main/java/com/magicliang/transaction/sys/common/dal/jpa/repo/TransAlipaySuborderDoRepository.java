package com.magicliang.transaction.sys.common.dal.jpa.repo;

import com.magicliang.transaction.sys.common.dal.jpa.dataObject.TransAlipaySuborderDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TransAlipaySuborderDoRepository extends JpaRepository<TransAlipaySuborderDo, Long>, JpaSpecificationExecutor<TransAlipaySuborderDo> {
}