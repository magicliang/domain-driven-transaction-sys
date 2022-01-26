package com.magicliang.transaction.sys.common.dal.jpa.repo;

import com.magicliang.transaction.sys.common.dal.jpa.dataObject.TransBankCardSuborderDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TransBankCardSuborderDoRepository extends JpaRepository<TransBankCardSuborderDo, Long>, JpaSpecificationExecutor<TransBankCardSuborderDo> {
}